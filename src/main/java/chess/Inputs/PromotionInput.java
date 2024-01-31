package chess.Inputs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chess.Logic.Game;
import chess.Logic.GameUpdate;

public class PromotionInput implements MouseListener{

    private int posX;
    private int posY;
    private int x;
    private int y;
    private int player1Piece;
    private int player2Piece;
    private GameUpdate gameUpdate;
    
    public PromotionInput(GameUpdate gameUpdate, int x, int y, int player1Piece, int player2Piece){
        this.gameUpdate = gameUpdate;
        this.x = x;
        this.y = y;
        this.player1Piece = player1Piece;
        this.player2Piece = player2Piece;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        posX = e.getY()/100;
        posY = e.getX()/100;
        Game.frame.top.removePromotion();
        gameUpdate.isPromoting = false;

        gameUpdate.promotion(posX, posY, x, y, player1Piece, player2Piece);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }
    
}
