/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static loa.Piece.*;

/** An automated Player.
 *  @author Yi Zhang
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels. Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if ((depth == 0) || (board.gameOver())) {
            return loaHeuristic(board);
        }
        ArrayList<Board> children = nextBoard(board);
        int res;
        Board recommend = null;
        if (sense == 1) {
            int maxSofar = -INFTY;
            for (Board potential : children) {
                int h = findMove(potential, depth - 1, saveMove,
                        sense, alpha, beta);
                if (h > maxSofar) {
                    maxSofar = h;
                    recommend = potential;
                }
                alpha = Math.max(alpha, h);
                if (beta <= alpha) {
                    break;
                }
            }
            res = maxSofar;
        } else {
            int minSofar = INFTY;
            for (Board potential : children) {
                int h = findMove(potential, depth - 1, saveMove,
                        sense, alpha, beta);
                if (h < minSofar) {
                    minSofar = h;
                    recommend = potential;
                }
                beta = Math.min(beta, h);
                if (beta <= alpha) {
                    break;
                }
            }
            res = minSofar;
        }
        if (saveMove) {
            assert recommend != null;
            _foundMove = recommend.lastMove();
        }
        return res;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        Random r = new Random();
        if (r.nextInt(10) > 5) {
            return RANDOM_DEPTH1;
        } else {
            return RANDOM_DEPTH2;
        }
    }

    /** Return the heuristic value of board B. */
    private int loaHeuristic(Board b) {
        if (b.gameOver()) {
            if (b.winner() == WP) {
                return WINNING_VALUE;
            } else if (b.winner() == BP) {
                return -WINNING_VALUE;
            } else {
                return 0;
            }
        }
        return threat(b) + solid(b) + normal(b) + centralize(b);
    }

    /** Return a great reward of 80 (for maximizer, -80 for minimizer) if
     * one side piece P is one move from win for the given board B. */
    private int threat(Board b) {
        for (Move mv : b.legalMoves()) {
            b.makeMove(mv);
            if ((b.piecesContiguous(BP))
                    || (b.piecesContiguous(WP))) {
                return b.piecesContiguous(WP) ? THREAT : -THREAT;
            }
            b.retract();
        }
        return 0;
    }

    /** Return a reward of 40 (for maximizer) if there is a solid formation.
     *  in board B and piece P(Pieces that are connected in
     *  multiple directions). */
    private int solid(Board b) {
        for (Square sq : Square.ALL_SQUARES) {
            Piece p = b.get(sq);
            if (p == EMP) {
                continue;
            }
            int count = 0;
            for (int dir = 0; dir < 8; dir++) {
                for (Square adja : sq.adjacent()) {
                    if (b.get(adja) == p) {
                        count += 1;
                    }
                }
            }
            if (count > 3) {
                return p == WP ? SOLID : -SOLID;
            }
        }
        return 0;
    }


    /** If there are no special cases like a threat or a capture, evaluate
     *  board B using features like the biggest region size and return
     *  the static value. */
    private int normal(Board b) {
        return 10 * (b.getRegionSizes(WP).get(0)
                - b.getRegionSizes(BP).get(0));
    }

    /** Generate possible outcomes based on the given B.
     *  Return a list of board. */
    private ArrayList<Board> nextBoard(Board b) {
        ArrayList<Board> nextBoard = new ArrayList<>();
        List<Move> legalMoveSet = b.legalMoves();
        if (legalMoveSet != null) {
            for (Move mv : legalMoveSet) {
                Board help = new Board(b);
                help.makeMove(mv);
                nextBoard.add(help);
            }
        }
        return nextBoard;
    }

    /** Take in a board B and calculate the extend of scattering. */
    private int centralize(Board b) {
        Square center  = Square.sq(Y, X);
        ArrayList<Integer> whiteDis = new ArrayList<>();
        ArrayList<Integer> blackDis = new ArrayList<>();
        for (Square sq : Square.ALL_SQUARES) {
            if (b.get(sq) == EMP) {
                continue;
            }
            if (b.get(sq) == BP) {
                blackDis.add(sq.distance(center));
            } else {
               whiteDis.add(sq.distance(center));
            }
        }
        if (getGame().manualBlack()) {
            return -sigma(whiteDis);
        }
        if (getGame().manualWhite()) {
            return sigma(blackDis);
        }
        return sigma(blackDis) - sigma(whiteDis);
    }

    /** Return the standard deviation of a list L, which contains
     *  several integers. */
    private int sigma(ArrayList<Integer> l) {
        int total = 0;
        for (int i : l) {
            total += i;
        }
        int avg = total / l.size();
        int sigma = 0;
        for (int i : l) {
            sigma += (i - avg) * (i - avg);
        }
        return (int) (Math.sqrt(sigma));
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

    /** Random depth number 1. */
    static final int RANDOM_DEPTH1 = 3;

    /** Random depth number 2. */
    static final int RANDOM_DEPTH2 = 2;

    /** Absolute score of a threat. */
    static final int THREAT = 80;

    /** Absolute score of a solid structure. */
    static final int SOLID = 40;

    static Random r = new Random();
    /** Random x coordinate to cluster. */
    static final int X = r.nextInt(8);

    /** Random y coordinate to cluster. */
    static final int Y = r.nextInt(8);
}
