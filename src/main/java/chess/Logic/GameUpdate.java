package chess.Logic;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import chess.Frame.*;

public class GameUpdate {
    public boolean frontAI;
    private boolean color;
    private boolean colorFixed;
    public boolean rotateBoard;
    public boolean rotateSelected;
    public boolean rotateEnd;
    public boolean isEnd;
    public boolean hasCastledPlayer1;
    public boolean hasCastledPlayer0;
    public int counterEnd;
    public boolean fakeRotate;
    private boolean overrideFront;
    private boolean overrideHistory;
    private int promX;
    private int promY;
    private boolean promotionFront;
    private boolean isMovePiece;
    private int lastSelectedX;
    private int lastSelectedY;
    public int currentMove;
    public int maxMove;
    public boolean isPromoting;
    public boolean isSelecting;
    public boolean wasPromoting;
    public String[][] pieces;
    String[][] piecesAI;
    private ArrayList<String[][]> history1;
    private ArrayList<String[][]> history0;
    int[][] selectedFields;
    public int[] lastMove;
    public ArrayList<Integer> selectedList = new ArrayList<>();
    public ArrayList<int[]> moveList1 = new ArrayList<>();
    public ArrayList<int[]> moveList0 = new ArrayList<>();
    public ArrayList<Boolean> frontPlayer = new ArrayList<>();
    private ArrayList<Integer[][]> typeList = new ArrayList<>();
    private ArrayList<int[]> markedFields = new ArrayList<>();
    private ArrayList<int[]> checks = new ArrayList<>();
    public boolean settingsActive;
    public boolean rotate;
    private boolean isAI;
    private ThreadLocal<Integer> currentColor = new ThreadLocal<>();
    public String castle;
    public String enPassent;
    public int currentPlayer;
    public int selectedX;
    public int selectedY;
    public String promotion = null;
    public boolean surrender;
    public boolean isSelected;
    public boolean isDragged;
    public int draggType;
    public int draggPosX;
    public int draggPosY;
    public boolean front;
    public GameTimer gameTimer1;
    public GameTimer gameTimer2;
    public Player player1;
    public Player player2;
    private double remisCounter;
    public MenuBar menuBar;

    private Top top;

    public GameUpdate(boolean color, int currentPlayer) {
        applyChanges();
        colorFixed = this.color;
        this.isEnd = false;
        castle = "1111";
        enPassent = null;
        isPromoting = false;
        wasPromoting = false;
        isSelecting = false;
        overrideFront = false;
        isMovePiece = false;
        hasCastledPlayer1 = false;
        hasCastledPlayer0 = false;
        overrideHistory = false;
        currentMove = 0;
        rotateBoard = Settings.rotateBoard;
        maxMove = 0;
        settingsActive = false;
        lastSelectedX = -1;
        lastSelectedY = -1;
        remisCounter = 0.0;
        this.currentPlayer = currentPlayer;
        this.isSelected = false;
        this.rotate = false;
        this.isDragged = false;
        this.draggType = -1;
        this.draggPosX = -1;
        this.draggPosY = -1;
        this.currentColor.set(currentPlayer);
        player1 = Game.player1;
        player2 = Game.player2;

        if (this.color == false) {
            front = false;
        } else {
            front = true;
        }

        pieces = new String[8][8];
        lastMove = new int[4];

        fillArray();
        setUpPlayer();
        history1 = new ArrayList<>();
        history0 = new ArrayList<>();
        checks.add(null);
        addTypeList();
        surrender = false;
        if (Settings.rotateBoard) {
            setBoardRotation(Settings.rotateBoard);
        }
        if (Settings.style == "Player vs. AI") {
            new Thread(() -> {
                nextMove();
            }).start();
        }
    }

    private void addTypeList() {
        Integer[][] s = new Integer[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    s[i][j] = getType(i, j, pieces);
                } else {
                    s[i][j] = -1;
                }
            }
        }
        typeList.add(s);
    }

    public String[][] fillArray() {
        if (color == true) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {

                    if (i == 0) {
                        if (j == 0 || j == 7) {
                            pieces[i][j] = "900";
                            pieces[7][j] = "810";
                        } else if (j == 1 || j == 6) {
                            pieces[i][j] = "70";
                            pieces[7][j] = "61";
                        } else if (j == 2 || j == 5) {
                            pieces[i][j] = "50";
                            pieces[7][j] = "41";
                        } else if (j == 3) {
                            pieces[i][j] = "30";
                            pieces[7][j] = "21";
                        } else if (j == 4) {
                            pieces[i][j] = "100";
                            pieces[7][j] = "010";
                        }
                    } else {
                        pieces[i][j] = "1100";
                        pieces[6][j] = "1010";
                    }
                }
            }
        } else {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {

                    if (i == 0) {
                        if (j == 0 || j == 7) {
                            pieces[i][j] = "810";
                            pieces[7][j] = "900";
                        } else if (j == 1 || j == 6) {
                            pieces[i][j] = "61";
                            pieces[7][j] = "70";
                        } else if (j == 2 || j == 5) {
                            pieces[i][j] = "41";
                            pieces[7][j] = "50";
                        } else if (j == 4) {
                            pieces[i][j] = "21";
                            pieces[7][j] = "30";
                        } else if (j == 3) {
                            pieces[i][j] = "010";
                            pieces[7][j] = "100";
                        }
                    } else {
                        pieces[i][j] = "1010";
                        pieces[6][j] = "1100";
                    }
                }
            }
        }
        return pieces;
    }

    public boolean isSelected(int posX, int posY) {
        if (selectedList == null) {
            return false;
        } else {
            for (int i = 0; i < selectedList.size(); i += 2) {
                if (selectedList.get(i) == posX && selectedList.get(i + 1) == posY) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLastMove(int posX, int posY) {
        if (moveList1.isEmpty() || (currentMove == 0 && moveList1.size() > 0)) {
            return false;
        }
        // if (currentMove == maxMove && isEnd == true && color == false) {
        // fakeRotate = true;
        // }
        for (int i = 0; i < 4; i += 2) {
            if (detectFront()) {
                if (rotateSelected == true && posX == rotateEnpassent(moveList0.get(currentMove - 1)[i])
                        && posY == rotateEnpassent(moveList0.get(currentMove - 1)[i + 1])) {
                    return true;
                }

                else if (rotateSelected == false && posX == moveList1.get(currentMove - 1)[i]
                        && posY == moveList1.get(currentMove - 1)[i + 1]) {
                    return true;
                }
            } else {
                if (rotateSelected == true && posX == rotateEnpassent(moveList1.get(currentMove - 1)[i])
                        && posY == rotateEnpassent(moveList1.get(currentMove - 1)[i + 1])) {
                    return true;
                }

                else if (rotateSelected == false && posX == moveList0.get(currentMove - 1)[i]
                        && posY == moveList0.get(currentMove - 1)[i + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isClear(int posX, int posY, int pieceColor, String[][] pieces) {

        if ((posX >= 8 || posY >= 8) || (posX < 0 || posY < 0)) {
            return false;
        }
        if (pieces[posX][posY] == null) {
            return true;
        } else if (getColor(posX, posY, pieces) != pieceColor || pieceColor == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAttacked(int posX, int posY, int pieceColor, String[][] pieces) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null && pieceColor != getColor(i, j, pieces)) {
                    Pieces piece = new Pieces(i, j, getColor(i, j, pieces), getType(i, j, pieces), color,
                            Game.gameUpdate, 0);
                    ArrayList<Integer> list = piece.possibleMoves(i, j, 1, pieces);

                    for (int k = 0; k < list.size(); k += 2) {
                        if (list.get(k) == posX && list.get(k + 1) == posY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

     boolean isNull(int posX, int posY, String[][] pieces) {

        if (pieces[posX][posY] == null) {
            return true;
        } else {
            return false;
        }
    }

    public  int getType(int posX, int posY, String[][] pieces) {
        if(pieces[posX][posY] == null){
            return -1;
        }

        if (pieces[posX][posY].length() == 4) {
            return Integer.parseInt(pieces[posX][posY].substring(0, 2));
        } else {
            return Character.getNumericValue(pieces[posX][posY].charAt(0));
        }
    }

    public  int getColor(int posX, int posY, String[][] pieces) {
            if (pieces[posX][posY].length() == 4) {
                return Character.getNumericValue(pieces[posX][posY].charAt(2));
            } else {
                return Character.getNumericValue(pieces[posX][posY].charAt(1));
            }

    }

     String castlePossible(int posX, int posY, int pieceColor, String[][] pieces) {

        String[] arr = new String[2];

        if (detectFront()) {
            if ((color == true && getCurrentPlayer() == 1) || (color == false && getCurrentPlayer() == 0)
                    || rotateBoard == true) {
                if (pieces[7][4] != null && (pieces[7][4].charAt(0) == '0' || pieces[7][4].charAt(0) == '1')
                        && pieces[7][4].charAt(2) == '0'
                        && Character.getNumericValue(pieces[7][4].charAt(1)) == getCurrentPlayer()
                        && isAttacked(posX, posY, getCurrentPlayer(), pieces) == false) {
                    if (pieces[7][0] != null && (pieces[7][0].charAt(0) == '8' || pieces[7][0].charAt(0) == '9')
                            && pieces[7][0].charAt(2) == '0'
                            && Character.getNumericValue(pieces[7][0].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[7][1] == null && isAttacked(7, 1, getCurrentPlayer(), pieces) == false
                                && pieces[7][2] == null && isAttacked(7, 2, getCurrentPlayer(), pieces) == false
                                && pieces[7][3] == null && isAttacked(7, 3, getCurrentPlayer(), pieces) == false) {
                            arr[0] = "1";
                        }
                    }
                    if (pieces[7][7] != null && (pieces[7][7].charAt(0) == '8' || pieces[7][7].charAt(0) == '9')
                            && pieces[7][7].charAt(2) == '0'
                            && Character.getNumericValue(pieces[7][7].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[7][6] == null && isAttacked(7, 6, getCurrentPlayer(), pieces) == false
                                && pieces[7][5] == null && isAttacked(7, 5, getCurrentPlayer(), pieces) == false) {
                            arr[1] = "1";
                        }
                    }
                }
            } else {
                if (pieces[0][4] != null && (pieces[0][4].charAt(0) == '0' || pieces[0][4].charAt(0) == '1')
                        && pieces[0][4].charAt(2) == '0'
                        && Character.getNumericValue(pieces[0][4].charAt(1)) == getCurrentPlayer()
                        && isAttacked(posX, posY, getCurrentPlayer(), pieces) == false) {
                    if (pieces[0][0] != null && (pieces[0][0].charAt(0) == '8' || pieces[0][0].charAt(0) == '9')
                            && pieces[0][0].charAt(2) == '0'
                            && Character.getNumericValue(pieces[0][0].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[0][1] == null && isAttacked(0, 1, getCurrentPlayer(), pieces) == false
                                && pieces[0][2] == null && isAttacked(0, 2, getCurrentPlayer(), pieces) == false
                                && pieces[0][3] == null && isAttacked(0, 3, getCurrentPlayer(), pieces) == false) {
                            arr[0] = "1";
                        }
                    }
                    if (pieces[0][7] != null && (pieces[0][7].charAt(0) == '8' || pieces[0][7].charAt(0) == '9')
                            && pieces[0][7].charAt(2) == '0'
                            && Character.getNumericValue(pieces[0][7].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[0][6] == null && isAttacked(0, 6, getCurrentPlayer(), pieces) == false
                                && pieces[0][5] == null && isAttacked(0, 5, getCurrentPlayer(), pieces) == false) {
                            arr[1] = "1";
                        }
                    }
                }
            }
        } else {
            if ((color == true && getCurrentPlayer() == 1) || (color == false && getCurrentPlayer() == 0)
                    || rotateBoard == true) {
                if (pieces[7][3] != null && (pieces[7][3].charAt(0) == '0' || pieces[7][3].charAt(0) == '1')
                        && pieces[7][3].charAt(2) == '0'
                        && Character.getNumericValue(pieces[7][3].charAt(1)) == getCurrentPlayer()
                        && isAttacked(posX, posY, getCurrentPlayer(), pieces) == false) {
                    if (pieces[7][7] != null && (pieces[7][7].charAt(0) == '8' || pieces[7][7].charAt(0) == '9')
                            && pieces[7][7].charAt(2) == '0'
                            && Character.getNumericValue(pieces[7][7].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[7][6] == null && isAttacked(7, 6, getCurrentPlayer(), pieces) == false
                                && pieces[7][5] == null && isAttacked(7, 5, getCurrentPlayer(), pieces) == false
                                && pieces[7][4] == null && isAttacked(7, 4, getCurrentPlayer(), pieces) == false) {
                            arr[1] = "1";
                        }
                    }
                    if (pieces[7][0] != null && (pieces[7][0].charAt(0) == '8' || pieces[7][0].charAt(0) == '9')
                            && pieces[7][0].charAt(2) == '0'
                            && Character.getNumericValue(pieces[7][0].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[7][1] == null && isAttacked(7, 1, getCurrentPlayer(), pieces) == false
                                && pieces[7][2] == null && isAttacked(7, 2, getCurrentPlayer(), pieces) == false) {
                            arr[0] = "1";
                        }
                    }
                }
            } else {
                if (pieces[0][3] != null && (pieces[0][3].charAt(0) == '0' || pieces[0][3].charAt(0) == '1')
                        && pieces[0][3].charAt(2) == '0'
                        && Character.getNumericValue(pieces[0][3].charAt(1)) == getCurrentPlayer()
                        && isAttacked(posX, posY, getCurrentPlayer(), pieces) == false) {
                    if (pieces[0][7] != null && (pieces[0][7].charAt(0) == '8' || pieces[0][7].charAt(0) == '9')
                            && pieces[0][7].charAt(2) == '0'
                            && Character.getNumericValue(pieces[0][7].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[0][6] == null && isAttacked(0, 6, getCurrentPlayer(), pieces) == false
                                && pieces[0][5] == null && isAttacked(0, 5, getCurrentPlayer(), pieces) == false
                                && pieces[0][4] == null && isAttacked(0, 4, getCurrentPlayer(), pieces) == false) {
                            arr[1] = "1";
                        }
                    }
                    if (pieces[0][0] != null && (pieces[0][0].charAt(0) == '8' || pieces[0][0].charAt(0) == '9')
                            && pieces[0][0].charAt(2) == '0'
                            && Character.getNumericValue(pieces[0][0].charAt(1)) == getCurrentPlayer()) {
                        if (pieces[0][1] == null && isAttacked(0, 1, getCurrentPlayer(), pieces) == false
                                && pieces[0][2] == null && isAttacked(0, 2, getCurrentPlayer(), pieces) == false) {
                            arr[0] = "1";
                        }
                    }
                }
            }
        }
        String s = "";
        if (arr[0] != null) {
            s += 1;
            if (arr[1] != null) {
                s += 1;
            } else {
                s += 0;
            }
        } else {
            s += 0;
            if (arr[1] != null) {
                s += 1;
            } else {
                s += 0;
            }
        }
        return s;
    }

     public boolean enemy(int currentPosX, int currentPosY, int posX, int posY, int pieceColor, String[][] pieces) {
        if (pieces[posX][posY] == null) {
            return false;
        }
        if (pieces[posX][posY].length() == 4) {
            if (Character.getNumericValue(pieces[posX][posY].charAt(2)) == pieceColor) {
                return false;
            } else {
                return true;
            }
        } else {
            if (Character.getNumericValue(pieces[posX][posY].charAt(1)) == pieceColor) {
                return false;
            } else {
                return true;
            }
        }
    }

    boolean pawnMoved(int posX, int posY, String[][] pieces) {

        if (Character.getNumericValue(pieces[posX][posY].charAt(3)) == 0) {
            return false;
        } else {
            return true;
        }
    }

    boolean isChecked(int currentPosX, int currentPosY, int posX, int posY, int type, boolean enPassent, String[][] pieces) {

//        String[][] pieces = getPieces();

        int kingX = 0;
        int kingY = 0;

        if (getType(currentPosX, currentPosY, pieces) == 0 || getType(currentPosX, currentPosY, pieces) == 1) {
            kingX = posX;
            kingY = posY;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((pieces[i][j] != null && getType(i, j, pieces) == 0 && getColor(i, j, pieces) == getCurrentPlayer())
                            || (pieces[i][j] != null && getType(i, j, pieces) == 1
                                    && getColor(i, j, pieces) == getCurrentPlayer())) {
                        kingX = i;
                        kingY = j;
                    }
                }
            }
        }

        String[][] pieces1 = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces1[i][j] = pieces[i][j];
            }
        }
        if (enPassent) {
            int y1 = Character.getNumericValue(this.enPassent.charAt(1));
            int y2 = Character.getNumericValue(this.enPassent.charAt(3));
            if (y1 > y2) {
                pieces1[posX][y2 + 1] = null;
            } else {
                pieces1[posX][y1 + 1] = null;
            }
        }
        pieces1[posX][posY] = pieces1[currentPosX][currentPosY];
        pieces1[currentPosX][currentPosY] = null;
        if (isAttacked(kingX, kingY, getCurrentPlayer(), pieces1)) {
            return true;
        } else {
            return false;
        }
    }

     public int isEnd() {
        String[][] pieces = getPieces();
        if (isRemis()) {
            return 2;
        }

        int kingX = 0;
        int kingY = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    if (pieces[i][j] != null && (getType(i, j, pieces) == 0
                            || getType(i, j, pieces) == 1) && getColor(i, j, pieces) == currentPlayer) {
                        kingX = i;
                        kingY = j;
                    }
                }
            }
        }
        if (isAttacked(kingX, kingY, currentPlayer, pieces)) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (pieces[i][j] != null && getColor(i, j, pieces) == currentPlayer) {
                        Pieces piece = new Pieces(i, j, currentPlayer, getType(i, j, pieces), color, Game.gameUpdate, 0);
                        ArrayList<Integer> list = new ArrayList<>();
                        list = piece.possibleMoves(i, j, 0, pieces);
                        if (list.isEmpty() == false) {
                            return 0;
                        }
                    }
                }
            }
            return 1;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (pieces[i][j] != null && getColor(i, j, pieces) == currentPlayer) {
                        Pieces piece = new Pieces(i, j, currentPlayer, getType(i, j, pieces), color, Game.gameUpdate, 0);
                        ArrayList<Integer> list = new ArrayList<>();
                        list = piece.possibleMoves(i, j, 0, pieces);
                        if (list.isEmpty() == false) {
                            return 0;
                        }
                    }
                }
            }
            return 2;
        }
    }

    private boolean isRemis() {
        if (remisCounter >= 50) {
            return true;
        }
        if (moveRepetition()) {
            return true;
        }
        if (enoughPieces() == false) {
            return true;
        }
        return false;
    }

    private boolean enoughPieces() {
        Integer[][] arr = typeList.get(typeList.size() - 1);
        int lightCounter0 = 0;
        int lightCounter1 = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (arr[i][j] == 8 || arr[i][j] == 9 || arr[i][j] == 2 || arr[i][j] == 3 || arr[i][j] == 11
                        || arr[i][j] == 10) {
                    return true;
                }
                if (arr[i][j] == 5 || arr[i][j] == 7) {
                    lightCounter0++;
                    if (lightCounter0 == 2) {
                        return true;
                    }
                }
                if (arr[i][j] == 4 || arr[i][j] == 6) {
                    lightCounter1++;
                    if (lightCounter1 == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean moveRepetition() {
        int counter;

        for (int i = 0; i < typeList.size(); i++) {
            counter = 0;
            for (int j = 0; j < typeList.size(); j++) {
                if (Arrays.deepEquals(typeList.get(i), typeList.get(j))) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckedAfter(int posX, int posY) {

        if(!isMovePiece && checks.get(currentMove) != null && posX == checks.get(currentMove)[0] && posY == checks.get(currentMove)[1]){
            return true;
        }
        return false;
    }

    public void selectPiece(int posX, int posY) {

        selectedList.clear();
        int type;

        selectedX = posX;
        selectedY = posY;

        if (pieces[posX][posY] != null && getColor(posX, posY, pieces) == currentPlayer
                && (lastSelectedX != posX || lastSelectedY != posY) || isDragged == true) {
            lastSelectedX = posX;
            lastSelectedY = posY;
            if (pieces[posX][posY].length() == 4) {
                type = Integer.parseInt(pieces[posX][posY].substring(0, 2));
            } else {
                type = Character.getNumericValue(pieces[posX][posY].charAt(0));
            }

            Pieces piece = new Pieces(posX, posY, currentPlayer, type, color, Game.gameUpdate, 0);
            selectedList = piece.possibleMoves(posX, posY, 0, pieces);
        } else {
            lastSelectedX = -1;
            lastSelectedY = -1;
        }
    }

    public void movePiece(int posX, int posY, int selectedX, int selectedY, int promotionPiece) {
        isMovePiece = true;
        if(gameTimer1 == null){
            setUpTimer();
        }

        if(currentMove != maxMove){
            pieces = deepCopy(history1.get(maxMove));
            currentMove = maxMove;
        }

        lastMove[0] = posX;
        lastMove[1] = posY;
        lastMove[2] = selectedX;
        lastMove[3] = selectedY;

        int[] move = { posX, posY, selectedX, selectedY };

        if (overrideHistory == true) {
            if (detectFront()) {
                history1.set(history1.size() - 1, deepCopy(pieces));
                history0.set(history0.size() - 1, rotateArray(deepCopy(pieces)));
            } else {
                history1.set(history1.size() - 1, rotateArray(deepCopy(pieces)));
                history0.set(history0.size() - 1, deepCopy(pieces));
            }
            overrideHistory = false;
        } else {
            if (detectFront()) {
                history1.add(deepCopy(pieces));
                history0.add(rotateArray(deepCopy(pieces)));
            } else {
                history0.add(deepCopy(pieces));
                history1.add(rotateArray(deepCopy(pieces)));
            }
        }

        if (rotateBoard == true) {
            if (overrideFront == true) {
                frontPlayer.set(frontPlayer.size() - 1, true);
            } else {
                frontPlayer.add(true);
            }
            overrideFront = false;
        } else {
            if (overrideFront == true) {
                frontPlayer.set(frontPlayer.size() - 1, false);
            } else {
                frontPlayer.add(false);
            }
            overrideFront = false;
        }

        if ((getType(selectedX, selectedY, pieces) == 1 || getType(selectedX, selectedY, pieces) == 0)
                && (posY == selectedY - 2 || posY == selectedY + 2)) {
            if(currentPlayer == 1){
                hasCastledPlayer1 = true;

            }
            else{
                hasCastledPlayer0 = true;
            }
            if (detectFront()) {
                if (selectedX == 0) {
                    if (posY == 6) {
                        pieces[0][5] = pieces[0][7];
                        pieces[0][7] = null;
                    } else {
                        pieces[0][3] = pieces[0][0];
                        pieces[0][0] = null;
                    }
                } else {
                    if (posY == 6) {
                        pieces[7][5] = pieces[7][7];
                        pieces[7][7] = null;
                    } else {
                        pieces[7][3] = pieces[7][0];
                        pieces[7][0] = null;
                    }
                }
            } else {
                if (selectedX == 0) {
                    if (posY == 1) {
                        pieces[0][2] = pieces[0][0];
                        pieces[0][0] = null;
                    } else {
                        pieces[0][4] = pieces[0][7];
                        pieces[0][7] = null;
                    }
                } else {
                    if (posY == 1) {
                        pieces[7][2] = pieces[7][0];
                        pieces[7][0] = null;
                    } else {
                        pieces[7][4] = pieces[7][7];
                        pieces[7][7] = null;
                    }
                }
            }
            if (posY == 6) {
                move[1] = posY + 1;
            } else if (posY == 1) {
                move[1] = posY - 1;
            } else if (posY == 2) {
                move[1] = posY - 2;
            } else {
                move[1] = posY + 2;
            }
            pieces[selectedX][selectedY] = getType(selectedX, selectedY, pieces) + String.valueOf(currentPlayer) + "1";
        }

        if ((getType(selectedX, selectedY, pieces) == 11 || getType(selectedX, selectedY, pieces) == 10)
                && (posX == 7 || posX == 0)) {
            ;
        } else {
            if (detectFront()) {
                moveList1.add(move);
                moveList0.add(rotateArr(move));
            } else {
                moveList0.add(move);
                moveList1.add(rotateArr(move));
            }
            if(currentMove == maxMove){
                currentMove++;
            }
            maxMove++;
        }

        if (getType(selectedX, selectedY, pieces) == 1 || getType(selectedX, selectedY, pieces) == 0) {
            pieces[selectedX][selectedY] = getType(selectedX, selectedY, pieces) + String.valueOf(currentPlayer) + "1";
        }

        if ((getType(selectedX, selectedY, pieces) == 10 || getType(selectedX, selectedY, pieces) == 11)
                && posY != selectedY
                && enemy(selectedX, selectedY, posX, posY, currentPlayer, pieces) == false) {

            if (front == true && currentPlayer == 1 || front == false && currentPlayer == 0) {
                if (colorFixed == true && currentPlayer == 1 || colorFixed == false && currentPlayer == 0) {
                    player1.addPiece(getType(posX + 1, posY, pieces));
                    pieces[posX + 1][posY] = null;
                } else {
                    player2.addPiece(getType(posX + 1, posY, pieces));
                    pieces[posX + 1][posY] = null;
                }
            } else {
                if (colorFixed == true && currentPlayer == 1 || colorFixed == false && currentPlayer == 0) {
                    player1.addPiece(getType(posX - 1, posY, pieces));
                    pieces[posX - 1][posY] = null;
                } else {
                    player2.addPiece(getType(posX - 1, posY, pieces));
                    pieces[posX - 1][posY] = null;
                }
            }
        }

        if (getType(selectedX, selectedY, pieces) == 8 || getType(selectedX, selectedY, pieces) == 9) {
            pieces[selectedX][selectedY] = getType(selectedX, selectedY, pieces) + String.valueOf(currentPlayer) + "1";
        }

        if ((getType(selectedX, selectedY, pieces) == 11 || getType(selectedX, selectedY, pieces) == 10)
                && (posX == 7 || posX == 0)) {
                    int player1Piece = -1;
                    int player2Piece = -1;
            if (pieces[posX][posY] != null) { 
                if (currentPlayer == 1 && colorFixed == true || currentPlayer == 0 && colorFixed == false) {
                    player1Piece = getType(posX, posY, pieces);
                } else {
                    player2Piece = getType(posX, posY, pieces);
                }
            }
            if (pieces[posX][posY] == null && getType(selectedX, selectedY, pieces) != 10
                    && getType(selectedX, selectedY, pieces) != 11) {
                remisCounter += 0.5;
            } else {
                remisCounter = 0;
            }
            pieces[posX][posY] = pieces[selectedX][selectedY];
            pieces[selectedX][selectedY] = null;

            promX = posX;
            promY = posY;
            promotionFront = front;

            selectedList.clear();
            enPassent = null;
            isPromoting = true;
            isSelecting = true;
            if (detectFront()) {
                moveList1.add(move);
                moveList0.add(rotateArr(move));
            } else {
                moveList0.add(move);
                moveList1.add(rotateArr(move));
            }

            currentMove++;
            maxMove++;
            if(promotionPiece != -1){
                promotion(promotionPiece, promotionPiece, posX, posY, player1Piece, player2Piece);
            }
            else{
                Promotion promotion = new Promotion(color, currentPlayer, Game.gameUpdate, player1Piece, player2Piece);
                Game.frame.top.promtotion(promotion);
            }
        }

        else {
            if (pieces[posX][posY] != null) {
                if (currentPlayer == 1 && colorFixed == true || currentPlayer == 0 && colorFixed == false) {
                    player1.addPiece(getType(posX, posY, pieces));
                } else {
                    player2.addPiece(getType(posX, posY, pieces));
                }
            }
            if (pieces[posX][posY] == null && getType(selectedX, selectedY, pieces) != 10
                    && getType(selectedX, selectedY, pieces) != 11) {
                remisCounter += 0.5;
            } else {
                remisCounter = 0;
            }
            pieces[posX][posY] = pieces[selectedX][selectedY];
            pieces[selectedX][selectedY] = null;

            selectedList.clear();
            enPassent = null;

            addTypeList();

            if ((getType(posX, posY, pieces) == 11 || getType(posX, posY, pieces) == 10)) {
                pieces[posX][posY] = String.valueOf(getType(posX, posY, pieces)) + String.valueOf(currentPlayer) + "1";

                if (selectedX + 2 == posX || selectedX - 2 == posX) {
                    enPassent = "";
                    if (posY == 0) {
                        enPassent += "99";
                    } else {
                        enPassent += String.valueOf(rotateEnpassent(posX)) + String.valueOf(rotateEnpassent(posY - 1));
                    }
                    if (posY == 8) {
                        enPassent += "99";
                    } else {
                        enPassent += String.valueOf(rotateEnpassent(posX)) + String.valueOf(rotateEnpassent(posY + 1));
                    }
                }

            }

            if (currentPlayer == 0) {
                currentPlayer = 1;
            } else {
                currentPlayer = 0;
            }
            checks.add(getCheck(pieces));
            if (isEnd() == 0) {
                if (gameTimer1.isRunning()) {
                    if (gameTimer2.hasStarted()) {
                        gameTimer1.pause();
                        gameTimer2.resume();
                    } else {
                        gameTimer1.pause();
                        gameTimer2.start();
                    }
                } else if (gameTimer2.isRunning()) {
                    if (gameTimer1.hasStarted()) {
                        gameTimer2.pause();
                        gameTimer1.resume();
                    } else {
                        gameTimer2.pause();
                        gameTimer1.start();
                    }
                } else if (color == false && rotateBoard == false || color == false && rotateBoard == true) {
                    gameTimer1.start();
                } else {
                    gameTimer2.start();
                }
            }

            if (rotateBoard == true) {
                rotateBoard = false;
                if (isEnd() == 1) {
                    changeTop(1, currentPlayer);
                    if (currentPlayer == 0) {
                        currentPlayer = 1;
                    } else {
                        currentPlayer = 0;
                    }
                    rotateEnd = true;
                    isEnd = true;
                } else if (isEnd() == 2) {
                    changeTop(2, currentPlayer);
                    if (currentPlayer == 0) {
                        currentPlayer = 1;
                    } else {
                        currentPlayer = 0;
                    }
                    rotateEnd = true;
                    isEnd = true;
                }
                rotateBoard = true;

            } else {
                if (isEnd() == 1) {
                    changeTop(1, currentPlayer);
                    gameTimer1.setNull();
                    gameTimer2.setNull();

                    isEnd = true;
                } else if (isEnd() == 2) {
                    changeTop(2, currentPlayer);
                    gameTimer1.setNull();
                    gameTimer2.setNull();
                    isEnd = true;
                }
            }

            if (rotateBoard == true && isEnd == false) {
                rotateBoard();
            }
            lastSelectedX = -1;
            lastSelectedY = -1;
            isMovePiece = false;
            if (Settings.style == "Player vs. AI" && !isEnd) {
                new Thread(() -> {
                    nextMove();
                }).start();
            }
        }

    }

    private int[] getCheck(String[][] pieces) {
        int kingx = 0;
        int kingy = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(pieces[i][j] != null && (getType(i, j, pieces) == 1 || getType(i, j, pieces) == 0) && getColor(i, j, pieces) == currentPlayer){
                    kingx = i;
                    kingy = j;
                    break;
                }
            }
        }

        if(isAttacked(kingx, kingy, currentPlayer, pieces)){
            return new int[]{kingx, kingy};
        }
        else{
            return null;
        }
    }

    private void setUpTimer() {
        gameTimer1 = new GameTimer(Game.frame.getTimerLabel1());
        gameTimer2 = new GameTimer(Game.frame.getTimerLabel2());
    }

    public void changeTop(int i, int currentPlayer2) {
        if (i == 1) {
            if (currentPlayer == 1) {
                top.textLabel.setText("Black wins");
            } else {
                top.textLabel.setText("White wins");
            }
        } else if (i == 2) {
            top.textLabel.setText("Draw");
        } else {
            if (currentPlayer == 1) {
                top.textLabel.setText("Black wins");
            } else {
                top.textLabel.setText("White wins");
            }
            surrender = true;
        }
    }

    public String[][] getPieces() {
        if (!isAI) {
            return pieces;
        } else {
            return piecesAI;
        }
    }



    public void promotion(int x, int y, int posX, int posY, int player1Piece, int player2Piece) {
        isSelecting = false;
        isPromoting = false;
        
        if (promotionFront != front) {
            promX = rotateNum(promX);
            promY = rotateNum(promY);
        }
        if(player1Piece != -1){
            player1.addPiece(player1Piece);
        }
        else if(player2Piece != -1){
            player2.addPiece(player2Piece);
        }

        if (y == 0) {
            if (currentPlayer == 1) {
                pieces[promX][promY] = "2" + String.valueOf(currentPlayer);
            } else {
                pieces[promX][promY] = "3" + String.valueOf(currentPlayer);
            }
        } else if (y == 1) {
            if (currentPlayer == 1) {
                pieces[promX][promY] = "8" + String.valueOf(currentPlayer) + "1";
            } else {
                pieces[promX][promY] = "9" + String.valueOf(currentPlayer) + "1";
            }
        } else if (y == 2) {
            if (currentPlayer == 1) {
                pieces[promX][promY] = "6" + String.valueOf(currentPlayer);
            } else {
                pieces[promX][promY] = "7" + String.valueOf(currentPlayer);
            }
        } else if (y == 3) {
            if (currentPlayer == 1) {
                pieces[promX][promY] = "4" + String.valueOf(currentPlayer);
            } else {
                pieces[promX][promY] = "5" + String.valueOf(currentPlayer);
            }
        }
        setPromotionHistory();

        if (currentPlayer == 0) {
            currentPlayer = 1;
        } else {
            currentPlayer = 0;
        }
        checks.add(getCheck(pieces));

        if (isEnd() == 0) {
            if (gameTimer1.isRunning()) {
                if (gameTimer2.hasStarted()) {
                    gameTimer1.pause();
                    gameTimer2.resume();
                } else {
                    gameTimer1.pause();
                    gameTimer2.start();
                }
            } else if (gameTimer2.isRunning()) {
                if (gameTimer1.hasStarted()) {
                    gameTimer2.pause();
                    gameTimer1.resume();
                } else {
                    gameTimer2.pause();
                    gameTimer1.start();
                }
            } else if (color == false && rotateBoard == false || color == false && rotateBoard == true) {
                gameTimer1.start();
            } else {
                gameTimer2.start();
            }
         }

        if (rotateBoard == true) {
            rotateBoard = false;
            if (isEnd() == 1) {
                changeTop(1, currentPlayer);
                if (currentPlayer == 0) {
                    currentPlayer = 1;
                } else {
                    currentPlayer = 0;
                }
                isEnd = true;
            } else if (isEnd() == 2) {
                changeTop(2, currentPlayer);
                if (currentPlayer == 0) {
                    currentPlayer = 1;
                } else {
                    currentPlayer = 0;
                }
                isEnd = true;
            }
            rotateBoard = true;

        } else {
            if (isEnd() == 1) {
                changeTop(1, currentPlayer);
                isEnd = true;
            } else if (isEnd() == 2) {
                changeTop(2, currentPlayer);
                isEnd = true;
            }
        }

        if (rotateBoard == true && isEnd == false) {
            rotateBoard();
        }
        lastSelectedX = -1;
        lastSelectedY = -1;

         if (Settings.style == "Player vs. AI") {
            new Thread(() -> {
                nextMove();
            }).start();
        }
        isMovePiece = false;
    }

    private int rotateNum(int num) {
        int counter = 0;

        for (int i = 7; i > num; i--) {
            counter++;
        }
        return counter;
    }

    public void backOrForward(int i) {
        if(isSelecting){
            selectingHistory(i);
            return;
        }
        if (overrideFront == false) {
            if (rotateBoard == true) {
                frontPlayer.add(true);
            } else {
                frontPlayer.add(false);
            }
            overrideFront = true;
        }
        if (overrideHistory == false) {
            if (detectFront()) {
                history1.add(deepCopy(pieces));
                history0.add(rotateArray(deepCopy(pieces)));
            } else {
                history0.add(deepCopy(pieces));
                history1.add(rotateArray(deepCopy(pieces)));
            }
            overrideHistory = true;
        }

        if (i == 0 && currentMove >= 1) {
            if (detectFront()) {
                pieces = deepCopy(history1.get(currentMove - 1));
            } else {
                pieces = deepCopy(history0.get(currentMove - 1));
            }
            currentMove--;
            selectedList.clear();
        } else if (i == 1 && currentMove < maxMove) {

            if (detectFront()) {
                pieces = deepCopy(history1.get(currentMove + 1));
            } else {
                pieces = deepCopy(history0.get(currentMove + 1));
            }
            currentMove++;
        }
        if (currentMove == maxMove && isEnd == true) {
            fakeRotate = false;
        }
        if (currentMove == maxMove && isEnd == false) {
            checkPositon();
        }
    }

    private void selectingHistory(int i) {
         if (i == 0 && currentMove >= 1) {
            if (detectFront()) {
                pieces = deepCopy(history1.get(currentMove - 1));
            } else {
                pieces = deepCopy(history0.get(currentMove - 1));
            }
            currentMove--;
            selectedList.clear();
        } else if (i == 1 && currentMove < maxMove) {

            if (detectFront()) {
                pieces = deepCopy(history1.get(currentMove + 1));
            } else {
                pieces = deepCopy(history0.get(currentMove + 1));
            }
            currentMove++;
        }
        
        if(maxMove > currentMove){
            Game.frame.top.removePromotion();
            isPromoting = false;
            isSelecting = false;
            maxMove--;
            moveList0.remove(maxMove);
            moveList1.remove(maxMove);
            wasPromoting = true;

            if(maxMove+1 < history0.size()){
                history0.remove(maxMove+1);
                history1.remove(maxMove+1);
            }
        }
    }

    /**
     * lol
     */
    private void setPromotionHistory() {
        if(wasPromoting){
            history0.remove(history0.get(history0.size() - 1));
            history1.remove(history1.get(history1.size() - 1));
        }
        wasPromoting = false;
    }

    private void checkPositon() {

        if (rotateBoard == false && currentMove == maxMove
                && (front == true && colorFixed == false || front == false && colorFixed == true)) {
            if (color == true) {
                color = false;
            } else {
                color = true;
            }
        } else if (rotateBoard == true && currentMove == maxMove) {
            // rotateBoard = false;
            // rotateSelected = false;
            // fakeRotate = false;

            if ((front == true && colorFixed == false) || (front == false && colorFixed == true)) {
                if (color == true) {
                    color = false;
                } else {
                    color = true;
                }
            }
        }
    }

    public String[][] deepCopy(String[][] origin) {
        String[][] copy = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = origin[i][j];
            }
        }
        return copy;
    }

    public void getTop() {
        top = Game.frame.getTop();
        menuBar = (MenuBar) Game.frame.top.menuBar;
    }

    public void newGame() {
        top.textLabel.setText("");
        if(gameTimer1 != null){
            gameTimer1.resetTimer();
        gameTimer2.resetTimer();
        gameTimer1.setNull();
        gameTimer2.setNull();
        }
        player1.clear();
        player2.clear();
        Game.frame.side1.gameTimerPanel1.updateLabel();
        Game.frame.side2.gameTimerPanel2.updateLabel();

        if (Game.frame.top.isPromotionNull() == false) {
            Game.frame.top.removePromotion();
        }
        Game.newGame();
    }

    public void applyChanges() {
        switch (Settings.color) {
            case 0:
                color = true;
                colorFixed = true;
                break;
            case 1:
                double random = Math.random();
                if (random < 0.5) {
                    color = true;
                    colorFixed = true;
                } else {
                    color = false;
                    colorFixed = false;
                }
                break;
            case 2:
                color = false;
                colorFixed = false;
                break;
        }
    }

    public void setSettings() {

    }

    public void setBoardRotation(boolean rotateBoard) {
        if (isEnd && maxMove == currentMove) {
            ;
        } else {

            if (currentMove == maxMove) {
                this.rotateBoard = rotateBoard;
                fakeRotate = rotateBoard;
                this.rotateSelected = rotateBoard;
                if (rotateBoard == true
                        && ((color == true && currentPlayer == 1) || (color == false && currentPlayer == 0))) {
                    rotateSelected = false;
                }
            }

            selectedList.clear();

            if (color == true && currentPlayer == 0 || color == false && currentPlayer == 1 || currentMove != maxMove
                    || isEnd == true) {

                rotateBoard();

            }
        }
    }

    private void rotateBoard() {
        String[][] rotatePieces = new String[8][8];

        for (int i = 0, iR = 7; i < 8; i++, iR--) {
            for (int j = 0, jR = 7; j < 8; j++, jR--) {
                rotatePieces[i][j] = pieces[iR][jR];
            }
        }
        pieces = rotatePieces;
        if (front == false) {
            front = true;
        } else {
            front = false;
        }
        for(int[] i : checks){
            if(i != null){
                i[0] = rotateNum(i[0]);
                i[1] = rotateNum(i[1]);
            }
        }
    }

    public int rotateEnpassent(int num) {
        if (rotateBoard == false) {
            return num;
        }
        int counter = 0;

        for (int i = 7; i > num; i--) {
            counter++;
        }
        return counter;
    }

    private String[][] rotateArray(String[][] arr) {
        String[][] rotatePieces = new String[8][8];

        for (int i = 0, iR = 7; i < 8; i++, iR--) {
            for (int j = 0, jR = 7; j < 8; j++, jR--) {
                rotatePieces[i][j] = arr[iR][jR];
            }
        }
        return rotatePieces;
    }

    public int[] rotateArr(int[] arr) {
        int[] temp = new int[arr.length];
        for (int j = 0; j < arr.length; j++) {
            int counter = 0;
            for (int i = 7; i > arr[j]; i--) {
                counter++;
            }
            temp[j] = counter;
        }
        return temp;
    }

    public void draggPiece(int x, int y) {
        lastSelectedX = -1;
        lastSelectedY = -1;

        if (isSelected == false && pieces[x / Board.squareSize][y / Board.squareSize] != null
                && getColor(x / Board.squareSize, y / Board.squareSize, pieces) == currentPlayer) {
            if (selectedX != x / Board.squareSize || selectedY != y / Board.squareSize || isDragged == false) {
                selectPiece(x / Board.squareSize, y / Board.squareSize);
            }
            draggType = getType(x / Board.squareSize, y / Board.squareSize, pieces);
            draggPosX = x / Board.squareSize;
            draggPosY = y / Board.squareSize;
        }
        isSelected = true;
        isDragged = true;
        Game.board.isDragged(x, y, draggType, draggPosX, draggPosY);
    }

    public void draggEnd(int x, int y) {

        if (isDragged == true) {
            isSelected = false;
            if (isSelected(x, y)) {
                movePiece(x, y, selectedX, selectedY, -1);
            }
            lastSelectedX = -1;
            lastSelectedY = -1;
            selectedX = -1;
            selectedY = -1;
            draggType = -1;
            draggPosX = -1;
            draggPosY = -1;
            selectedList.clear();
            isDragged = false;
            Game.board.draggEnd();
        }
    }

    public boolean isEnemy(int posX, int posY) {
        String[][] pieces = getPieces();
        if (pieces[posX][posY] != null && getColor(posX, posY, pieces) != currentPlayer) {
            return true;
        }
        return false;
    }

    private boolean detectFront() {
        if (front) {
            return true;
        } else {
            return false;
        }
    }

    public void endTime() {
        selectedList.clear();
        int temp;
        if (currentPlayer == 1) {
            temp = 0;
        } else {
            temp = 1;
        }
        changeTop(1, temp);
        if (Game.frame.top.isPromotionNull() == false) {
            Game.frame.top.removePromotion();
        }
        gameTimer1.setNull();
        gameTimer2.setNull();

        isEnd = true;
    }

    public void surrender() {
        changeTop(1, currentPlayer);
        selectedList.clear();
        isEnd = true;
        if (Game.frame.top.isPromotionNull() == false) {
            Game.frame.top.removePromotion();
        }
        if(gameTimer1 != null){
            gameTimer1.setNull();
        }
        if(gameTimer2 != null){
            gameTimer2.setNull();
        }

    }

    public void setUpPlayer() {
        if (colorFixed) {
            player1.text.setText("White");
            player2.text.setText("Black");
        } else {
            player1.text.setText("Black");
            player2.text.setText("White");
        }
    }

    public void rotate() {
        selectedList.clear();
        if (rotateBoard) {
            if (rotate) {
                rotate = false;
            } else {
                rotate = true;
            }
            rotateBoard();
        } else {
            rotateBoard();
            if (color == true) {
                color = false;
            } else {
                color = true;
            }
        }
    }

    public void markField(int x, int y) {
        int[] field = { x, y };
        for (int i = 0; i < markedFields.size(); i++) {
            if (Arrays.equals(markedFields.get(i), field)) {
                markedFields.remove(i);
                return;
            }
        }
        markedFields.add(field);

    }

    public boolean isMarked(int x, int y) {
        int[] field = { x, y };

        for (int i = 0; i < markedFields.size(); i++) {
            if (Arrays.equals(markedFields.get(i), field)) {
                return true;
            }
        }
        return false;
    }

    public void clearMarkedList() {
        markedFields.clear();
    }

    private void nextMove() {
        if (colorFixed == true && currentPlayer == 0 || colorFixed == false && currentPlayer == 1) {
            frontAI = front;
            isAI = true;
            int[] move = new int[4];
            move = AI.calculateMove(pieces);
            isAI = false;

            movePiece(move[0], move[1], move[2], move[3], 1);
        } else {
            isAI = false;
        }
    }

    public boolean isPlayer() {
        if ((colorFixed == true && currentPlayer == 1 || colorFixed == false && currentPlayer == 0)
                || Settings.style != "Player vs. AI") {
            return true;
        }
        return false;
    }

    public  ArrayList<Integer> possibleMovesAI(String[][] piecesAI, int x, int y, int currentColor) {
        this.currentColor.set(currentColor);
        Pieces piece = new Pieces(x, y, currentColor, getType(x, y, piecesAI), color, Game.gameUpdate, 0);
        return piece.possibleMoves(x, y, 0, piecesAI);
    }
    public  ArrayList<Integer> getAttackerAI(String[][] piecesAI, int x, int y, int currentColor, int ignoreColors) {
        this.currentColor.set(currentColor);
        Pieces piece = new Pieces(x, y, currentColor, getType(x, y, piecesAI), color, Game.gameUpdate, ignoreColors);
        return piece.possibleMoves(x, y, 1, piecesAI);
    }
    public  ArrayList<Integer> getAttackerAICheck(String[][] piecesAI, int x, int y, int currentColor, int ignoreColors) {
        this.currentColor.set(currentColor);
        Pieces piece = new Pieces(x, y, currentColor, getType(x, y, piecesAI), color, Game.gameUpdate, ignoreColors);
        return piece.possibleMoves(x, y, 0, piecesAI);
    }
    public int getCurrentPlayer() {
        if (isAI) {
            return currentColor.get();
        } else {
            return currentPlayer;
        }
    }
    public boolean getColor(){
        return color;
    }
    public boolean getFront(){
        if(isAI){
            return frontAI;
        }
        return front;
    }

    public boolean isAttackedAI(int kingX, int kingY, int color, String[][] position){
        this.currentColor.set(color);
        return isAttacked(kingX, kingY, color, position);
    }
}
