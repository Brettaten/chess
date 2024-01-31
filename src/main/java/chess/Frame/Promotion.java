package chess.Frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import chess.Logic.Colors;
import chess.Logic.GameUpdate;
import chess.Logic.Ressources;
import chess.Inputs.PromotionInput;

public class Promotion extends JPanel{
    private int posX;
    private int posY;
    private BufferedImage img;
    private BufferedImage[] piecesImg;
    private int currentPlayer;

    private JLabel queen;
    private JLabel rook;
    private JLabel knight;
    private JLabel bishop;

    public Promotion(boolean color, int currentPlayer, GameUpdate gameUpdate, int player1Piece, int player2Piece){
        this.currentPlayer = currentPlayer;
        this.addMouseListener(new PromotionInput(gameUpdate, posX, posY, player1Piece, player2Piece));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        piecesImg = new BufferedImage[4];

        img = Ressources.pieces;
        addImg();

        createPanel();
    }
    private void addImg() {
        if(currentPlayer == 1){
            for(int i = 1; i < 5; i++){
                piecesImg[i-1] = img.getSubimage(i * 100, 0, 100, 100);
            }
        }
        else{
            for(int i = 1; i < 5; i++){
                piecesImg[i-1] = img.getSubimage(i * 100, 100, 100, 100);
            }
        }
        queen = new JLabel(new ImageIcon(piecesImg[0]));
        rook = new JLabel(new ImageIcon(piecesImg[3]));
        knight = new JLabel(new ImageIcon(piecesImg[2]));
        bishop = new JLabel(new ImageIcon(piecesImg[1]));
    }
    
    private void createPanel(){

        this.setBorder(new MatteBorder(5, 5, 0, 5, Colors.borderColor));

        this.add(queen);
        this.add(rook);
        this.add(knight);
        this.add(bishop);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Colors.promotionBackground);
    }
}
