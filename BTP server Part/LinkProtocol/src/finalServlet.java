

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class finalServlet
 */
public class finalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public finalServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.print("final!!!!");
		  Connection con=null;
		  Collusion coll=new Collusion();
			Statement st1=null;
			PrintWriter p=rs.getWriter();
			JSONObject jobj=null;
			ResultSet verifySet1=null;
			String cUserId,cRequest;
			int noverifiercount = 0, flag = 0,request = 0,yorn = 0,vtrueverify = 0,vfalseverify = 0,k=0,vtotalverify=0;
			int decision=0;
			float yscore=0.0f;
			float threshold=15.0f;
			float nscore=0.0f;
			PreparedStatement ps=null,psperm;
			BufferedReader br=new BufferedReader(new InputStreamReader(rq.getInputStream()));
				String st="";
				StringBuilder sb=new StringBuilder();
				while((st=br.readLine())!=null)
				{
					sb.append(st);
				}
			//	System.out.print(sb.toString());
				//p.print(sb.toString());
				try {
					jobj=new JSONObject(sb.toString());
					cUserId=jobj.getString("CUSERID");
					cRequest=jobj.getString("SIGNREQ"); 
					 //p.print(jobj.getString("NAME")+" "+jobj.getString("PASSWORD"));
				//	p=rs.getWriter();
				 	Class.forName("com.mysql.jdbc.Driver");
					con=DriverManager.getConnection("jdbc:mysql://localhost:3306/btp","root","");
					st1=(Statement)con.createStatement();
					/*spation temp for claimer*/
					Timestamp curt=new Timestamp(System.currentTimeMillis());
					System.out.println("current time: "+curt);
					PreparedStatement ps1=con.prepareStatement("select Time from btp_table where UserID='"+cUserId+"'");
					
					ResultSet rs3=ps1.executeQuery();
					rs3.next();
				    Timestamp pastime=rs3.getTimestamp(1);
				    System.out.println("pastime: "+pastime);
					ps1=con.prepareStatement("select location from claim where userid='"+cUserId+"' and request='"+cRequest+"'");
					rs3=ps1.executeQuery();
					rs3.next();
					String Loc=rs3.getString(1);
					   System.out.println("current location: "+Loc);
					
					DateTimeCheck dtc=new DateTimeCheck();
					if(!dtc.checkDateTime(curt, pastime, cUserId, Loc))
						
						{
						
							decision=0;//reject the claim...
							System.out.println("spatiotemporalcorelatn failed!!");
						
						}
					
					
					else
					{
						System.out.println("spatiotemporalcorelatn success!!");
					psperm=con.prepareStatement("select b.UserID,Score,Nof_verify,False_verify,True_verify,yorn from btp_table b,verifier v where b.UserID=v.userID and v.request=?");
					psperm.setString(1,cRequest);
					ResultSet verifySet=psperm.executeQuery();
					/*no verifier case...*/
				//System.out.println(verifySet);
					
					while(verifySet.next())
					{
						System.out.println(verifySet.getString(1));
					}
					
					//System.out.println("nahoooooooooooooo");
				//System.out.print(coll.checkCollusion(verifySet, cUserId));
					/**/
					 ps=con.prepareStatement("select NoVerifierCount from btp_table where UserID='"+cUserId+"'");
					 rs3=ps.executeQuery();
					 rs3.next();
					 noverifiercount=rs3.getInt(1);
					 
					 verifySet=psperm.executeQuery();
				if(!verifySet.next())
					{
					    noverifiercount++;
						/*if noverifiers count for a claimer is greater than 5 than reject the claim*/
						if(noverifiercount>=5)
	               	    {
	               		 decision=0;//reject claim
	               		// decrease score of claimer...
				       // ps=con.prepareStatement("update btp_table set Score=Score-10 where UserID='"+cUserId+"'");
				        //int i=ps.executeUpdate();
	               		System.out.println("no verifier count is greater than 5");
	               	    }
						else{
							System.out.println("no verifier count is greater than 5 but decrease its score by 5");
						      //cscore=cscore-5;
						    //else accept d claim but decrease the score of claimer by 5
							 ps=con.prepareStatement("update btp_table set Score=Score-5 where UserID='"+cUserId+"'");
							 //increase the noverifier count by 1 and check whether its trend is good or not 
							 int i=ps.executeUpdate();
							 ps=con.prepareStatement("update btp_table set NoVerifierCount=NoVerifierCount+1 where UserID='"+cUserId+"'");
							 i=ps.executeUpdate();
							 ps=con.prepareStatement("select NoVerifierCount,Nof_claimes from btp_table where UserID='"+cUserId+"'");
							// i=ps.executeQuery();
							 ResultSet rs1=ps.executeQuery();
						    
							 while(rs1.next())
						    {
							if(rs1.getInt(1)>=.1*rs1.getInt(2))
							{
								decision=0;//reject claim
								System.out.print("noverifier count is less than 5 but greater than 0.1 of totalclaimes");
							}
						    else{
						    	decision=1;//accept claim
						    }
						    }
							 
					    }
					}
				
					else
					{
						//verifyset2=verifySet;
						verifySet=psperm.executeQuery();
						
						if(coll.checkCollusion(cRequest,cUserId)==false)
							{
								decision=0;//reject claim...
								System.out.println("all verifiers have verified him more dan twice!!");
							}
						else
						{
						verifySet=psperm.executeQuery();
						while(verifySet.next())
						{
							yorn=verifySet.getInt(6);


							if(yorn==1)
							{
							    yscore+=verifySet.getFloat(2);
							    System.out.println(yscore);
							}
							else
						    {
								nscore+=verifySet.getFloat(2);
						    }
							
						}
						int i=0;
						if(yscore-nscore>=threshold)
						{
						if(yscore > nscore)
						{   
							System.out.println("yscore>nscore!!! accpt claim");
							System.out.println(yscore);
							verifySet=psperm.executeQuery();
							while(verifySet.next())
		                      {
		                    	if(verifySet.getInt(6)==1)
		                    	{
		                    		System.out.println("incrementing the verifiers score by 5 who hv said yes");	
								ps=con.prepareStatement("update btp_table set Score=Score+5,True_verify=True_verify+1,Nof_verify=Nof_verify+1,Location=?,Time=?  where UserID='"+verifySet.getString(1)+"'");
		                    	ps.setString(1, Loc);
		                    	ps.setTimestamp(2, curt);
								i=ps.executeUpdate();
		                    	}
		                    	else
		                    	{
		                    		System.out.println("decrementing the verifiers score by 5 who hv said no");
		                    	 ps=con.prepareStatement("update btp_table set Score=Score-5,False_verify=False_verify+1,Nof_verify=Nof_verify+1  where UserID='"+verifySet.getString(1)+"'");       
		                         i=ps.executeUpdate();   
		                    	}
		                         decision=1;//accept claim...
		                      }
		                    
						}
						else
						{
							System.out.println("noscore>yesscore!!!reject claim");
							verifySet=psperm.executeQuery();
							while(verifySet.next())
		                      {
		                    	if(verifySet.getInt(6)==0)
		                    	{
		                    		System.out.println("incrementing the verifiers score by 5 who hv said no");
								ps=con.prepareStatement("update btp_table set Score=Score+5,True_verify=True_verify+1,Nof_verify=Nof_verify+1 where UserID='"+verifySet.getString(1)+"'");
								
								i=ps.executeUpdate();
		                    	}
		                    	else
		                    	{
		                    		System.out.println("decrementing the verifiers score by 5 who hv said yes");
		                    	 ps=con.prepareStatement("update btp_table set Score=Score-5,False_verify=False_verify+1,Nof_verify=Nof_verify+1  where UserID='"+verifySet.getString(1)+"'");       
		                         i=ps.executeUpdate();   
		                    	}
		                         decision=0;//reject claim...
		                      }
						}
					}
						else
						{
							System.out.println("yscore-nscore<threshhold");
							ps=con.prepareStatement("select Nof_claimes,False_claims from btp_table where UserID='"+cUserId+"'");
							ResultSet rs1=ps.executeQuery();
							int totalClaims=0;
							int falseClaims=0;
							int strong=0,weak=0;
							rs1.next();
							
								totalClaims=rs1.getInt(1);
								falseClaims=rs1.getInt(2);
							
							if((falseClaims/totalClaims)<=0.35)
							{   
								System.out.println("falseclaim/totalclaim<=0.35");
								verifySet=psperm.executeQuery();
								while(verifySet.next())
								{   if(verifySet.getInt(6)==0)
									{
										if(verifySet.getFloat(2)>=30.0f)
											strong++;
								
										else
											weak++;
									}
									
								}
								if(strong>weak)
									{
									System.out.println("the verifier saying \"no\" are strong so reject claim");
										decision=0;
									
									}
								else
								{
									System.out.println("the verifier saying \"no\" are weak so accept claim");
									decision=1;
								}
								
							}
							else
							{
								System.out.println("falseclaim/totalclaim>0.35 so reject claim");
								decision=0;//reject claim...
							}
								
							
						}
						
				}
					}
	           
					}
					if(decision==1)
					{
								try
									{
										ps=con.prepareStatement("update btp_table set Score=Score+10,Nof_claimes=Nof_claimes+1,True_claims=True_claims+1,Location=?,Time=? where UserID='"+cUserId+"'");
										ps.setString(1,Loc);
										ps.setTimestamp(2,curt);
										int i=ps.executeUpdate();
										System.out.println("claime accepted");
									}
								catch(Exception ex)
									{
										ex.printStackTrace();
									}
						
					}
					else
					{
						try
						{
							ps=con.prepareStatement("update btp_table set Score=Score-10,Nof_claimes=Nof_claimes+1,False_claims=False_claims+1 where UserID='"+cUserId+"'");
						
							int i=ps.executeUpdate();
							System.out.println("claime rejected");
						}
					catch(Exception ex)
						{
							ex.printStackTrace();
						}
			
						
					}
				}catch(Exception ex)
			 {
				 ex.printStackTrace();
				 
			 }
				
				System.out.print("blah blah blah!!!!!!!!!");
				p.print(decision);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
