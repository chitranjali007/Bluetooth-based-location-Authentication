

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Hello
 */
public class Hello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Hello() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
          doGet(request, response);
         
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter p=response.getWriter();
		p.print("hellooooooooooooo");
		 String ip;
	  	    try {
	  	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	  	        while (interfaces.hasMoreElements()) {
	  	            NetworkInterface iface = interfaces.nextElement();
	  	            // filters out 127.0.0.1 and inactive interfaces
	  	            if (iface.isLoopback() || !iface.isUp())
	  	                continue;

	  	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	  	          InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                p.print(ip);
	  	          /*  while(addresses.hasMoreElements()) {
	  	                InetAddress addr = addresses.nextElement();
	  	                ip = addr.getHostAddress();
	  	                p.print(iface.getDisplayName() + " " + ip);
	  	            }*/
	  	        }
	  	    } catch (SocketException e) {
	  	        throw new RuntimeException(e);
	  	    }
		
	}

	
}
