package chess.Logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.imageio.ImageIO;

public class Ressources {
    public static BufferedImage pieces;
    public static BufferedImage piecesSmall;

    public static void importPieces() {
        try {
            InputStream inputStream = Ressources.class.getClassLoader().getResourceAsStream("main/ressources/Pieces.png");
            pieces = ImageIO.read(inputStream);
            InputStream inputStream1 = Ressources.class.getClassLoader().getResourceAsStream("main/ressources/PiecesSmall.png");
            piecesSmall = ImageIO.read(inputStream1);
//            pieces = ImageIO.read(Objects.requireNonNull(Ressources.class.getResource("Chess/src/main/ressources/Pieces.png")));
//            piecesSmall = ImageIO.read(Objects.requireNonNull(Ressources.class.getResource("Chess/src/main/ressources/PiecesSmall.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
