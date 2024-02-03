package chess.Frame;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.GameUpdate;
import chess.Inputs.BottomInput;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Bottom extends JPanel {

    private JLabel arrowLeft;
    private JLabel arrowRight;
    private JLabel surrender;
    private JLabel newGame;
    private JLabel rotate;
    private BufferedImage rotateImg;

    Bottom(GameUpdate gameUpdate) {
        importImg();
        rotateImg = colorImg(rotateImg);
        this.setBackground(Colors.backgroundColor);
        double heightFactor = (double) 140 /1080;
        this.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * heightFactor)));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        newGame = new JLabel("New");
        newGame.setFont(new Font((String) null, Font.BOLD, (int) (Game.screenHeight*0.055)));
        newGame.setHorizontalAlignment(JLabel.CENTER);
        newGame.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double)250/1920)), 0));
        newGame.setForeground(Colors.foregroundColor);
        newGame.setBorder(new LineBorder(Colors.borderColor, 2));

        arrowLeft = new JLabel("<");
        arrowLeft.setFont(new Font(null, Font.BOLD, (int) (Game.screenHeight*0.055)));
        arrowLeft.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double)100/1920)), 0));
        arrowLeft.setHorizontalAlignment(JLabel.CENTER);
        arrowLeft.setForeground(Colors.foregroundColor);
        arrowLeft.setBorder(new MatteBorder(2, 0, 2, 2, Colors.borderColor));

        arrowRight = new JLabel(">");
        arrowRight.setFont(new Font(null, Font.BOLD, (int) (Game.screenHeight*0.055)));
        arrowRight.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double)100/1920)), 0));
        arrowRight.setHorizontalAlignment(JLabel.CENTER);
        arrowRight.setForeground(Colors.foregroundColor);
        arrowRight.setBorder(new MatteBorder(2, 0, 2, 0, Colors.borderColor));

        surrender = new JLabel("X");
        surrender.setFont(new Font(null, Font.BOLD, (int) (Game.screenHeight*0.055)));
        surrender.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double)250/1920)), 0));
        surrender.setHorizontalAlignment(JLabel.CENTER);
        surrender.setForeground(Colors.foregroundColor);
        surrender.setBorder(new LineBorder(Colors.borderColor, 2));

        rotate = new JLabel();
        rotate.setIcon(new ImageIcon(rotateImg));
        rotate.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double)100/1920)), surrender.getHeight()));
        rotate.setHorizontalAlignment(JLabel.CENTER);

        this.add(Box.createHorizontalGlue());
        this.add(newGame);
        this.add(arrowLeft);
        this.add(rotate);
        this.add(arrowRight);
        this.add(surrender);
        this.add(Box.createHorizontalGlue());
        this.addMouseListener(new BottomInput(arrowLeft, arrowRight, surrender, newGame, rotate, gameUpdate));
    }

    private void importImg() {

        InputStream is = getClass().getResourceAsStream("/main/ressources/Rotate.png");

        try {
            rotateImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Colors.backgroundColor);

        newGame.setForeground(Colors.foregroundColor);
        newGame.setBorder(new LineBorder(Colors.borderColor, 2));

        arrowLeft.setForeground(Colors.foregroundColor);
        arrowLeft.setBorder(new MatteBorder(2, 0, 2, 2, Colors.borderColor));

        arrowRight.setForeground(Colors.foregroundColor);
        arrowRight.setBorder(new MatteBorder(2, 2, 2, 0, Colors.borderColor));

        surrender.setForeground(Colors.foregroundColor);
        surrender.setBorder(new LineBorder(Colors.borderColor, 2));

        rotate.setIcon(new ImageIcon(colorImg(rotateImg)));
    }

    private BufferedImage colorImg(BufferedImage img) {
        Color color = Colors.foregroundColor;
        Image image = img.getScaledInstance((int) (Game.screenHeight * ((double) 80/1080)), (int) (Game.screenHeight * ((double) 80/1080)), Image.SCALE_SMOOTH);

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                int pixel = bufferedImage.getRGB(j, i);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha < 21) {
                    // pixel is transparent
                } else {
                    bufferedImage.setRGB(j, i, color.getRGB());
                }
            }
        }

        return bufferedImage;

    }
}
