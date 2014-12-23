
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;


public class finali extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	JSONObject jobj=null;
    public finali() {
        super();
    }
    
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
		doGet(rq, rs); 
			}  
    
    
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
    	int noverifiercount = 0, flag = 0,request = 0,yorn = 0,threshold=40,vtrueverify = 0,vfalseverify = 0,k=0,vtotalverify=0;
    	int count1=0,count2=0;
    	int[]arr1 = new int[20];
    	int[]arr2 = new int[20];
		int[]arr3 = new int[20];
	    int[]arr4 = new int[20];
	    String[]arr0=new String[20];
    	String vuserid = null;
    	int decision=0,cscore=0,vscore=0,ctotalclaims=0,cfalseclaims=0,i=0;float sum1 = 0.0f,sum2 = 0.0f;
    	rs.setContentType("text/html");
    	Connection cn = null;
    	 try{
			 	BufferedReader br=new BufferedReader(new InputStreamReader(rq.getInputStream()));
				String st="";
				StringBuilder sb=new StringBuilder();
			
			  while((st=br.readLine())!=null)
				{
					sb.append(st);
					
				}
			  String cuserid="123";
			  	Class.forName("com.mysql.jdbc.Driver");
				String query="SELECT * FROM btp_table where userid='"+cuserid+"'";
			    Statement st1 = (Statement) cn.createStatement();
				ResultSet rs1 = st1.executeQuery(query); 
				while(rs1.next())
				{
					 cscore=rs1.getInt(5);
					 ctotalclaims=rs1.getInt(6);
					 cfalseclaims=rs1.getInt(7);
					 noverifiercount=rs1.getInt(12);
				}
				
				String query1="Select * from claim c,verifier v where c.request=v.request";
				ResultSet rs2 = st1.executeQuery(query1); 
				if(rs2==null)
				{
				    noverifiercount++;
					//no verifier
					if(noverifiercount>=5)
               	    {
               		 decision=1;
               		 cscore=cscore-10;
			        }
					else{
					      cscore=cscore-5;
					    cfalseclaims++;
					    if(cfalseclaims>=.1*ctotalclaims)
						{
							decision=1;
						}
					    else{
					    	decision=0;
					    }
				    }
				}
				while(rs2.next())
				{
					request = rs2.getInt(6);
					decision=rs2.getInt(7);
				}
				
				
				String query2="Select userid from verifier v where v.request="+request;
				ResultSet rs3 = st1.executeQuery(query2);
				while(rs3.next())
				{
					
					vuserid=rs3.getString(2);
					arr0[i]=vuserid;
					yorn=rs3.getInt(5);
				}
				
				
				String query3="Select * from btp_table b where b.userid='"+vuserid+"'";
				ResultSet rs4 = st1.executeQuery(query3);
				while(rs4.next())
				{
					vscore=rs4.getInt(5);
					vtrueverify=rs4.getInt(11);
					vfalseverify=rs4.getInt(10);
					vtotalverify=rs4.getInt(9);
				}
				
				
				
				
						String yes_verify[]=new String[20];i=0;
						String no_verify[]=new String[20];int j=0;
ResultSet rss=st1.executeQuery("select userid,Score,yorn from btp_table b,verifier v where v.request='"+request+"' and v.userid=b.userid");
while(rss.next())
{  yorn=rss.getInt(3);


				if(yorn==1)
				{
				    sum1=sum1+rss.getFloat(2);
					yes_verify[i]=rss.getString(1);
					i++;
				}
				else
			    {
					sum2=sum2+rss.getFloat(1);
					no_verify[j]=rss.getString(1);
					j++;
					//arr2[i]=vscore;
			        //i++;
			    }
}
			
				if(sum1-sum2>=threshold)
				{
				if(sum1 > sum2)
				{
                      for( i=0;i<yes_verify.length;i++)
                      {
                    	       // arr1[i]++;
                    	  ResultSet rss1=st1.executeQuery(" select Score,True_verify from btp_table where userid="+yes_verify[i]);
                    	     while(rss1.next())
                    	     {
                    	    	 vscore=getInt(1)+1;
                    	    	 vtrueverify=getInt(2)+1;
                    	     }
                    	        
                    	        PreparedStatement ps1=cn.prepareStatement("update btp_table set Score='vscore',True_verify='vtrueverify'  where userid='"+yes_verify[i]+"'");
                    	        
                    	        
                      }
                      for(j=0;j<no_verify.length;j++)
                      {
                        ResultSet rss2=st1.executeQuery(" select Score,True_verify,False_verify from btp_table where userid="+no_verify[i]);
               	     while(rss2.next())
               	     {
               	    	 vscore=getInt(1)-1;
               	    	 vtrueverify=getInt(2)-1;
               	    	 vfalseverify=getInt(3)+1;
               	     }
               	  PreparedStatement ps1=cn.prepareStatement("update btp_table set Score='vscore',True_verify='vtrueverify',False_verify='vfalseverify'  where userid='"+no_verify[i]+"'");
                      }
                      
                      decision=0;//accept claim...
                     
				}
				else{
					for(j=0;j<no_verify.length;j++)
                    {
						arr2[i]++;
            			vtrueverify++;
            			ResultSet rss3=st1.executeQuery(" select Score,True_verify from btp_table where userid="+no_verify[i]);
                  	     while(rss3.next())
                  	     {
                  	    	 vscore=getInt(1)+1;
                  	    	 vtrueverify=getInt(2)+1;
                  	     }
                  	  PreparedStatement ps2=cn.prepareStatement("update btp_table set Score='vscore',True_verify='vtrueverify',False_verify='vfalseverify'  where userid='"+no_verify[i]+"'");
                  	 
                    }
						for(i=0;i<yes_verify.length;i++)
                        {
							arr1[j]=arr1[j]-5;
	                        vtrueverify--;
	                        vfalseverify++;
	                        ResultSet rss4=st1.executeQuery(" select Score,True_verify,False_verify from btp_table where userid="+yes_verify[i]);
	                  	     while(rss4.next())
	                  	     {
	                  	    	 vscore=getInt(1)-1;
	                  	    	 vtrueverify=getInt(2)-1;
	                  	    	 vfalseverify=getInt(3)+1;
	                  	     }
	                  	  PreparedStatement ps3=cn.prepareStatement("update btp_table set Score='vscore',True_verify='vtrueverify',False_verify='vfalseverify'  where userid='"+yes_verify[i]+"'");
	                  	  
                        }
                    decision=1;//reject claim...
					
				}
				}
				else
				{
				
					if(cfalseclaims>10)
					{
						decision=1;//reject claim...
					}
				 
					else
					{ 
						count1=0;count2=0;
						if(sum1>sum2)
						{ 
							for(j=0;j<no_verify.length;j++)
                            {
								ResultSet rss5=st1.executeQuery("select score,decision from btp_table where userid="+no_verify[i]);
								while(rss5.next())
								{
									vscore=getInt(1);
									decision=getInt(2);
								}
								
								
								if(vscore<threshold)
								 { 
									 vscore--;
									 decision=0;//accept claim...
									 count1++;
									 arr3[k]=decision;
									 k++;
						
								}
								 else
								 {
									  decision=1;
									  count2++;
									  arr4[k]=decision;
									  k++;
								 }
                            }
							if(count1>count2){
							    decision=0;
							    PreparedStatement ps4=cn.prepareStatement("update btp_table set decision='decision'  where userid='"+cuserid+"'");
							    PreparedStatement ps5=cn.prepareStatement("update btp_table set Score='vscore'  where userid='"+no_verify[i]+"'");
							}
							
							else
							{
								decision=1;
							 PreparedStatement ps6=cn.prepareStatement("update btp_table set decision='decision'  where userid='"+cuserid+"'");
						}
						}
							else
							{
								for(j=0;j<=yes_verify.length;j++)
	                            {
									ResultSet rss6=st1.executeQuery("select score,decision from btp_table where userid="+yes_verify[i]);
									while(rss6.next())
									{
										vscore=getInt(1);
										decision=getInt(2);
									}
									
									 if(vscore<threshold){
										
										 vscore--;
										 decision=0;
										 count1++;
										 arr4[k]=decision;
										 k++;
									 }
									 else
									 {
										decision=1;
										count2++;
										 arr4[k]=decision;
										 k++;
	                            }
							}
								if(count1>count2){
								    decision=0;
								    PreparedStatement ps7=cn.prepareStatement("update btp_table set decision='decision'  where userid='"+cuserid+"'");
								    PreparedStatement ps8=cn.prepareStatement("update btp_table set Score='vscore'  where userid='"+yes_verify[i]+"'");
								}
								else
									decision=1;
								PreparedStatement ps9=cn.prepareStatement("update btp_table set decision='decision'  where userid='"+cuserid+"'");
						}
					
					}
				}
    }
	catch(Exception e) {
	    			 e.getMessage();
	    	}
    }

	private int getInt(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
 }
    



	
