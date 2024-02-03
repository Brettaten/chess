package chess.Frame;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

import chess.Logic.Game;
import chess.Logic.GameUpdate;

public class Frame extends JFrame {

    public Side1 side1;
    public Side2 side2;
    public Player player1;
    public Player player2;
    public Top top;
    private Bottom bottom;
    public static boolean color = true;

    public Frame(Board board, GameUpdate gameUpdate) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true); // cant change size
        this.setTitle("Chess"); // set Titel
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        player1 = Game.player1;
        player2 = Game.player2;

        side1 = new Side1(player1, 1);
        side2 = new Side2(player2, 2);
        top = new Top(gameUpdate);
        bottom = new Bottom(gameUpdate);

        //board.setPreferredSize(new Dimension(700, 700));

        this.add(board, BorderLayout.CENTER);
        this.add(side1 , BorderLayout.WEST);
        this.add(side2, BorderLayout.EAST);
        this.add(top, BorderLayout.NORTH);
        this.add(bottom, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null); // center frame
        this.setVisible(true);
    }

    public Top getTop() {
        return top;
    }
    public JLabel getTimerLabel1(){
        return side1.gameTimerPanel1.timerLabel;
    }

    public JLabel getTimerLabel2(){
        return side2.gameTimerPanel2.timerLabel;
    }

    public void addSettings(SettingsFrame settingsFrame) {
        this.add(settingsFrame, BorderLayout.CENTER);
        this.validate();
        this.repaint();
    }
}
