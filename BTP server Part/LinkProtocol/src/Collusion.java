import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.Statement;


public class Collusion {
	public boolean checkCollusion(String cRequest,String claimer)
	{
		  Connection con=null;
		  Statement st1=null;
		  PreparedStatement psperm=null,ps=null;
		  int result=0;
		  boolean ShouldAllow=false;
		  ResultSet verifySet=null;
		  try{
			  Class.forName("com.mysql.jdbc.Driver");
			  con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
			  st1=(Statement)con.createStatement();
			  System.out.println(claimer);
			  psperm=con.prepareStatement("select b.UserID,Score,Nof_verify,False_verify,True_verify,yorn from btp_table b,verifier v where b.UserID=v.userID and v.request=?");
				psperm.setString(1,cRequest);
				 verifySet=psperm.executeQuery();
			while(verifySet.next())
			 {   System.out.println(verifySet.getString(1));
				 ps=con.prepareStatement("select * from collusion where claimerID=? and verifierID=?");
				 ps.setString(1,claimer);
				 ps.setString(2,verifySet.getString(1));
				 ResultSet result1=ps.executeQuery();
				 if(!result1.next())
					  {
					     System.out.println("null");
						 ps=con.prepareStatement("insert into collusion values(?,?,1)");
						 ps.setString(1,claimer);
						  ps.setString(2, verifySet.getString(1));
						  result=ps.executeUpdate();
					      if(result==1)
					    	  System.out.println("inserted");
					  }
				 else
			  {
				  ps=con.prepareStatement("update collusion set TimesVerify=TimesVerify+1 where claimerID='"+claimer+"' and verifierID='"+verifySet.getString(1)+"'");
			      ps.executeUpdate();
			  }
			 }
			
			 verifySet=psperm.executeQuery();
			while(verifySet.next())
			{
				ps=con.prepareStatement("select TimesVerify from collusion where claimerID='"+claimer+"' and verifierID='"+verifySet.getString(1)+"'");
				ResultSet rs2=ps.executeQuery();
				rs2.next();
				
					int TimesVerify=rs2.getInt(1);
					if(TimesVerify<=2)
					{
						ShouldAllow=true;
					}
					else
					{
						ps=con.prepareStatement("update verifier set request='not allowed' where request='"+cRequest+"' and UserID='"+verifySet.getString(1)+"'");
						int i=ps.executeUpdate();
					}
					
				
			}
			 
		  	}
		  catch(Exception ex)
		  {
			  ex.printStackTrace();
		  }
		return ShouldAllow;
	}

}
