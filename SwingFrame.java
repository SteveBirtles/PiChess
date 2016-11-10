import org.eclipse.jetty.server.Server;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingFrame extends JFrame 
{

    public static String opponent = null;

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

        try
        {
            Server server = new Server(8080);
            server.setHandler(new HTTPRequestHandler(board));
            server.start();
            System.out.println("Server is live on " + HTTPRequestHandler.getMyNetworkAdapter());
        }
        catch (Exception ex)
        {
            System.out.println("Sever creation failed: " + ex.getMessage());
        }

    }

    public static void main(String[] args)
    {

        if (args.length > 0) opponent= args[0];

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