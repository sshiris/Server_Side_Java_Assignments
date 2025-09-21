package hw1.webforum;

import java.io.IOException;
import java.util.Vector;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/forum")
public class ForumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Vector<Entry> list = new Vector<>();
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			String username = request.getParameter("username");
			String message = request.getParameter("message");
			
			if(username != null && message !=null && !username.isEmpty() && !message.isEmpty()) {
				Entry newEntry = new Entry(username, message);
				list.add(newEntry);
			}
			DisplayInfo.displayInfo(list,response);
		}
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String search = request.getParameter("search");
		String s = search.toLowerCase();
		Vector<Entry> filtered = list;
		
		if(search!=null && !search.isEmpty()) {
			filtered = list.stream()
				.filter(e -> e.getUsername().toLowerCase().contains(s)
						|| e.getMessage().toLowerCase().contains(s))
				.collect(Collectors.toCollection(Vector::new));
				
		}
		
		DisplayInfo.displayInfo(filtered,response);
    }
}
