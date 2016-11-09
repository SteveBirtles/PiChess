import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HTTPRequestHandler extends AbstractHandler {

    private StringBuilder history;
    private ChessBoard board;
    
    public HTTPRequestHandler(ChessBoard board)
    {
        history = new StringBuilder();
        this.board = board;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {                 
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if (request.getRequestURI().equals("/favicon.ico")) return; // SKIP FAVICON REQUESTS;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateobj = new Date();
        System.out.println();

        String requestText = "[ " + request.getRemoteAddr()  + "  |  " + df.format(dateobj) + "  |  ";
        requestText += request.getMethod() + " ] \t " + request.getRequestURI() + " \t ";

        if (request.getQueryString() != null)
        {
            for(String q : request.getQueryString().split("&"))
            {
                if (q.contains("=")) 
                {
                    String variable = q.split("=")[0];
                    String value = q.split("=")[1];
                    requestText += "   " + variable + " = " + value;                                         
                }
                else               
                {
                    requestText += "   Invalid query string component (" + q + ")";
                }
            }
        }
        else
        {
            requestText += "   No query string supplied";
        }
        System.out.println(requestText);
        history.append("<p>" + requestText + "</p>");

        StringBuilder responseText = new StringBuilder();
        responseText.append("<!DOCTYPE html>\n");
        responseText.append("<html>\n");
        responseText.append("<body>\n");
        responseText.append("<h1>Welcome to my tiny Java web server!</h1>\n");
        responseText.append("<hr /><h3>Messages recived so far:</h3>\n");
        responseText.append(history);           
        responseText.append("</body>\n");
        responseText.append("</html>\n");        
        response.getWriter().println(responseText);

        baseRequest.setHandled(true);
    }

    public static String getMyNetworkAdapter() throws SocketException
    {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) 
        {
            NetworkInterface iface = interfaces.nextElement();

            if (iface.isLoopback() || !iface.isUp()) continue; // filters out 127.0.0.1 and inactive interfaces

            Enumeration<InetAddress> addresses = iface.getInetAddresses();            

            if (iface.getDisplayName().startsWith("wlan0")) continue;

            while(addresses.hasMoreElements())             
            {                
                InetAddress addr = addresses.nextElement();
                if (!(addr instanceof Inet4Address)) continue;
                return (addr.getHostAddress());
            }
        }
        return null;
    }

    /*public static void main(String[] args) throws Exception {
        Server server = new Server(80);
        server.setHandler(new HTTPRequestHandler());
        server.start();
        System.out.println("Server is live on " + getMyNetworkAdapter());
        System.out.println("Close with CTRL+SHIFT+R in BlueJ main window or CTRL+C on command line.");
        System.out.println("-----------------------------------------------------------------------");      
        server.join();
    }*/
}
