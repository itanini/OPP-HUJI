public class Game {

    private int size;
    private int winStreak;
    private Board board;
    private Player playerX;
    private Player playerO;
    private Renderer renderer;

    Game(Player playerX, Player playerO, Renderer renderer) {
        this.winStreak = 3;
        this.board = new Board(4);
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    Game(Player playerX, Player playerO, int winStreak, int size, Renderer renderer) {
        if (winStreak > size){
            this.winStreak = size;
        }else {
            this.winStreak = winStreak;
        }
        this.board = new Board(size);
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;


    }

    public int getWinStreak() {
        return winStreak;
    }

    public Mark run() {
        Player curPlayer = playerO;
        Mark curMark = Mark.O;
        while (gameStatus(curMark) == null) {
            curMark = changeMark(curMark);
            curPlayer = changePlayer(curPlayer);
            curPlayer.playTurn(board, curMark);
            renderer.renderBoard(board);


        }
        return gameStatus(curMark);
    }

    private static Mark changeMark(Mark curMark) {
        if (curMark == Mark.X){
            curMark = Mark.O;
        } else {
            curMark = Mark.X;
        }
        return curMark;
    }

    private Player changePlayer(Player curPlayer) {
        if (curPlayer == playerX){
            curPlayer = playerO;
        } else {
            curPlayer = playerX;
        }
        return curPlayer;
    }

    private Mark gameStatus(Mark mark) {
        // checks if one of the marks got to a win streak
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getMark(row, col) == mark) {
                    Mark gameStatus = checkStreak(row, col, mark);
                    if (gameStatus != null) {
                        return mark;
                    }
                }
            }
        }
        if (checkIfDraw()) {
            return Mark.BLANK;
        }
        return null;
    }

    private boolean checkIfDraw() {
        // returns true only if the board is totally full
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getMark(row, col) == Mark.BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    private Mark checkStreak(int originalRow, int originalCol, Mark mark) {
        int curRow = originalRow;
        int curCol = originalCol;
        for (int row = -1; row < 2; row++) {
            for (int col = -1; col < 2; col++) {
                // Checks if the suspected coordinate is not the original one, and if it is inside the board
                if (!(row == 0 && col == 0) && (-1 < curRow + row && curRow + row < board.getSize()) && (-1 < curCol + col && curCol + col < board.getSize())) {
                    // Checks if the mark on the current coordinate is equal to the mark on the suspected coordinate.
                    if (mark == board.getMark(curRow + row, curCol + col)) {
                        int streak = 2;
                        if (streak == winStreak){
                            return mark;
                        }
                        while (streak < winStreak) {
                            curRow = curRow + row;
                            curCol = curCol + col;
                            if (!((0 <= curRow + row && curRow + row < board.getSize()) && (0 <= curCol + col && curCol + col < board.getSize()))
                                    // ^ Checks if coordinate is in the board
                                    || (mark != board.getMark(curRow + row, curCol + col)))
                            // ^ Checks if coordinate streak stops
                            {
                                break;
                            }
                            streak++;
                            if (streak == winStreak) {
                                return mark;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
