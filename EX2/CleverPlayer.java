import java.util.Random;
class CleverPlayer implements Player{
    // starts with the first row and fill every free coordinate in it - in 25% of the time chooses a random coordinate on the board
    Random rand = new Random();
    public void playTurn(Board board, Mark mark){
    int randInt = rand.nextInt(100);
        if (randInt<75){
            for (int row = 0; row < board.getSize(); row++){
                for (int col = 0; col < board.getSize(); col++){
                    if (board.putMark(mark, row, col)) {
                        return;
                    }
                }
            }
        }else {
            if (!board.putMark(mark, rand.nextInt(board.getSize()), rand.nextInt(board.getSize()))){
                playTurn(board,mark);
            }
        }
    }
}