# Specific details on ProcessNearestNeighbors.java file

This function of ProcessNearestNeighbors.java is to only pre-process
the nearest neigbors of a given Word2Vec model. In this application,
a Word2Vec model of 300,000 words was used. The file will create/update
a binary .db file. Note that time results and optimal batch size are based
on running this file on a Google Pixelbook.Time result may change for
different hardwares.

## Processing Time

On a Google Pixelbook, a batch size of 500 words results in the fastest runtime
for computing the nearest neighbors, while avoiding OOMs. We may see some speedup by
migrating from N4dj matrix operations to a numerical computing library like
numpy. Example: Each batch of 500 words takes about 60,000 - 80,000 ms.
Processing 100 batches can take from 1 hour 30 minutes - 2 hours. In our case,
processing 300,000 words took 600 batches in total.

## Underlying reasons for slow performances

1. N4dj's behind the hood operations. For example, to slice a large matrix by a range of indices,
N4dj has to make a copy which is really slow, especially with a big matrix.
[Source](https://stackoverflow.com/questions/58681345/how-to-select-a-given-set-of-indexes-from-an-ndarray-in-nd4j-similarly-to-numpy)
2. Each computation of a batch results in sorting the
cosine similarity values in each row of size 300,000.
3. There is some time factor with serializing the result into the .db file.
Note: We use MapDB API to serialize a HashMap Object, which utilizes Random Access Files (RAF).