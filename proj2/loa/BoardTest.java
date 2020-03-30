/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Square.sq;
import static loa.Move.mv;

/** Tests of the Board class API.
 *  @author Yi Zhang
 */
public class BoardTest {

    /** A "general" position. */
    static final Piece[][] BOARD1 = {
        { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD2 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  BP,  BP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP, EMP,  WP },
        { EMP,  WP,  WP,  BP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD3 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
        { EMP,  WP,  WP,  WP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A "general" position. */
    static final Piece[][] BOARD4 = {
            { EMP, EMP,  EMP,  BP,  BP, EMP, EMP, EMP},
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
            { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP },
            { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP },
            { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A "general" position. */
    static final Piece[][] BOARD5 = {
            { EMP, EMP,  EMP, EMP, BP, EMP, EMP, EMP },
            { WP,  EMP, EMP, EMP, EMP,  BP, EMP, WP  },
            { WP,  EMP, EMP, BP,  BP,  BP, EMP, WP  },
            { WP,   BP,  BP, EMP, EMP,  WP, EMP, EMP },
            { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP },
            { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    static final Piece[][] INITIAL_PIECES = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };


    static final String BOARD1_STRING =
        "===\n"
        + "    - b b b - b b - \n"
        + "    - - - - - - - - \n"
        + "    w - - - b - - w \n"
        + "    w - w w - w - - \n"
        + "    w - b - - w - - \n"
        + "    w - - - b b - w \n"
        + "    w - - - - - - w \n"
        + "    - b - b b - - - \n"
        + "Next move: black\n"
        + "===";

    /** Test display */
    @Test
    public void toStringTest() {
        assertEquals(BOARD1_STRING, new Board(BOARD1, BP).toString());
    }

    /** Test legal moves. */
    @Test
    public void testLegality1() {
        Board b = new Board(BOARD1, BP);
        assertTrue("f3-d5", b.isLegal(mv("f3-d5")));
        assertTrue("f3-h5", b.isLegal(mv("f3-h5")));
        assertTrue("f3-h1", b.isLegal(mv("f3-h1")));
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertFalse("f3-d1", b.isLegal(mv("f3-d1")));
        assertFalse("f3-h3", b.isLegal(mv("f3-h3")));
        assertFalse("f3-e4", b.isLegal(mv("f3-e4")));
        assertFalse("c4-c7", b.isLegal(mv("c4-c7")));
        assertFalse("b1-b4", b.isLegal(mv("b1-b4")));
    }

    /** Test contiguity. */
    @Test
    public void testContiguous1() {
        Board b1 = new Board(BOARD1, BP);
        assertFalse("Board 1 black contiguous?", b1.piecesContiguous(BP));
        assertFalse("Board 1 white contiguous?", b1.piecesContiguous(WP));
        assertFalse("Board 1 game over?", b1.gameOver());

        Board b2 = new Board(BOARD2, BP);
        assertTrue("Board 2 black contiguous?", b2.piecesContiguous(BP));
        assertFalse("Board 2 white contiguous?", b2.piecesContiguous(WP));
        assertTrue("Board 2 game over", b2.gameOver());

        Board b3 = new Board(BOARD3, BP);
        assertTrue("Board 3 white contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(BP));
        assertTrue("Board 3 game over", b3.gameOver());
    }

    @Test
    public void testEquals1() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD1, BP);

        assertEquals("Board 1 equals Board 2", b1, b2);
    }

    @Test
    public void testEquals2() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD4, BP);

        b2.set(sq("b1"), BP);
        assertEquals("Board 1 equals Board 2", b1, b2);
    }

    @Test
    public void turnTest() {
        Board b1 = new Board(BOARD1, BP);

        b1.makeMove(mv("b1-b3"));
        assertEquals(WP, b1.turn());
        b1.makeMove(mv("a2-c2"));
        assertEquals(BP, b1.turn());

        b1.retract();
        assertEquals(WP, b1.turn());
        b1.retract();
        assertEquals(BP, b1.turn());

        Board b = new Board(BOARD1, WP);
        assertEquals(WP, b.turn());
        assertFalse(b.isLegal(mv("b1-b3")));
        b.makeMove(mv("a2-c2"));
        assertEquals(BP, b.turn());
    }

    @Test
    public void legalTest() {
        Board b1 = new Board(BOARD1, BP);

        Move correct1 = mv("g8-h7");
        assertTrue("This move is legal", b1.isLegal(correct1));

        Move correct2 = mv("c8-c5");
        assertTrue("This move is legal", b1.isLegal(correct2));

        Move mvBlock = mv("c8-g4");
        assertFalse("This move is blocked", b1.isLegal(mvBlock));

        Move mvWrongTurn = mv("a2-c2");
        assertFalse("We cannot move our opponents piece",
                b1.isLegal(mvWrongTurn));

        Move mvWrongDis = mv("c8-h3");
        assertFalse("This move can only move 4, positions ",
                b1.isLegal(mvWrongDis));
    }


    @Test
    public void testRetract() {
        Board b1 = new Board(BOARD2, BP);
        Board b2 = new Board(BOARD2, BP);

        Move mv = mv("b4-b1");
        b2.makeMove(mv);
        b2.retract();
        assertEquals("Board 1 equals Board 1", b1, b2);
    }

    @Test
    public void testMove1() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);
        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(4, 5)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(6, 3)));
        assertEquals("Check move count for board 1 after one move",
                     1, b1.movesMade());
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                     0, b1.movesMade());
    }

    @Test
    public void testMove2() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);

        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(4, 5)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(6, 3)));
        assertEquals("Check move count for board 1 after one move",
                1, b1.movesMade());

        b1.makeMove(mv("a2-c2"));
        assertEquals("square c2 after a2-c2", WP, b1.get(sq(2, 1)));
        assertEquals("square a2 after a2-c2", EMP, b1.get(sq(0, 1)));
        assertEquals("Check move count for board 1 after one move",
                2, b1.movesMade());

        b1.retract();
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                0, b1.movesMade());
    }

    @Test
    public void testRegion() {
        Board b5 = new Board(BOARD5, BP);
        assertEquals(5, b5.getRegionSizes(WP).size());
        assertEquals(4, b5.getRegionSizes(BP).size());

        Board b0 = new Board(INITIAL_PIECES, BP);
        assertEquals(2, b0.getRegionSizes(WP).size());
        assertEquals(2, b0.getRegionSizes(WP).size());
    }

    @Test
    public void legalMoveTest() {
        Board b0 = new Board(INITIAL_PIECES, BP);
        List<Move> legalSet = b0.legalMoves();
        assertEquals(36, legalSet.size());
    }

}
