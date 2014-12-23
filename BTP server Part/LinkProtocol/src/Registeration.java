

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class Registeration
 */
public class Registeration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	JSONObject jobj=null;
    public Registeration() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(rq, rs); 
			
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			
			  
		// p=rs.getWriter();
			//p.print("hello");
				String name=jobj.getString("NAME");
				p.print(name);
				String password=jobj.getString("PASSWORD");
				String userid=jobj.getString("USERID");
				String location=jobj.getString("CITY");
				String email=jobj.getString("EMAIL");
				byte[] pubkey=jobj.getString("PUBKEY").getBytes();
				SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
				//Timestamp t=format.
			/*String name="NAME";
			String password="PASSWORD";
			String userid="USERmm";
			String location="CITY";
			String email="EMAIL";
			byte[] pubkey=email.getBytes();*/
				float score=30.0f;
				int claim=0;
				int fclaim=0;
				int tclaim=0;
				int verify=0;
				int fverify=0;
				int tverify=0;
				Connection con;
				Statement st1;
				 try{
					 	p=rs.getWriter();
					 	PreparedStatement ps=null;
					 	Class.forName("com.mysql.jdbc.Driver");
						con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
						st1=(Statement)con.createStatement();
						ps=con.prepareStatement("select * from btp_table where UserID='"+userid+"'");
						ResultSet rs1=ps.executeQuery();
						if(rs1.next())
						{
							p.print("USERNAME ALREADY EXISTS!!");
							
						}
						else
						{
						ps=con.prepareStatement("insert into btp_table values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						ps.setString(1,name);
						ps.setString(2,userid);
						ps.setString(3,password);
						ps.setString(4,location);
						ps.setFloat(5,score);
						ps.setInt(6,claim);
						ps.setInt(7,fclaim);
						ps.setInt(8,tclaim);
						ps.setInt(9,verify);
						ps.setInt(10,fverify);
						ps.setInt(11,tverify);
						ps.setBytes(12,pubkey);
						ps.setInt(13,0);
						ps.setTimestamp(14,new Timestamp(System.currentTimeMillis()));
						int i=ps.executeUpdate();
						if(i>0)
							p.write("Register Successfully!!");
						else
							p.write("Registeration Failed!!");
						
				 }
				 }
				 catch(Exception ex)
				 {
					 ex.printStackTrace();
					 
				 }
		
	
	
	}

	

}
