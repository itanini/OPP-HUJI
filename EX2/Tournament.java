public class Tournament {

    int rounds;
    Renderer renderer;
    Player[] players;

    Tournament(int rounds, Renderer renderer, Player[] players){
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = players;

    }

    void playTournament(int size, int winStreak, String[] playerNames){
        int tiesScore = 0;
        int player1Score = 0;
        int player2Score = 0;
        int turn = 0;

        if (!(1<size && size <10)){
            System.exit(2);
        }
        for (int round = 0; round < rounds; round++) {
            Player playerX = players[turn % 2];
            Player playerO = players[(turn + 1) % 2];
            Game game = new Game(playerX, playerO, winStreak, size, renderer);
            Mark gameWinner = game.run();
            if (gameWinner == Mark.BLANK){
                tiesScore++;
            } else if (gameWinner == Mark.X) {
                if (turn % 2 == 0) {
                    player1Score++;
                } else {
                    player2Score++;
                }
            }
            else if (gameWinner == Mark.O){
                if (turn%2 == 1) {
                    player1Score++;
                }else {
                    player2Score++;
                }
            }
            turn++;
        }
        System.out.println("######### Results ######### \nPlayer 1, " + playerNames[0] + " won: " + player1Score + " rounds \n" +
                "Player 2, " + playerNames[1] + " won: " + player2Score + " rounds\n" +
                "Ties: " +tiesScore);
    }
    public static void main(String[] args) {
        RendererFactory rendererFactory = new RendererFactory();
        PlayerFactory playerFactory = new PlayerFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[3], Integer.parseInt(args[1]));
        Tournament tournament = new Tournament(Integer.parseInt(args[0]), renderer,
                new Player[]{playerFactory.buildPlayer(args[4]), playerFactory.buildPlayer(args[5])});
        tournament.playTournament(Integer.parseInt(args[1]), Integer.parseInt(args[2]), new String[] {args[4], args[5]});


    }
}
