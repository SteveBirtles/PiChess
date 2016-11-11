import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import java.net.HttpURLConnection;
import java.net.URL;

public class ChessBoard extends JPanel implements ActionListener {

    private Timer timer;

    private BufferedImage[] sprite;
    private int[][] square;
    private int[][] moves;
    private boolean[][] unmoved;
    private int cursorX = 0, cursorY = 0;
    private int selectedX = -1, selectedY;
    private double mouseX, mouseY;
    private boolean showcoords;

    private String columns[] = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};  
    private String rows[] = new String[] {"8", "7", "6", "5", "4", "3", "2", "1"};

    public void setSquare(String position, int value, boolean isunmoved)
    {
        position = position.toUpperCase();

        if (position.length() != 2) return;        

        int x = position.charAt(0) - 65;
        int y = 7 - (position.charAt(1) - 49);

        if (x < 0 || x > 7) return;
        if (y < 0 || y > 7) return;

        square[y][x] = value;
        unmoved[y][x] = isunmoved;

    }

    public void doMove(String start, String end)
    {

        start = start.toUpperCase();
        end = end.toUpperCase();

        if (start.length() != 2 || end.length() != 2) return;

        int x1 = start.charAt(0) - 65;
        int y1 = 7 - (start.charAt(1) - 49);
        int x2 = end.charAt(0) - 65;
        int y2 = 7 - (end.charAt(1) - 49);

        System.out.println("REQUEST TO MOVE (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ")"); 

        if (x1 < 0 || x1 > 7) return;
        if (y1 < 0 || y1 > 7) return;
        if (x2 < 0 || x2 > 7) return;
        if (y2 < 0 || y2 > 7) return;

        if (square[y1][x1] == 0) return;

        square[y2][x2] = square[y1][x1];
        square[y1][x1] = 0;
        unmoved[y1][x1] = false;

    }

    public ChessBoard() 
    {
        sprite = new BufferedImage[13];

        try
        {
            sprite[0] =   ImageIO.read(new File("selected.png"));
            sprite[1] =   ImageIO.read(new File("white_pawn.png"));
            sprite[2] =   ImageIO.read(new File("white_rook.png"));
            sprite[3] =   ImageIO.read(new File("white_knight.png"));
            sprite[4] =   ImageIO.read(new File("white_bishop.png"));
            sprite[5] =   ImageIO.read(new File("white_queen.png"));
            sprite[6] =   ImageIO.read(new File("white_king.png"));
            sprite[7] =   ImageIO.read(new File("black_pawn.png"));
            sprite[8] =   ImageIO.read(new File("black_rook.png"));
            sprite[9] =   ImageIO.read(new File("black_knight.png"));
            sprite[10] =  ImageIO.read(new File("black_bishop.png"));
            sprite[11] =  ImageIO.read(new File("black_queen.png"));
            sprite[12] =  ImageIO.read(new File("black_king.png"));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        addKeyListener(new KeyboardyMcKeyboardFace());  
        addMouseListener(new MouseyMcMouseFace());
        setFocusable(true);

        setBackground(new Color(0,0,64)); 

        resetBoard();

        timer = new Timer(16, this);
        timer.start();                 

    }

    private void resetBoard()
    {
        square = new int[8][];
        square[0] = new int[]{8, 9, 10, 11, 12, 10, 9, 8};
        square[1] = new int[]{7, 7, 7, 7, 7, 7, 7, 7};
        square[2] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        square[3] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        square[4] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        square[5] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        square[6] = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        square[7] = new int[]{2, 3, 4, 5, 6, 4, 3, 2};

        moves = new int[8][8];
        unmoved = new boolean[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (square[y][x] > 0) unmoved[y][x] = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        mouseX = MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX();
        mouseY = MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY();        

        if (mouseX > 320 && mouseX < 960 && mouseY > 64 && mouseY < 704) {
            cursorX = (int) (mouseX - 320) / 80;
            cursorY = (int) (mouseY - 64) / 80;
        }        

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                moves[y][x] = -1;
            }
        }

        if (selectedX > -1)
        {
            MoveChecker m = new MoveChecker();
            ArrayList<Node> allowedMoves = m.getMoves(square[selectedY][selectedX]);

            for (Node n : allowedMoves) {
                boolean last = false, jumpking = false;
                int[] c = n.getValue();
                int x = selectedX + c[1];
                int y = selectedY + c[0];

                if (x >= 0 && y >= 0 && x < 8 && y < 8) {
                    moves[y][x] = c[2];
                    if (square[y][x] == 0 && moves[y][x] == 1) moves[y][x] = -1;
                    if (!unmoved[selectedY][selectedX] && (moves[y][x] == 2 || moves[y][x] == 4)) moves[y][x] = -1;
                    if (square[y][x] > 0) {
                        if ( moves[y][x] == 2 || moves[y][x] == 3 ) moves[y][x] = -1;
                        if ( square[y][x] < 7 && square[selectedY][selectedX] < 7 ) moves[y][x] = -1;       
                        if ( square[y][x] >= 7 && square[selectedY][selectedX] >= 7 ) moves[y][x] = -1; 
                        if (( square[selectedY][selectedX] == 2 && square[y][x] == 6 ) || 
                        ( square[selectedY][selectedX] == 8 && square[y][x] == 12 )) { jumpking = true; }
                        else { last = true; }
                    }
                }

                if (last) continue;

                if (n.getEdges().size() > 0) {
                    Node next = n.getEdges().get(0);
                    while (true) {
                        c = next.getValue();            
                        int xx = selectedX + c[1];
                        int yy = selectedY + c[0];
                        if (xx >= 0 && yy >= 0 && xx < 8 && yy < 8) {
                            moves[yy][xx] = c[2];    
                            if (square[yy][xx] == 0 && moves[yy][xx] == 1 ) moves[yy][xx] = -1;
                            if (!unmoved[selectedY][selectedX] && (moves[yy][xx] == 2 || moves[yy][xx] == 4 || jumpking)) moves[yy][xx] = -1;
                            if (square[yy][xx] > 0) {
                                if (moves[yy][xx] == 2 || moves[yy][xx] == 3 ) moves[yy][xx] = -1;
                                if (square[yy][xx] < 7 && square[selectedY][selectedX] < 7 ) moves[yy][xx] = -1;                             
                                if (square[yy][xx] >= 7 && square[selectedY][selectedX] >= 7 ) moves[yy][xx] = -1; 
                                if (( square[selectedY][selectedX] == 2 && square[yy][xx] == 6 ) || 
                                ( square[selectedY][selectedX] == 8 && square[yy][xx] == 12 )) {
                                    if (next.getEdges().size() == 0) break; 
                                    next = next.getEdges().get(0);   
                                    c = next.getValue();            
                                    xx = selectedX + c[1];
                                    yy = selectedY + c[0];
                                    if (xx >= 0 && yy >= 0 && xx < 8 && yy < 8 && square[yy][xx] == 0) moves[yy][xx] = c[2];
                                }                                
                                last = true;
                            }                            
                        }
                        if (last || jumpking || next.getEdges().size() == 0) break; 
                        next = next.getEdges().get(0);                       
                    }                    
                }
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) { 

        MoveChecker m = new MoveChecker();

        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if ((x + y) % 2 == 0)
                {
                    if (x == cursorX && y == cursorY)
                        g.setPaint(new Color(255,255,64));                
                    else
                        g.setPaint(new Color(255,255,255));                
                }
                else
                {
                    if (x == cursorX && y == cursorY)
                        g.setPaint(new Color(192,192,0));                
                    else
                        g.setPaint(new Color(128,128,128));                
                }

                g.fillRect (320 + x * 80, 64 + y * 80, 80, 80);              

                if (showcoords)
                {
                    g.setPaint(new Color(192,192,192));
                    g.drawString(columns[x] + rows[y], 322 + x * 80, 142 + y * 80);
                }

                if (square[y][x] > 0) g.drawImage (sprite[square[y][x]], 320 + x * 80, 64 + y * 80, this);

                if (x == selectedX && y == selectedY)
                {
                    g.drawImage (sprite[0], 320 + x * 80, 64 + y * 80, this);
                }

                if (moves[y][x] >= 0)
                {

                    /*switch (moves[y][x])
                    {
                    case 0: g.setPaint(new Color(0,255,0)); break;
                    case 1: g.setPaint(new Color(255,0,0)); break;
                    case 2: g.setPaint(new Color(0,0,255)); break;
                    case 3: g.setPaint(new Color(0,255,255)); break;
                    case 4: g.setPaint(new Color(255,0,255)); break;
                    }*/

                    if (square[y][x] == 0) g.setPaint(new Color(0,255,0));
                    else g.setPaint(new Color(255,0,0));
                    g.fillOval(350 + x * 80, 94 + y * 80, 20, 20);
                }               
            }
        }
    }

    public void forceSync()
    {
        URL url;
        HttpURLConnection con;
        int responseCode;

        if (SwingFrame.opponent == null) return;
        {
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 8; y++)
                {
                    String position = columns[x] + rows[y];

                    try
                    {
                        url = new URL( "http://" + SwingFrame.opponent + "/set?position=" + position + "&value=" + square[y][x] + "&unmoved=" + unmoved[y][x]);                        
                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        responseCode = con.getResponseCode();
                        System.out.println("HTTP GET URL: " + url + ", Response Code: " + responseCode);
                    }
                    catch (Exception ex)
                    {
                        System.out.println("HTTP GET ERROR: " + ex.getMessage());
                    }

                }

            }
        }

    }

    public void processClick()
    {
        if (selectedX >= 0)
        {
            if (selectedX != cursorX || selectedY != cursorY)
            {

                if (moves[cursorY][cursorX] >= 0)
                {
                    square[cursorY][cursorX] = square[selectedY][selectedX];
                    square[selectedY][selectedX] = 0;     
                    unmoved[selectedY][selectedX] = false;

                    if (SwingFrame.opponent != null)
                    {                        
                        String start = columns[selectedX] + rows[selectedY];
                        String end = columns[cursorX] + rows[cursorY];

                        try
                        {
                            URL url = new URL( "http://" + SwingFrame.opponent + "/move?start=" + start + "&end=" + end );                        
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            int responseCode = con.getResponseCode();
                            System.out.println("HTTP GET URL: " + url + ", Response Code: " + responseCode);
                        }
                        catch (Exception ex)
                        {
                            System.out.println("HTTP GET ERROR: " + ex.getMessage());
                        }
                    }
                }
            }
            selectedX = -1;
        }
        else
        {
            if (square[cursorY][cursorX] > 0)
            {
                selectedX = cursorX;
                selectedY = cursorY;                
            }
        }

    }

    class KeyboardyMcKeyboardFace extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int keycode = e.getKeyCode();

            if (keycode == 'w' || keycode == 'W') 
            {
                cursorY -= 1;
                if (cursorY < 0) cursorY = 0;                
            }
            else if (keycode == 's' || keycode == 'S') 
            {
                cursorY += 1;
                if (cursorY > 7) cursorY = 7;
            }            
            else if (keycode == 'a' || keycode == 'A') 
            {
                cursorX -= 1;
                if (cursorX < 0) cursorX = 0;
            }
            else if (keycode == 'd' || keycode == 'D') 
            {
                cursorX += 1;
                if (cursorX > 7) cursorX = 7;
            }
            else if (keycode == ' ')
            {
                processClick();
            }
            else if (keycode == 'r' || keycode == 'R') 
            {
                resetBoard();
            }
            else if (keycode == 'c' || keycode == 'C') 
            {
                showcoords = !showcoords;
            }
            else if (keycode == 'f' || keycode == 'F') 
            {
                forceSync();
            }
            else if (keycode == 'q' || keycode == 'Q') 
            {
                System.exit(0);
                return;
            }            

        }
    }

    class MouseyMcMouseFace implements MouseListener
    {
        @Override
        public void mousePressed(MouseEvent e) {
            processClick();
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}        
    }
}

