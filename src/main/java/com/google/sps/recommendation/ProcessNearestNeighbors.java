package com.google.sps.recommendation;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import com.google.sps.tool.ArrayUtil;
import org.mapdb.*;

/* This function of this file is to ONLY pre-process the nearest neigbors of 300,000 words.
 * It will create/update a binary .db file. Time results and optimal batch size
 * are based on running this file on a Google Pixelbook. Time result may change for
 * different hardwares.
 *
 * Processing Time:
 * On a Google Pixelbook, a batch size of 500 words results in the fastest runtime for
 * computing the nearest neighbors, while avoiding OOMs. We may see some speedup by
 * migrating from N4dj matrix operations to a numerical computing library like
 * numpy.
 *
 * Time example:
 * Each batch of 500 words takes about 60,000 - 80,000 ms.
 * Processing 100 batches can take from 1 hour 30 minutes - 2 hours.
 *
 * Some underlying reasons for this slow time performance:
 * 1) N4dj's behind the hood operations. For example, to slice a large matrix by a range of indices,
 *    N4dj has to make a copy which is really slow, especially with a big matrix.
 * 2) Each computation of a batch results in sorting the
 *    cosine similarity values in each row of size 300,000.
 * 3) There is some time factor with serializing the result into the .db file.
 *    Note: We use MapDB API to serialize a HashMap Object, which utilizes Random Access Files (RAF)
 */
public class ProcessNearestNeighbors {
  public static void main(String[] args) throws IOException {
    String path =
        Paths.get("/home/ngothomas/GoogleNews-vectors-negative300-SLIM.bin.gz").toString();
    WordVectors vec = WordVectorSerializer.readWord2VecModel(path);
    InMemoryLookupTable lookupTable = (InMemoryLookupTable) vec.lookupTable();
    VocabCache lookupCache = (VocabCache) vec.vocab();

    /* Create an array that maps index to word,
     * where the index represents where the word is
     * in the word vector matrix 300,000 x 300 */
    String[] indexToWord = new String[300000];
    for (Object i : lookupCache.words()) {
      String word = (String) i;
      int index = vec.indexOf(word);
      indexToWord[index] = word;
    }

    /* Get raw word vectors 300,000 x 300 matrix (syn0) and normalize it (vecA)
     * syn0 naming comes from the original Word2Vec paper */
    INDArray syn0 = lookupTable.getSyn0();
    INDArray vecA = syn0.divColumnVector(syn0.norm2(1));

    /* There are about 300,000 words, so we want to iterate 600 times
     * each iteration computes a batch of 500 words */
    int rangeStart = 0;
    int rangeEnd = 500;
    for (int i = 0; i < 600; i++) {
      int[] range = IntStream.range(rangeStart, rangeEnd).toArray();
      INDArray arrayIndex = Nd4j.create(range, new long[] {1, 500}, DataType.FLOAT);

      // Slice matrix into a batch of 500 x 300
      INDArray batchMatrix =
          vecA.get(NDArrayIndex.indices(arrayIndex.toLongVector()), NDArrayIndex.all());

      // Compute cosine similarity matrix (500 x 300) (300 x 300,000) = 500 x 300
      INDArray similarityMatrix = batchMatrix.mmul(vecA.transpose());

      // Truncate matrix to 500 x 50 and store in file
      storeInFile(similarityMatrix, indexToWord, rangeStart);

      rangeStart = rangeEnd;
      rangeEnd += 500;
    }
  }

  public static void storeInFile(INDArray similarityMatrix, String[] indexToWord, int rangeStart) {
    DB db = DBMaker.fileDB("/home/ngothomas/downloads/webapp/step197-2020/word2vec.db").make();

    BTreeMap<String, String[]> map =
        db.treeMap("Main")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.JAVA)
            .createOrOpen();

    // Loop through each row to sort nearest neighbor & truncate to 1 x 50
    for (int i = 0; i < 500; i++) {
      float[] row = similarityMatrix.getRow(i).toFloatVector();
      String word = indexToWord[rangeStart + i];
      String[] topWords = getTopFiftyNearestNeighbors(row, indexToWord);
      map.put(word, topWords);
    }

    db.close();
  }

  public static String[] getTopFiftyNearestNeighbors(float[] row, String[] indexToWord) {
    int[] indices = ArrayUtil.argsort(row);
    String[] topWords = new String[50];
    int pointer = 0;
    for (int k = indices.length - 1; k >= indices.length - 50; k--) {
      String word = indexToWord[indices[k]];
      topWords[pointer] = word;
      pointer++;
    }
    return topWords;
  }
}
