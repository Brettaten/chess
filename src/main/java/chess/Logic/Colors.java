package chess.Logic;

import java.awt.Color;

public class Colors {

    public static Color backgroundColor;
    public static Color menuBarColor;
    public static Color foregroundColor;
    public static Color foreground;
    public static Color sideColor;
    public static Color borderColor;
    public static Color backgroundCombo;
    public static Color colorSelektion;
    public static Color promotionBackground;
    public static Color disabledBackground;
    public static Color disabledText;

    public static Color fieldsBright;
    public static Color fieldsDark;
    public static Color selectedColor;
    public static Color lastMove;
    public static Color checked;
    public static Color marked;

    public static void changeTheme(int i) {
        switch (i) {
            case 0:
                backgroundColor = Color.BLACK;
                menuBarColor = Color.DARK_GRAY;
                foregroundColor = new Color(200, 200, 200); // foreground on dark surface bottom
                foreground = Color.WHITE; // foreground
                sideColor = Color.GRAY;
                borderColor = Color.WHITE;
                backgroundCombo = Color.BLACK;
                colorSelektion = Color.GRAY;
                promotionBackground = Color.LIGHT_GRAY;
                disabledBackground = new Color(40,40,40);
                disabledText = new Color(100,100,100);

                fieldsBright = new Color(200, 200, 200);
                fieldsDark = new Color(80, 80, 80);
                selectedColor = new Color(30, 150, 110);
                lastMove = new Color(0, 250, 100, 220);
                checked = new Color(220, 30, 30, 220);
                marked = new Color(255,255,255, 240);
                break;
            case 1:
                backgroundColor = new Color(200, 200, 200);
                menuBarColor = new Color(150, 150, 150);
                foregroundColor = Color.GRAY;
                foreground = Color.WHITE;
                sideColor = new Color(180, 180, 180);
                borderColor = Color.WHITE;
                backgroundCombo = Color.BLACK;
                colorSelektion = Color.GRAY;
                promotionBackground = new Color(200, 200, 200);
                disabledBackground = new Color(40,40,40);
                disabledText = new Color(100,100,100);

                fieldsBright = new Color(220, 220, 220);
                fieldsDark = new Color(160, 160, 160);
                selectedColor = new Color(60, 180, 140);
                lastMove = new Color(40, 255, 150, 200);
                checked = new Color(255, 60, 60, 200);
                break;
            case 2:
                backgroundColor = new Color(50, 50, 240);
                menuBarColor = new Color(150, 150, 150);
                foregroundColor = Color.WHITE;
                foreground = Color.WHITE;
                sideColor = new Color(110, 110, 255);
                borderColor = Color.WHITE;
                backgroundCombo = Color.BLACK;
                colorSelektion = Color.GRAY;
                promotionBackground = new Color(50, 50, 240);
                disabledBackground = new Color(40,40,40);
                disabledText = new Color(100,100,100);

                fieldsBright = new Color(70, 70, 255);
                fieldsDark = new Color(20, 20, 150);
                selectedColor = new Color(20, 140, 200);
                lastMove = new Color(50, 255, 200, 200);
                checked = new Color(225, 40, 40, 200);
                break;
            case 3:
                backgroundColor = new Color(130, 255, 130);
                menuBarColor = new Color(150, 150, 150);
                foregroundColor = Color.WHITE;
                foreground = Color.WHITE;
                sideColor = new Color(80, 255, 80);
                borderColor = Color.WHITE;
                backgroundCombo = Color.BLACK;
                colorSelektion = Color.GRAY;
                promotionBackground = new Color(130, 255, 130);
                disabledBackground = new Color(40,40,40);
                disabledText = new Color(100,100,100);

                fieldsBright = new Color(0, 255, 0);
                fieldsDark = new Color(0, 150, 0);
                selectedColor = new Color(0, 100, 150);
                lastMove = new Color(50, 180, 255, 200);
                checked = new Color(255, 60, 60, 200);
                break;
            case 4:
                backgroundColor = new Color(255, 130, 130);
                menuBarColor = new Color(150, 150, 150);
                foregroundColor = Color.WHITE;
                foreground = Color.WHITE;
                sideColor = new Color(255, 80, 80);
                borderColor = Color.WHITE;
                backgroundCombo = Color.BLACK;
                colorSelektion = Color.GRAY;
                promotionBackground = new Color(255, 130, 130);
                disabledBackground = new Color(40,40,40);
                disabledText = new Color(100,100,100);

                fieldsBright = new Color(255, 0, 0);
                fieldsDark = new Color(150, 0, 0);
                selectedColor = new Color(20, 160, 120);
                lastMove = new Color(60, 255, 170, 200);
                checked = new Color(120, 120, 255, 150);
                break;
        }
    }
}
