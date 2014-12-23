import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;


import org.json.JSONObject;

import com.mysql.jdbc.Statement;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


public class verify extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	JSONObject jobj=null;
    public verify() {
        super();
    }
 protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
    	
		PrintWriter p=rs.getWriter();
		BufferedReader br=new BufferedReader(new InputStreamReader(rq.getInputStream()));
			String st="";
			StringBuilder sb=new StringBuilder();
		
		  while((st=br.readLine())!=null)
			{
				sb.append(st);
				
			}
			//p.print(sb.toString());
			try {
				 jobj=new JSONObject(sb.toString());
				//p.print(jobj.getString("NAME")+" "+jobj.getString("PASSWORD"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			  
	            p=rs.getWriter();
				String userid=jobj.getString("VERUSERNAME");
				String city=jobj.getString("VERLOCATION");
				String request=jobj.getString("CREQUEST");
				String yorn=jobj.getString("YESORNO");
				String reply=jobj.getString("VREPLY");
		        Connection con;
				Statement st1;
				checkSign ck=new checkSign();
				if(ck.verify(reply)==true)
				{
				 try{
					 	p=rs.getWriter();
					 	Class.forName("com.mysql.jdbc.Driver");
						con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
						st1=(Statement)con.createStatement();
						PreparedStatement ps=con.prepareStatement("insert into verifier values(?,?,?,?,?)");
						
						ps.setString(1,userid);
						ps.setString(2,city);
						ps.setString(3,request);
						ps.setString(4,yorn);
						ps.setString(5,reply);
						int i=ps.executeUpdate();
						if(i>0)
						{
							System.out.print("verifier inserted!!");
						}
						else
						{
							System.out.print("verify insertion failed!!");
						}
		           }catch(Exception ex)
				 {
					 ex.printStackTrace();
					 
				 }
		
	
				}
				else
				{
					System.out.println("sorry digital signature does not seem to be of the claimed person!!");
					
				}
	}
 
    


    
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		doGet(rq, rs); 
			}  
    
} 
   