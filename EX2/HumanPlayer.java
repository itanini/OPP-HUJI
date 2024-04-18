import java.util.Scanner;

class HumanPlayer implements Player{
    private Scanner scanner = new Scanner(System.in);
    private int row;
    private int col;
    public void playTurn(Board board, Mark mark){
        System.out.println("Player " + mark + ", type coordinates");
        String coordinates = scanner.nextLine();
        if (coordinates.length() == 2) {
            row = Character.getNumericValue(coordinates.charAt(0));
            col = Character.getNumericValue(coordinates.charAt(1));
        } else {
            row = -1;
            col = -1;
        }
        if (board.putMark(mark, row,col)){
        } else {
            System.out.println("Invalid coordinates, type again:");
            playTurn(board, mark);
        }
    }
}
