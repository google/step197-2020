import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploadForImageAnalysis")
public class UploadOnBlobstoreServlet extends HttpServlet {

  // Create an upload URL for the user to upload their images to BlobStore
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    // Redirects the user to /usercards servlet upon completion of image being processed in
    // Blobstore
    String uploadUrl = blobstoreService.createUploadUrl("/imageAnalysis");

    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }
}