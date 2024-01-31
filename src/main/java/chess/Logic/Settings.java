package chess.Logic;

public class Settings {
    public static int color;
    public static String style;
    public static String time;
    public static int increment;
    public static int difficulty;
    public static boolean rotateBoard;

    public Settings(int color, String time, int increment, boolean rotateBoard, String style, int difficulty){
        Settings.color = color;
        Settings.time = time;
        Settings.increment = increment;
        Settings.rotateBoard = rotateBoard;
        Settings.style = style;
        Settings.difficulty = difficulty;
    }
    public static void setColor(int color){
        Settings.color = color;
    }
    public static void setTime(String time){
        Settings.time = time;
    }
    public static void setIncrement(int increment){
        Settings.increment = increment;
    }
     public static void setRotateBoard(boolean rotateBoard){
        Settings.rotateBoard = rotateBoard;
    }
    public static void setStyle(String style){
        Settings.style = style;
    }
    public static void setDifficulty(int difficulty){
        Settings.difficulty = difficulty;
    }
}
