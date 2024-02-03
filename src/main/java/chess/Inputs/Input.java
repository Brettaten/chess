package chess.Inputs;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import chess.Frame.Board;
import chess.Logic.Game;
import chess.Logic.GameUpdate;

public class Input implements MouseInputListener {
    private int x;
    private int y;
    private int startDraggx;
    private int startDraggy;
    private GameUpdate gameUpdate;

    public Input(GameUpdate gameUpdate, boolean promotion, int posX, int posY) {
        this.gameUpdate = gameUpdate;
    }

    public Input(GameUpdate gameUpdate) {
        this.gameUpdate = gameUpdate;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        if (e.getButton() == MouseEvent.BUTTON1 && gameUpdate.isPlayer()) {
            if (gameUpdate.isPromoting == false && gameUpdate.maxMove == gameUpdate.currentMove
                    && gameUpdate.surrender == false && gameUpdate.isEnd == false
                    && gameUpdate.settingsActive == false) {
                y = e.getX() / Board.squareSize;
                x = e.getY() / Board.squareSize;

                startDraggx = x;
                startDraggy = y;

                gameUpdate.isDragged = false;
                if (gameUpdate.isSelected(x, y)) {

                    gameUpdate.movePiece(x, y, gameUpdate.selectedX, gameUpdate.selectedY, -1);
                } else {
                    gameUpdate.selectPiece(x, y);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            gameUpdate = Game.gameUpdate;
            y = e.getX() / Board.squareSize;
            x = e.getY() / Board.squareSize;
            gameUpdate.clearMarkedList();
            gameUpdate.draggEnd(x, y);
        }
        if (SwingUtilities.isRightMouseButton(e) && gameUpdate.isDragged == false && gameUpdate.settingsActive == false){
            gameUpdate = Game.gameUpdate;
            y = e.getX() / Board.squareSize;
            x = e.getY() / Board.squareSize;
            gameUpdate.markField(x, y);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        if (SwingUtilities.isLeftMouseButton(e) && gameUpdate.isPlayer() && gameUpdate.pieces[startDraggx][startDraggy] != null){
            gameUpdate.clearMarkedList();
            if (gameUpdate.isPromoting == false && gameUpdate.maxMove == gameUpdate.currentMove
                    && gameUpdate.surrender == false && gameUpdate.isEnd == false
                    && gameUpdate.settingsActive == false) {
                y = e.getX();
                x = e.getY();
                gameUpdate.draggPiece(x, y);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
