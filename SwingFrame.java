import org.eclipse.jetty.server.Server;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingFrame extends JFrame 
{

    public SwingFrame() 
    {
        this.setSize(1280, 1024);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(3);
        this.setTitle("Pi Swing Chess");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);       
        this.setVisible(true);

        ChessBoard board = new ChessBoard();
        this.add(board);
    }

    public static void main(String[] args) throws Exception
    {

        Server server = new Server(80);
        server.setHandler(new HTTPRequestHandler());
        server.start();
        System.out.println("Server is live on " + HTTPRequestHandler.getMyNetworkAdapter());

        SwingUtilities.invokeLater(new Runnable() 
            {
                public void run() 
                {
                    SwingFrame game = new SwingFrame();
                    game.setVisible(true);
                }
            });                
    } 
}