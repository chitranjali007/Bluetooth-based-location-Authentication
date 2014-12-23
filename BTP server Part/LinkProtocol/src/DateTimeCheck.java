import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Statement;


public class DateTimeCheck {

	public boolean checkDateTime(Timestamp curTime,Timestamp pasTime,String userId,String curloc)
	{
		boolean res=false;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		 
		Timestamp d1 = null;
		Timestamp d2 = null;
        String pastlocation=null;
		try {
			d1 = pasTime;
			d2 = curTime;
 
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
			long cactualdiff=diffDays*24*3600+diffHours*3600+diffMinutes*60+diffSeconds;
			cactualdiff=(cactualdiff/60);
			System.out.println("difference: "+cactualdiff);
			
			Connection con;
			Statement st1;
			PreparedStatement ps=null;
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
			st1=(Statement)con.createStatement();
			
			
			ps=con.prepareStatement("select Location from btp_table where UserID='"+userId+"'");
			ResultSet rs1=ps.executeQuery();
			rs1.next();
			pastlocation=rs1.getString(1);
			System.out.println("pastlocation: "+pastlocation);
			
			ps=con.prepareStatement("SELECT Time FROM location WHERE Source='"+pastlocation+"' and Destination ='"+curloc+"'");
			ResultSet rs2=ps.executeQuery();
			rs2.next();
			int time=rs2.getInt(1);
			
			if(cactualdiff>=time)
				res= true;
	
		} catch (Exception e) {
			e.printStackTrace();
		}
 	
		return res;
		
	}
	
	
}
