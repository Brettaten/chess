package chess.Frame;

import java.awt.*;
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
    public static int squareSize;
    private int draggX;
    private int draggY;
    private int draggType;

    GameUpdate gameUpdate;
    private int draggPosX;
    private int draggPosY;

    public Board(boolean color, GameUpdate gameUpdate) {
        piecesImg = new BufferedImage[12];
        img = Ressources.pieces;
        double widthFactor = (double) 560 /1920;
        this.squareSize = (int) (Game.screenWidth - (2*(Game.screenWidth * widthFactor)))/8;
        this.gameUpdate = gameUpdate;
        this.isDragged = false;
        this.draggType = -1;
        this.draggX = -1;
        this.draggPosY = -1;
        this.setBorder(new LineBorder(Colors.borderColor, (int) (Game.screenHeight * ((double) 5/ 1080))));

        addImg();
        
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
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    } 
                    else {
                        g.setColor(color1);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    }
                    if (gameUpdate.isLastMove(i, j)) {
                        g.setColor(color4);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    }
                    if (gameUpdate.isSelected(i, j)) {
                         if(gameUpdate.isEnemy(i,j)){
                            g.setColor(color3);
                            g.fillRect(j*squareSize,i*squareSize,squareSize, (int) (squareSize*0.05));
                            g.fillRect(j*squareSize, (int) (i*squareSize+squareSize-(squareSize*0.05)), squareSize, (int) (squareSize*0.05));
                            g.fillRect(j*squareSize, i*squareSize, (int) (squareSize*0.05), squareSize);
                            g.fillRect(j*squareSize+squareSize-(int) (squareSize*0.05), i*squareSize, (int) (squareSize*0.05), squareSize);
                        }
                        else{
                        g.setColor(color3);
                        g.fillArc(j * squareSize + squareSize/2 - squareSize/8, i * squareSize + squareSize/2 - squareSize/8, squareSize/4, squareSize/4, 0, 360);
                        }
                    }
                    if(gameUpdate.isMarked(i, j)){
                        g.setColor(color5);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    }
                    
                    if (j != 7) {
                        boardColor = false;
                    }
                } else {
                    if (gameUpdate.isCheckedAfter(i, j)) {
                        g.setColor(color5);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    } 
                    else {
                        g.setColor(color2);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    }
                    if (gameUpdate.isLastMove(i, j)) {
                        g.setColor(color4);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    }
                    if (gameUpdate.isSelected(i, j)) {
                        if(gameUpdate.isEnemy(i,j)){
                            g.setColor(color3);
                            g.fillRect(j*squareSize,i*squareSize,squareSize, (int) (squareSize*0.05));
                            g.fillRect(j*squareSize, (int) (i*squareSize+squareSize-(squareSize*0.05)), squareSize, (int) (squareSize*0.05));
                            g.fillRect(j*squareSize, i*squareSize, (int) (squareSize*0.05), squareSize);
                            g.fillRect(j*squareSize+squareSize-(int) (squareSize*0.05), i*squareSize, (int) (squareSize*0.05), squareSize);
                        }
                        else{
                        g.setColor(color3);
                            g.fillArc(j * squareSize + squareSize/2 - squareSize/8, i * squareSize + squareSize/2 - squareSize/8, squareSize/4, squareSize/4, 0, 360);
                        }
                    }
                    if(gameUpdate.isMarked(i, j)){
                        g.setColor(color5);
                        g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
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
                        g.drawImage(piecesImg[Integer.parseInt(gameUpdate.pieces[i][j].substring(0, 2))], j * squareSize,
                                i * squareSize, null);
                    } else {
                        g.drawImage(piecesImg[Character.getNumericValue(gameUpdate.pieces[i][j].charAt(0))], j * squareSize,
                                i * squareSize, null);
                    }
                }
            }
        }
        if (isDragged == true) {
            g.setColor(color3);
            g.fillRect(draggPosY*squareSize, draggPosX*squareSize, squareSize, squareSize);
            g.drawImage(piecesImg[draggType], draggY - 50, draggX - 50, null);
        }
    }

    private void addImg() {
        for (int i = 0, j = 0; i < 6; i++, j += 2) {
            piecesImg[j] = scaleImage(img.getSubimage(i * 100, 0, 100, 100), squareSize, squareSize);
            piecesImg[j + 1] = scaleImage(img.getSubimage(i * 100, 100, 100, 100), squareSize, squareSize);
        }
    }

    public BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return scaledImage;
    }

    public void promtotion(Promotion promotion) {
        this.setLayout(null);
        this.add(promotion);
        
    }

    public void isDragged(int x, int y, int type, int posX, int posY) {
        int distance = (int) (Game.screenHeight * ((double) 25/1080));
        if(type != -1 && posX != -1 && posY != -1 && x >= 0+distance && x <= (squareSize*8)-distance && y >= 0+distance && y <= (squareSize*8)-distance){
            isDragged = true;
            draggX = x;
            draggY = y;
            draggType = type;
            this.draggPosX = posX;
            this.draggPosY = posY;
        }
        else if(type != -1 && posX != -1 && posY != -1 && x <= 0+distance || x >= (squareSize*8)-distance || y <= 0+distance || y >= (squareSize*8)-distance){
            isDragged = true;
            if(x <= 0+distance){
                draggX = 0+distance;
            }
            else if(x >= (squareSize*8)-distance){
                draggX = (squareSize*8)-distance;
            }
            else if(x >= 0+distance && x <= (squareSize*8)-distance){
                draggX = x;
            }
            if(y <= 0+distance){
                draggY = 0+distance;
            }
            else if(y >= (squareSize*8)-distance){
                draggY = (squareSize*8)-distance;
            }
            else if(y >= 0+distance && y <= (squareSize*8)-distance){
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
