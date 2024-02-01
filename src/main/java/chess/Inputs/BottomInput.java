package chess.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import chess.Logic.Game;
import chess.Logic.GameUpdate;

public class BottomInput implements MouseListener {
    private JLabel arrowLeft;
    private JLabel arrowRight;
    private JLabel surrender;
    private JLabel newGame;
    private JLabel rotate;
    private GameUpdate gameUpdate;

    public BottomInput(JLabel arrowLeft, JLabel arrowRight, JLabel surrender, JLabel newGame, JLabel rotate,
            GameUpdate gameUpdate) {
        this.arrowLeft = arrowLeft;
        arrowLeft.addMouseListener(this);
        this.arrowRight = arrowRight;
        arrowRight.addMouseListener(this);
        this.surrender = surrender;
        surrender.addMouseListener(this);
        this.newGame = newGame;
        newGame.addMouseListener(this);
        this.rotate = rotate;
        rotate.addMouseListener(this);
        this.gameUpdate = gameUpdate;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        if (gameUpdate.settingsActive == false) {
            if (e.getSource() == arrowLeft) {
                gameUpdate.backOrForward(0);
            } else if (e.getSource() == arrowRight) {
                gameUpdate.backOrForward(1);
            } else if (e.getSource() == surrender && (gameUpdate.isEnd == false)) {
                gameUpdate.surrender();
            } else if (e.getSource() == rotate) {
                gameUpdate.rotate();
            } else if (e.getSource() == newGame) {
                gameUpdate.newGame();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}