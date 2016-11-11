import java.util.ArrayList;

public class MoveChecker
{

    /* INDEX OF PIECES
     * 0 king (sprite 6 and 12)
     * 1 queen (sprite 5 and 11)
     * 2 white pawn (sprite 1)
     * 3 rook (sprite 2 and 8)
     * 4 knight (sprite 3 and 9)
     * 5 bishop (sprite 4 and 10)
     * 6 black pawn (sprite 7)
     */

    /* INDEX OF MOVE TYPES
     * 0 not special
     * 1 capture only (pawn)
     * 2 first move only and not capture (pawn)
     * 3 not capture (pawn)
     * 4 castling (king)
     * 5 castling (rook)
     * 6 en passant (pawn)
     */

    private Node[] moves = new Node[7];

    public MoveChecker()
    {
        for (int piece = 0; piece < 7; piece++)
        {
            moves[piece] = new Node(new int[]{0, 0, 0});
        }

        Node up         = new Node(new int[]{-1, 0, 0});
        Node down       = new Node(new int[]{+1, 0, 0});
        Node upright    = new Node(new int[]{-1, +1, 0});
        Node upleft     = new Node(new int[]{-1, -1, 0});
        Node downright  = new Node(new int[]{+1, +1, 0});
        Node downleft   = new Node(new int[]{+1, -1, 0});

        Node kingleft       = new Node(new int[]{0, -1, 0});
        Node kingleft2       = new Node(new int[]{0, -2, 4});
        kingleft.addEdge(kingleft2);

        Node kingright      = new Node(new int[]{0, +1, 0});
        Node kingright2      = new Node(new int[]{0, +2, 4});
        kingright.addEdge(kingright2);

        moves[0].addEdge(up);       // KING
        moves[0].addEdge(down);
        moves[0].addEdge(upright);
        moves[0].addEdge(upleft);
        moves[0].addEdge(downright);
        moves[0].addEdge(downleft);
        moves[0].addEdge(kingleft);
        moves[0].addEdge(kingright);

        Node[] ups = new Node[7];
        Node[] downs = new Node[7];
        Node[] lefts = new Node[7];
        Node[] rights = new Node[7];
        Node[] uprights = new Node[7];
        Node[] uplefts = new Node[7];
        Node[] downrights = new Node[7];
        Node[] downlefts = new Node[7];

        for (int i = 1; i <= 7; i++)
        {
            ups[i - 1]         = new Node(new int[]{-i, 0, 0});
            downs[i - 1]       = new Node(new int[]{+i, 0, 0});
            lefts[i - 1]       = new Node(new int[]{0, -i, 0});
            rights[i - 1]      = new Node(new int[]{0, +i, 0});
            uprights[i - 1]    = new Node(new int[]{-i, +i, 0});
            uplefts[i - 1]     = new Node(new int[]{-i, -i, 0});
            downrights[i - 1]  = new Node(new int[]{+i, +i, 0});
            downlefts[i - 1]   = new Node(new int[]{+i, -i, 0});

            if (i > 1)
            {
                ups[i - 2].addEdge(ups[i - 1]);
                downs[i - 2].addEdge(downs[i - 1]);
                lefts[i - 2].addEdge(lefts[i - 1]);
                rights[i - 2].addEdge(rights[i - 1]);
                uprights[i - 2].addEdge(uprights[i - 1]);
                uplefts[i - 2].addEdge(uplefts[i - 1]);
                downrights[i - 2].addEdge(downrights[i - 1]);
                downlefts[i - 2].addEdge(downlefts[i - 1]);
            }
        }

        moves[3].addEdge(ups[0]);       // ROOK
        moves[3].addEdge(downs[0]);
        moves[3].addEdge(lefts[0]);
        moves[3].addEdge(rights[0]);

        moves[5].addEdge(uprights[0]);      // BISHOP
        moves[5].addEdge(uplefts[0]);
        moves[5].addEdge(downrights[0]);
        moves[5].addEdge(downlefts[0]);

        moves[1].addEdge(ups[0]);           //QUEEN
        moves[1].addEdge(downs[0]);
        moves[1].addEdge(lefts[0]);
        moves[1].addEdge(rights[0]);
        moves[1].addEdge(uprights[0]);
        moves[1].addEdge(uplefts[0]);
        moves[1].addEdge(downrights[0]);
        moves[1].addEdge(downlefts[0]);

        Node a         = new Node(new int[]{2, 1, 0});
        Node b         = new Node(new int[]{2, -1, 0});
        Node c         = new Node(new int[]{1, 2, 0});
        Node d         = new Node(new int[]{1, -2, 0});
        Node e         = new Node(new int[]{-1, 2, 0});
        Node f         = new Node(new int[]{-1, -2, 0});
        Node g         = new Node(new int[]{-2, 1, 0});
        Node h         = new Node(new int[]{-2, -1, 0});

        moves[4].addEdge(a);
        moves[4].addEdge(b);
        moves[4].addEdge(c);
        moves[4].addEdge(d);
        moves[4].addEdge(e);
        moves[4].addEdge(f);
        moves[4].addEdge(g);
        moves[4].addEdge(h);

        Node pawnup         = new Node(new int[]{-1, 0, 3});
        Node pawnup2         = new Node(new int[]{-2, 0, 2});
        Node pawnupright    = new Node(new int[]{-1, +1, 1});
        Node pawnupleft     = new Node(new int[]{-1, -1, 1});

        Node pawndown       = new Node(new int[]{+1, 0, 3});        
        Node pawndown2       = new Node(new int[]{+2, 0, 2});
        Node pawndownright  = new Node(new int[]{+1, +1, 1});
        Node pawndownleft   = new Node(new int[]{+1, -1, 1});

        moves[2].addEdge(pawnup);       // WHITE PAWN
        pawnup.addEdge(pawnup2);
        moves[2].addEdge(pawnupleft);      
        moves[2].addEdge(pawnupright);      

        moves[6].addEdge(pawndown);     // BLACK PAWN
        pawndown.addEdge(pawndown2);        
        moves[6].addEdge(pawndownleft);      
        moves[6].addEdge(pawndownright);      

    }

    public ArrayList<Node> getMoves(int spriteNumber)
    {
        switch (spriteNumber)
        {
            case 6:     // KING
            case 12:
            return moves[0].getEdges();

            case 5:     // QUEEN
            case 11:
            return moves[1].getEdges();

            case 2:     //ROOK
            case 8:
            return moves[3].getEdges();

            case 4:     //BISHOP
            case 10:
            return moves[5].getEdges();

            case 3:     //KNIGHT
            case 9:
            return moves[4].getEdges();

            case 1:     //WHITE PAWN
            return moves[2].getEdges();

            case 7:     //BLACK PAWN
            return moves[6].getEdges();

        }
        return null;
    }

}
