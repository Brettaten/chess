package chess.Frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import chess.Inputs.Input;
import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.GameUpdate;
import chess.Logic.Ressources;

public class Board extends JPanel {
    private BufferedImage img;
    private BufferedImage[] piecesImg;
    private boolean isDragged;
    private int draggX;
    private int draggY;
    private int draggType;

    GameUpdate gameUpdate;
    private int draggPosX;
    private int draggPosY;

    public Board(boolean color, GameUpdate gameUpdate) {
        piecesImg = new BufferedImage[12];
        img = Ressources.pieces;
        addImg();

        this.gameUpdate = gameUpdate;
        this.isDragged = false;
        this.draggType = -1;
        this.draggX = -1;
        this.draggPosY = -1;
        this.setBorder(new LineBorder(Colors.borderColor, 5));
        
        Input input = new Input(gameUpdate);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public Board() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        gameUpdate = Game.gameUpdate;
        boolean boardColor = true;
        Color color1 = Colors.fieldsBright;
        Color color2 = Colors.fieldsDark;
        Color color3 = Colors.selectedColor;
        Color color4 = Colors.lastMove;
        Color color5 = Colors.checked;
        Color color6 = Colors.marked;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardColor) {
                    if (gameUpdate.isCheckedAfter(i, j)) {
                        g.setColor(color5);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    } 
                    else {
                        g.setColor(color1);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    if (gameUpdate.isLastMove(i, j)) {
                        g.setColor(color4);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    if (gameUpdate.isSelected(i, j)) {
                         if(gameUpdate.isEnemy(i,j)){
                            g.setColor(color3);
                            g.fillRect(j*100,i*100,100,5);
                            g.fillRect(j*100, i*100+95, 100, 5);
                            g.fillRect(j*100, i*100, 5, 100);
                            g.fillRect(j*100+95, i*100, 5, 100);
                        }
                        else{
                        g.setColor(color3);
                        g.fillArc(j * 100 + 50-25/2, i * 100 + 50-25/2, 25, 25, 0, 360);
                        }
                    }
                    if(gameUpdate.isMarked(i, j)){
                        g.setColor(color5);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    
                    if (j != 7) {
                        boardColor = false;
                    }
                } else {
                    if (gameUpdate.isCheckedAfter(i, j)) {
                        g.setColor(color5);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    } 
                    else {
                        g.setColor(color2);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    if (gameUpdate.isLastMove(i, j)) {
                        g.setColor(color4);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    if (gameUpdate.isSelected(i, j)) {
                        if(gameUpdate.isEnemy(i,j)){
                            g.setColor(color3);
                            g.fillRect(j*100,i*100,100,5);
                            g.fillRect(j*100, i*100+95, 100, 5);
                            g.fillRect(j*100, i*100, 5, 100);
                            g.fillRect(j*100+95, i*100, 5, 100);
                        }
                        else{
                        g.setColor(color3);
                        g.fillArc(j * 100 + 50-25/2, i * 100 + 50-25/2, 25, 25, 0, 360);
                        }
                    }
                    if(gameUpdate.isMarked(i, j)){
                        g.setColor(color5);
                        g.fillRect(j * 100, i * 100, 100, 100);
                    }
                    if (j != 7) {
                        boardColor = true;
                    }
                }

            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameUpdate.pieces[i][j] != null) {
                    if(i == draggPosX && j == draggPosY){
                        ;
                    }
                    else if(gameUpdate.pieces[i][j].length() == 4) {
                        g.drawImage(piecesImg[Integer.parseInt(gameUpdate.pieces[i][j].substring(0, 2))], j * 100,
                                i * 100, null);
                    } else {
                        g.drawImage(piecesImg[Character.getNumericValue(gameUpdate.pieces[i][j].charAt(0))], j * 100,
                                i * 100, null);
                    }
                }
            }
        }
        if (isDragged == true) {
            g.setColor(color3);
            g.fillRect(draggPosY*100, draggPosX*100, 100, 100);
            g.drawImage(piecesImg[draggType], draggY - 50, draggX - 50, null);
        }
    }

    private void addImg() {
        for (int i = 0, j = 0; i < 6; i++, j += 2) {
            piecesImg[j] = img.getSubimage(i * 100, 0, 100, 100);
            piecesImg[j + 1] = img.getSubimage(i * 100, 100, 100, 100);
        }
    }

    public void promtotion(Promotion promotion) {
        this.setLayout(null);
        this.add(promotion);
        
    }

    public void isDragged(int x, int y, int type, int posX, int posY) {
        if(type != -1 && posX != -1 && posY != -1 && x >= 0+25 && x <= 800-25 && y >= 0+25 && y <= 800-25){
            isDragged = true;
            draggX = x;
            draggY = y;
            draggType = type;
            this.draggPosX = posX;
            this.draggPosY = posY;
        }
        else if(type != -1 && posX != -1 && posY != -1 && x <= 0+25 || x >= 800-25 || y <= 0+25 || y >= 800-25){
            isDragged = true;
            if(x <= 0+25){
                draggX = 0+25;
            }
            else if(x >= 800-25){
                draggX = 800-25;
            }
            else if(x >= 0+25 && x <= 800-25){
                draggX = x;
            }
            if(y <= 0+25){
                draggY = 0+25;
            }
            else if(y >= 800-25){
                draggY = 800-25;
            }
            else if(y >= 0+25 && y <= 800-25){
                draggY = y;
            }

            if(draggType == -1){
                draggType = type;
            }

            this.draggPosX = posX;
            this.draggPosY = posY;
        }
    }

    public void draggEnd() {
        isDragged = false;
        
        draggPosX = -1;
        draggPosY = -1;
        draggType = -1;
    }
}
