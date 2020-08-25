import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploadForImageAnalysis")
public class UploadOnBlobstoreServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    String uploadUrl = blobstoreService.createUploadUrl("/imageAnalysis");
    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }
}