package hw1.webforum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import jakarta.servlet.http.HttpServletResponse;

public class DisplayInfo {
	public static void displayInfo(Vector<Entry> list, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<html><head><title>Forum</title></head><body>");
        out.println("<h2>Forum</h2>");
        out.println("<a href='index.html'>Back</a><hr>");
        
		if(list.isEmpty()) {
			out.println("<p>No messages.</p>");
		}else {
			for(Entry e : list) {
				out.println("<div style='border:1px solid #ccc; padding:8px; margin-bottom:10px;'>");
				out.println("<p>" +"UserName: "+ e.getUsername() + "</p>");
				out.println("<p>" +"Message: "+ e.getMessage() + "</p>");
				out.println("<p>" +"Date: "+ e.getDate() + "</p>");
				out.println("</div>");
			}
		}
		out.println("</body></html>");
		out.close();
	}

}
