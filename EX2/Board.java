public class Board {

    private Mark[][] board;
    private int size;

    Board(){
        this.size = 4;
        this.board = new Mark[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    Board(int size){
        this.size = size;
        this.board = new Mark[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    public int getSize(){
        return size;
    }

    public Mark[][] getBoard() {
        return board;
    }

    public boolean putMark (Mark mark, int row, int col){
        if (((0 <= row && row < this.board.length) && (0 <= col && col < this.board.length) && board[row][col] == Mark.BLANK)) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    Mark getMark(int row, int col) {
        if ((0 <= row && row < this.board.length) && (0 <= col && col < this.board.length)) {
            return board[row][col];
        }
        return Mark.BLANK;
    }
}
