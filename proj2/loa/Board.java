/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Yi Zhang
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Square s = sq(c, r);
                _board[s.index()] = contents[r][c];
            }
        }
        setMoveLimit(DEFAULT_MOVE_LIMIT);
        _turn = side;
        _subsetsInitialized = false;
        _winnerKnown = false;
        _winner = null;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }

        for (Square sq : ALL_SQUARES) {
            _board[sq.index()] = board.get(sq);
        }
        _turn = board.turn();
        _winner = board.winner();
        _whiteRegionSizes.addAll(board.getRegionSizes(WP));
        _blackRegionSizes.addAll(board.getRegionSizes(BP));
        _moves.addAll(board._moves);
        _subsetsInitialized = board._subsetsInitialized;
        _winnerKnown = board._winnerKnown;
        _moveLimit = board._moveLimit;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        if (next != null) {
            _turn = next;
        }
        _board[sq.index()] = v;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves (before tie results) to LIMIT. */
    void setMoveLimit(int limit) {
        _moveLimit = limit;
        _winnerKnown = false;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        Square from = move.getFrom(),
                to = move.getTo();
        if ((get(from) != get(to)) && (get(to) != EMP)) {
            move = Move.mv(from, to, true);
        }
        set(to, get(from)); set(from, EMP);
        _turn = get(to).opposite();
        _moves.add(move);
        _subsetsInitialized = false;
        _winnerKnown = false;
        computeRegions();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move last = _moves.get(_moves.size() - 1);
        _moves.remove(_moves.size() - 1);
        Square from = last.getFrom();
        Square to = last.getTo();
        set(from, get(to));
        if (!last.isCapture()) {
            set(to, EMP);
        } else {
            set(to, get(to) == BP ? WP : BP);
        }
        _turn = turn().opposite();
        _subsetsInitialized = false;
        _winnerKnown = false;
        computeRegions();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        boolean res = true;
        if (turn() != get(from)) {
            res = false;
        }
        if (!from.isValidMove(to)) {
            res = false;
        }
        int dir = from.direction(to);
        int dis = from.distance(to);
        int steps = steps(from, dir) + steps(from, (4 + dir) % 8) - 1;
        if (dis != steps) {
            res = false;
        }
        Piece fromPiece = get(from);
        Piece toPiece = get(to);
        if ((fromPiece == toPiece) && (fromPiece != EMP)) {
            res = false;
        }
        return res && !blocked(from, to);
    }

    /** Return the number of non EMP pieces starts from
     *  square SQ (including SQ) in direction DIR. */
    int steps(Square sq, int dir) {
        Square pointer = sq;
        int step = 0;
        while (pointer != null) {
            Piece p = get(pointer);
            if (p != EMP) {
                step += 1;
            }
            pointer = pointer.moveDest(dir, 1);
        }
        return step;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        ArrayList<Move> res = new ArrayList<>();
        for (Square sq : ALL_SQUARES) {
            if (get(sq) != turn()) {
                continue;
            }
            for (Square adja : sq.adjacent()) {
                int dir = sq.direction(adja);
                int step = steps(sq, dir) + steps(sq, (4 + dir) % 8) - 1;
                Square to = sq.moveDest(dir, step);
                if ((to != null) && (isLegal(sq, to))) {
                    boolean capture = (get(sq) != get(to))
                            && (get(to) != EMP);
                    Move mv = Move.mv(sq, to, capture);
                    res.add(mv);
                }
            }
        }
        return res;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces contiguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are contiguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            boolean wpCon = piecesContiguous(WP);
            boolean bpCon = piecesContiguous(BP);
            if ((bpCon) && (wpCon)) {
                _winnerKnown = true;
                if (turn() == WP) {
                    _winner =  BP;
                } else {
                    _winner = WP;
                }
            } else if (wpCon) {
                _winnerKnown = true;
                _winner = WP;
            } else if (bpCon) {
                _winnerKnown = true;
                _winner = BP;
            }
            if (_moves.size() == 2 * DEFAULT_MOVE_LIMIT) {
                _winnerKnown = true;
                _winner = EMP;
            }
            return _winner;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        Piece fromPiece = get(from);
        int step = from.distance(to);
        int dir = from.direction(to);
        Square next = from.moveDest(dir, 1);
        while (step > 1) {
            if (next == null) {
                return false;
            }
            Piece p = get(next);
            if ((p != fromPiece) && (p != EMP)) {
                return true;
            }
            next = next.moveDest(dir, 1);
            step -= 1;
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        int count = 0;
        int r = sq.row(),
            c = sq.col();
        if ((visited[r][c]) || (get(sq) != p)) {
            return 0;
        } else {
            visited[r][c] = true;
            count = 1;
        }
        for (Square adjSq : sq.adjacent()) {
            count += numContig(adjSq, visited, p);
        }
        return count;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Square sq = sq(c, r);
                if ((visited[r][c]) || (get(sq) == EMP)) {
                    continue;
                }
                int size = numContig(sq, visited, get(sq));
                if (size > 0) {
                    if (get(sq) == WP) {
                        _whiteRegionSizes.add(size);
                    } else {
                        _blackRegionSizes.add(size);
                    }
                }
            }
        }

        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** Return the very last move. */
    Move lastMove() {
        return _moves.get(_moves.size() - 1);
    }

    /** Return the list of unretracted moves. */
    List<Move> allMove() {
        return _moves;
    }


    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
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

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();

    /** Current side on move. */
    private Piece _turn;

    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;

    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;

    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of contiguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
