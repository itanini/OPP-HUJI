import java.util.Random;

class GeniusPlayer implements Player{
    // chooses the least filled cullonm and fill it.

    private Random rand = new Random();
    private int chosenCol = -1;
    public void playTurn(Board board, Mark mark) {
        // Player chooses the emptiest col to fill
        if (chosenCol < 0) {
            chosenCol = getMaxBlankCol(board);
        }
        for (int row = 0; row < board.getSize(); row++) {
            if (board.putMark(mark, row, chosenCol)) {
                return;
            }
        }
        while (!board.putMark(mark, rand.nextInt(board.getSize()), rand.nextInt(board.getSize()))) {
            continue;
        }
    }
    private static int getMaxBlankCol(Board board) {
        // finds the least filled cullonm
        int[] colCounterArray = new int[board.getSize()];
        for (int col = 0; col < board.getSize(); col++){
            int colCounter = 0;
            for (int row = 0; row < board.getSize(); row++){
                if (board.getMark(row, col) == Mark.BLANK){
                    colCounter += 1;
                }
            }
            colCounterArray[col] = colCounter;
        }
        int maxBlankCol = 0;
        for (int i = 0; i < colCounterArray.length; i++){
            if (colCounterArray[i] > colCounterArray[maxBlankCol]){
                maxBlankCol = i;
            }
        }
        return maxBlankCol;
    }
}
