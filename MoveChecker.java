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

    private Node[] moves = new Node[7];

    public MoveChecker()
    {
        for (int piece = 0; piece < 7; piece++)
        {
            moves[piece] = new Node(new int[]{0, 0});
        }

        Node up         = new Node(new int[]{0, -1});
        Node down       = new Node(new int[]{0, +1});
        Node left       = new Node(new int[]{-1, 0});
        Node right      = new Node(new int[]{+1, 0});
        Node upright    = new Node(new int[]{+1, -1});
        Node upleft     = new Node(new int[]{-1, -1});
        Node downright  = new Node(new int[]{+1, +1});
        Node downleft   = new Node(new int[]{-1, +1});

        moves[0].addEdge(up); // KING
        moves[0].addEdge(down);
        moves[0].addEdge(left);
        moves[0].addEdge(right);
        moves[0].addEdge(upright);
        moves[0].addEdge(upleft);
        moves[0].addEdge(downright);
        moves[0].addEdge(downleft);

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
            ups[i - 1]         = new Node(new int[]{0, -i});
            downs[i - 1]       = new Node(new int[]{0, +i});
            lefts[i - 1]       = new Node(new int[]{-i, 0});
            rights[i - 1]      = new Node(new int[]{+i, 0});
            uprights[i - 1]    = new Node(new int[]{+i, -i});
            uplefts[i - 1]     = new Node(new int[]{-i, -i});
            downrights[i - 1]  = new Node(new int[]{+i, +i});
            downlefts[i - 1]   = new Node(new int[]{-i, +i});

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

    }

    public ArrayList<Node> getMoves(int spriteNumber)
    {
        switch (spriteNumber)
        {
            case 6:
            case 12:
            return moves[0].getEdges();
        }
        return null;
    }

}