import java.util.Random;

class WhateverPlayer implements Player{
    //chooses random coordinates
    Random rand = new Random();
    public void playTurn(Board board, Mark mark){
        if (!board.putMark(mark, rand.nextInt(board.getSize()), rand.nextInt(board.getSize()))){
            playTurn(board,mark);
        }
    }
}