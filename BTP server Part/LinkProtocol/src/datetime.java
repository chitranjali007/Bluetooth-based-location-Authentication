import java.io.BufferedReader;
import java.io.IOException;


import java.sql.Date;

import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


public class datetime extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	JSONObject jobj=null;
    public datetime() {
        super();
    }
    
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		doGet(rq, rs); 
			}  
    
    
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
    
    	String dateStart = "01/14/2012 09:29:58";
		String dateStop = "01/15/2012 10:31:48";
 
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
 
		Date d1 = null;
		Date d2 = null;
 
		try {
			d1 = (Date) format.parse(dateStart);
			d2 = (Date) format.parse(dateStop);
 
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
 
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			System.out.print(" hellloooooooooooo.");
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
    }

	private int getInt(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
 }
    



	
