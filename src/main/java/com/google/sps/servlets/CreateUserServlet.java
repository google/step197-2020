import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/createuser")
public class CreateUserServlet extends HttpServlet {
  
  //If User does not exist in database, we create a new User entity. Page redirected to the /MyFolder Url
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }





}