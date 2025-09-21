package hw1.webforum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/forum")
public class ForumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ArrayList<Entry> list = new ArrayList<>();
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			String username = request.getParameter("username");
			String message = request.getParameter("message");
			
			if(username != null && message !=null && !username.isEmpty() && !message.isEmpty()) {
				Entry newEntry = new Entry(username, message);
				list.add(newEntry);
			}
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			DisplayInfo.displayInfo(list,out);
			out.close();
		}
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.sendRedirect("index.html");
    }
}
