package chess.Logic;

import chess.Frame.Board;
import chess.Frame.Frame;
import chess.Frame.GameTimerPanel;
import chess.Frame.Player;

import java.awt.*;

public class Game implements Runnable {
    Thread thread;
    private int FPS = 1000;
    public static Board board;
    public static Frame frame;
    public static GameUpdate gameUpdate;

    public static int screenWidth;
    public static int screenHeight;
    public static Player player1;
    public static Player player2;
    private int currentPlayer;
    private boolean color;
    public Settings settings;
    public static boolean validating;

    public Game() {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screensize.getWidth();
        screenHeight = (int) screensize.getHeight();
        validating = true;
        Ressources.importPieces();
        Colors.changeTheme(0);
        settings = new Settings(1, "5:00", 0, false, "Player vs. Player", 1);
        color = randomColor();
        currentPlayer = 1;
        player1 = new Player(1);
        player2 = new Player(2);

        gameUpdate = new GameUpdate(color, currentPlayer);
        board = new Board(color, gameUpdate);
        frame = new Frame(board, gameUpdate);
        gameUpdate.getTop();
        start();
    }

    private void start() {
        thread = new Thread(this);
        thread.start();
       
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS;

        long previousTime = System.nanoTime();

        long lastCheck = System.nanoTime();

        double deltaF = 0;
        while (true) {
            long currentTime = System.nanoTime();
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaF >= 1) {
                deltaF--;
                //System.out.println(AI.remove);
                frame.repaint();
                if(validating == true){
                    frame.validate();
                }
            }

            if (System.nanoTime() - lastCheck >= 1000000000) {
                lastCheck = System.nanoTime();
            }
        }
    }

    private boolean randomColor() {
        double random = Math.random();
        if (random > 0.5) {
            return true;
        } else {
            return false;
        }
    }
    public static void newGame(){
        gameUpdate = new GameUpdate(true, 1);
        gameUpdate.getTop();
    }
    public static void stopValidating(){
        validating = false;
    }
    public static void startValidating(){
        validating = true;
    }
}
