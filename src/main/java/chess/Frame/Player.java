package chess.Frame;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import chess.Logic.Colors;
import chess.Logic.Ressources;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends JPanel {
    private BufferedImage img;
    private BufferedImage[] piecesImg;
    private JPanel piecePanel;
    private JPanel pawns;
    private JPanel lightPieces;
    private JPanel heavyPieces;
    private JPanel player;
    public JLabel text;

    public Player(int i) {
        this.setPreferredSize(new Dimension(560, 200));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Colors.backgroundColor);

        piecesImg = new BufferedImage[12];
        img = Ressources.piecesSmall;
        addImg();

        setUpPlayerPanel();
        setUpPanel();
    }

    private void setUpPlayerPanel() {
        player = new JPanel();
        player.setBackground(Colors.backgroundColor);
        player.setPreferredSize(new Dimension(0, 50));

        text = new JLabel();
        text.setForeground(Colors.foreground);
        text.setFont(new Font(null, Font.PLAIN, 40));
        player.add(text);

        this.add(player);
    }

    private void setUpPanel() {
        piecePanel = new JPanel();
        piecePanel.setLayout(new BoxLayout(piecePanel, BoxLayout.Y_AXIS));
        piecePanel.setPreferredSize(new Dimension(0, 150));
        piecePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        piecePanel.setBackground(Colors.sideColor);
        piecePanel.setBorder(new MatteBorder(4, 0, 0, 0, Colors.borderColor));
        addComponents();

        this.add(piecePanel);
    }

    private void addComponents() {
        pawns = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pawns.setPreferredSize(new Dimension(0, 50));
        pawns.setBackground(Colors.sideColor);
        lightPieces = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        lightPieces.setPreferredSize(new Dimension(0, 50));
        lightPieces.setBackground(Colors.sideColor);
        lightPieces.setBorder(new MatteBorder(2, 0, 2, 0, Colors.borderColor));
        heavyPieces = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        heavyPieces.setPreferredSize(new Dimension(0, 50));
        heavyPieces.setBackground(Colors.sideColor);


        piecePanel.add(pawns);
        piecePanel.add(lightPieces);
        piecePanel.add(heavyPieces);
    }

    private void addImg() {
        for (int i = 0, j = 0; i < 6; i++, j += 2) {
            piecesImg[j] = img.getSubimage(i * 85, 0, 85, 85);
            piecesImg[j + 1] = img.getSubimage(i * 85, 85, 85, 85);
        }
    }

    public void addPiece(int i) {
        JLabel piece;
        if (i == 10 || i == 11) {
            piece = new JLabel(new ImageIcon(piecesImg[i].getScaledInstance(
                    40, 40, Image.SCALE_SMOOTH)));
            piece.setBorder(new EmptyBorder(5, 0, 0, 0));
            pawns.add(piece);

        } else if (i >= 4 && i <= 7) {
            piece = new JLabel(new ImageIcon(piecesImg[i].getScaledInstance(
                    40, 40, Image.SCALE_SMOOTH)));
            piece.setBorder(new EmptyBorder(5, 0, 0, 0));
            lightPieces.add(piece);

        } else {
             piece = new JLabel(new ImageIcon(piecesImg[i].getScaledInstance(
                    40, 40, Image.SCALE_SMOOTH)));
                    piece.setBorder(new EmptyBorder(5, 0, 0, 0));
            heavyPieces.add(piece);
        }
    }

    public void clear() {
        pawns.removeAll();
        lightPieces.removeAll();
        heavyPieces.removeAll();
    }
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        this.setBackground(Colors.sideColor);

        piecePanel.setBackground(Colors.sideColor);
        piecePanel.setBorder(new MatteBorder(4, 0, 0, 0, Colors.borderColor));

        player.setBackground(Colors.backgroundColor);
        text.setForeground(Colors.foreground);

        pawns.setBackground(Colors.sideColor);
        lightPieces.setBackground(Colors.sideColor);
        lightPieces.setBorder(new MatteBorder(2, 0, 2, 0, Colors.borderColor));
        heavyPieces.setBackground(Colors.sideColor);
    }
}
