import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;


public class claim extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	JSONObject jobj=null;
    public claim() {
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
			try {
				 jobj=new JSONObject(sb.toString());
				//p.print(jobj.getString("NAME")+" "+jobj.getString("PASSWORD"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//System.out.print(sb.toString());
				e.printStackTrace();
				
			}
			//p.print(sb.toString());
			/*try {
				 jobj=new JSONObject(sb.toString());
				//p.print(jobj.getString("NAME")+" "+jobj.getString("PASSWORD"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			  
	            p=rs.getWriter();
				String userid=jobj.getString("CUSERNAME");
				//p.print(userid);
				String serviceid=jobj.getString("SERVICE_ID");
				String city=jobj.getString("LOCATION");
				int sequenceno=jobj.getInt("SEQNO");
				String request=jobj.getString("SIGNED_REQUEST");
				
		/*verifying dig.sign on claimer*/
				checkSign ck=new checkSign();
				if(ck.verify(request)==true)
				{
				Connection con;
				Statement st1;
				 try{
					 	p=rs.getWriter();
					 	Class.forName("com.mysql.jdbc.Driver");
						con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
						st1=(Statement)con.createStatement();
						ResultSet rs1=st1.executeQuery("select MAX(serialNo) from claim where userid='"+userid+"'");
						int sn=0;
						while(rs1.next())
						{
							sn=rs1.getInt(1)+1;
						}
						//int rs2=st1.executeUpdate("update claim set serialNo="+sn+" where userid="+userid);
						PreparedStatement ps=con.prepareStatement("insert into claim values(?,?,?,?,?,?)");
						ps.setInt(1,sn);
						ps.setString(2,userid);
						ps.setString(3,serviceid);
						ps.setString(4,city);
						ps.setInt(5,sequenceno);
						ps.setString(6,request);
						int i=ps.executeUpdate();
						if(i>0)
						{
							System.out.print("claimer inserted!!");
						}
						else
						{
							System.out.print("claimer insertion failed!!");
						}
		           }catch(Exception ex)
				 {
					 ex.printStackTrace();
					 
				 }
			}
				else
					System.out.print("sorry digital signature does not seem to be of the claimed person!!");
}
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		doGet(rq, rs); 
			}  
    
    
   
}
