package chess.Logic;

import java.util.ArrayList;

public class Pieces {
    private int pieceColor;
    private int type;
    private int ignoreColors;
    private GameUpdate gameUpdate;
    private boolean front;

    Pieces(int posX, int posY, int pieceColor, int type, boolean color, GameUpdate gameUpdate, int ignoreColors){
        this.pieceColor = pieceColor;
        this.type = type;
        this.gameUpdate = gameUpdate;
        this.ignoreColors = ignoreColors;
    }

    public ArrayList<Integer> possibleMoves(int posX, int posY, int check, String[][] pieces){

        ArrayList<Integer> list = new ArrayList<>();
        if(ignoreColors == 0){
            switch(type){
                case 0,1:
                    for(int i = posX - 1; i < posX + 2; i++){
                        for(int j = posY - 1; j < posY + 2; j++){
                            if((i != posX || j != posY) && gameUpdate.isClear(i, j, pieceColor, pieces)){
                                if(check == 1){
                                    list.add(i);
                                    list.add(j);
                                }
                                else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                    list.add(i);
                                    list.add(j);
                                }
                            }
                        }
                    }
                    if(check == 0){
                        if(gameUpdate.castlePossible(posX, posY, pieceColor, pieces).charAt(0) == '1'){
                            list.add(posX);
                            list.add(posY-2);
                        }
                        if(gameUpdate.castlePossible(posX, posY, pieceColor, pieces).charAt(1) == '1'){
                            list.add(posX);
                            list.add(posY+2);
                        }
                    }
                    break;
                case 2,3:
                    int iIncrement = -1;
                    int jIncrement = -1;

                    for(int i = posX, j = posX, k = 0; k < 8; k++){
                        i = posX;
                        j = posY;

                        if(k == 1 || k == 2){
                            jIncrement++;
                        }
                        else if(k == 3 || k == 4){
                            iIncrement++;
                        }
                        else if(k == 5 || k == 6){
                            jIncrement--;
                        }
                        else if(k == 7){
                            iIncrement--;
                        }
                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, pieceColor, pieces)){
                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(gameUpdate.enemy(posX, posY, i, j, pieceColor, pieces)){
                                break;
                            }
                        }
                    }
                    break;
                case 4,5:
                    iIncrement = -1;
                    jIncrement = -1;

                    for(int i = posX, j = posY , k = 0; k < 4; k++){
                        i = posX;
                        j = posY;
                        if(k == 1){
                            iIncrement += 2;
                        }
                        else if(k == 2){
                            jIncrement += 2;
                        }
                        else if(k == 3){
                            iIncrement -= 2;
                        }

                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, pieceColor, pieces)){
                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(gameUpdate.enemy(posX, posY, i, j, pieceColor, pieces)){
                                break;
                            }
                        }
                    }
                    break;
                case 6,7:
                    if(gameUpdate.isClear(posX-2, posY-1, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX-2);
                            list.add(posY-1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 2, posY - 1, type, false, pieces)){
                            list.add(posX-2);
                            list.add(posY-1);
                        }
                    }
                    if(gameUpdate.isClear(posX-1, posY-2, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX-1);
                            list.add(posY-2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 1, posY - 2, type, false, pieces)){
                            list.add(posX-1);
                            list.add(posY-2);
                        }
                    }
                    if(gameUpdate.isClear(posX+1, posY-2, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX+1);
                            list.add(posY-2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 1, posY - 2, type, false, pieces)){
                            list.add(posX+1);
                            list.add(posY-2);
                        }
                    }
                    if(gameUpdate.isClear(posX+2, posY-1, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX+2);
                            list.add(posY-1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 2, posY - 1, type, false, pieces)){
                            list.add(posX+2);
                            list.add(posY-1);
                        }
                    }
                    if(gameUpdate.isClear(posX+2, posY+1, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX+2);
                            list.add(posY+1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 2, posY + 1, type, false, pieces)){
                            list.add(posX+2);
                            list.add(posY+1);
                        }
                    }
                    if(gameUpdate.isClear(posX+1, posY+2, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX+1);
                            list.add(posY+2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 1, posY + 2, type, false, pieces)){
                            list.add(posX+1);
                            list.add(posY+2);
                        }
                    }
                    if(gameUpdate.isClear(posX-1, posY+2, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX-1);
                            list.add(posY+2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 1, posY + 2, type, false, pieces)){
                            list.add(posX-1);
                            list.add(posY+2);
                        }
                    }
                    if(gameUpdate.isClear(posX-2, posY+1, pieceColor, pieces)){
                        if(check == 1){
                            list.add(posX-2);
                            list.add(posY+1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 2, posY + 1, type, false, pieces)){
                            list.add(posX-2);
                            list.add(posY+1);
                        }
                    }
                    break;
                case 8,9:
                    iIncrement = 0;
                    jIncrement = -1;

                    for(int i = posX, j = posY, k = 0; k < 4; k++){

                        i = posX;
                        j = posY;
                        if(k == 1){
                            iIncrement++;
                            jIncrement++;
                        }
                        else if(k == 2){
                            iIncrement--;
                            jIncrement ++;
                        }
                        else if(k == 3){
                            iIncrement --;
                            jIncrement--;
                        }

                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, pieceColor, pieces)){

                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(gameUpdate.enemy(posX, posY, i, j, pieceColor, pieces)){
                                break;
                            }
                        }
                    }
                    break;
                case 10,11:
                    int factor;
                    if(gameUpdate.front && gameUpdate.getCurrentPlayer() == 1 || !gameUpdate.front && gameUpdate.getCurrentPlayer() == 0){
                        factor = -1;
                    }
                    else{
                        factor = 1;
                    }
                    if(check == 0){
                        if(!gameUpdate.pawnMoved(posX, posY, pieces) && gameUpdate.isNull(posX + (2*factor), posY, pieces) && gameUpdate.isNull(posX + (factor), posY, pieces)){
                            if(!gameUpdate.isChecked(posX, posY, posX + (2 * factor), posY, type, false, pieces)){
                                list.add(posX + (2*factor));
                                list.add(posY);
                            }
                        }
                        if(gameUpdate.isNull(posX + (factor), posY, pieces)){
                            if(!gameUpdate.isChecked(posX, posY, posX + (factor), posY, type, false, pieces)){
                                list.add(posX + (factor));
                                list.add(posY);
                            }
                        }
                        if(gameUpdate.isClear(posX  + (factor), posY  + (factor), pieceColor, pieces) && gameUpdate.enemy(posX, posY, posX  + (factor), posY  + (factor), pieceColor, pieces)){
                            if(!gameUpdate.isChecked(posX, posY, posX + (factor), posY + (factor), type, false, pieces)){
                                list.add(posX  + (factor));
                                list.add(posY  + (factor));
                            }
                        }
                        if(gameUpdate.isClear(posX  + (factor), posY  + (-factor), pieceColor, pieces) && gameUpdate.enemy(posX, posY, posX  + (factor), posY  + (-factor), pieceColor, pieces)){
                            if(!gameUpdate.isChecked(posX, posY, posX + (factor), posY + (-factor), type, false, pieces)){
                                list.add(posX  + (factor));
                                list.add(posY  + (-factor));
                            }
                        }
                    }
                    if(check == 1){
                        if(gameUpdate.isClear(posX  + (-factor), posY  + (-factor), pieceColor, pieces)){
                            list.add(posX + (-factor));
                            list.add(posY  + (-factor));
                        }

                        if(gameUpdate.isClear(posX  + (-factor), posY  + (factor), pieceColor, pieces)){
                            list.add(posX + (-factor));
                            list.add(posY  + (factor));
                        }
                    }
                    if(check == 0){
                        if(gameUpdate.enPassent != null){
                            if(posY == Character.getNumericValue(gameUpdate.enPassent.charAt(1)) && posX ==  Character.getNumericValue(gameUpdate.enPassent.charAt(0)) && !gameUpdate.isChecked(posX, posY, posX + (factor), Character.getNumericValue(gameUpdate.enPassent.charAt(1)), type, true, pieces)){

                                if(gameUpdate.front && pieceColor == 1 ||!gameUpdate.front && pieceColor == 0){
                                    list.add(posX + (factor));
                                    list.add(posY + (-factor));
                                }
                                else if (!gameUpdate.front && pieceColor == 1 || gameUpdate.front && pieceColor == 0){
                                    list.add(posX + (factor));
                                    list.add(posY + (factor));
                                }

                            }
                            else if(posY == Character.getNumericValue(gameUpdate.enPassent.charAt(3)) && posX ==  Character.getNumericValue(gameUpdate.enPassent.charAt(2)) && !gameUpdate.isChecked(posX, posY, posX + (factor), Character.getNumericValue(gameUpdate.enPassent.charAt(3)), type, true, pieces)){

                                if(gameUpdate.front && pieceColor == 1 ||!gameUpdate.front && pieceColor == 0){
                                    list.add(posX + (factor));
                                    list.add(posY + (factor));
                                }
                                else if (!gameUpdate.front && pieceColor == 1 || gameUpdate.front && pieceColor == 0){
                                    list.add(posX + (factor));
                                    list.add(posY + (-factor));
                                }

                            }
                        }
                    }

                    break;

            }
        }
        else{
            switch(type){
                case 0,1:
                    for(int i = posX - 1; i < posX + 2; i++){
                        for(int j = posY - 1; j < posY + 2; j++){
                            if((i != posX || j != posY) && gameUpdate.isClear(i, j, 2, pieces)){
                                if(check == 1){
                                    list.add(i);
                                    list.add(j);
                                }
                                else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                    list.add(i);
                                    list.add(j);
                                }
                            }
                        }
                    }


                    break;
                case 2,3:
                    int iIncrement = -1;
                    int jIncrement = -1;

                    for(int i = posX, j = posX, k = 0; k < 8; k++){
                        i = posX;
                        j = posY;

                        if(k == 1 || k == 2){
                            jIncrement++;
                        }
                        else if(k == 3 || k == 4){
                            iIncrement++;
                        }
                        else if(k == 5 || k == 6){
                            jIncrement--;
                        }
                        else if(k == 7){
                            iIncrement--;
                        }
                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, 2, pieces)){
                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(pieces[i][j] != null){
                                break;
                            }
                        }
                    }
                    break;
                case 4,5:
                    iIncrement = -1;
                    jIncrement = -1;

                    for(int i = posX, j = posY , k = 0; k < 4; k++){
                        i = posX;
                        j = posY;
                        if(k == 1){
                            iIncrement += 2;
                        }
                        else if(k == 2){
                            jIncrement += 2;
                        }
                        else if(k == 3){
                            iIncrement -= 2;
                        }

                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, 2, pieces)){
                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(pieces[i][j] != null){
                                break;
                            }
                        }
                    }
                    break;
                case 6,7:
                    if(gameUpdate.isClear(posX-2, posY-1, 2, pieces)){
                        if(check == 1){
                            list.add(posX-2);
                            list.add(posY-1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 2, posY - 1, type, false, pieces)){
                            list.add(posX-2);
                            list.add(posY-1);
                        }
                    }
                    if(gameUpdate.isClear(posX-1, posY-2, 2, pieces)){
                        if(check == 1){
                            list.add(posX-1);
                            list.add(posY-2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 1, posY - 2, type, false, pieces)){
                            list.add(posX-1);
                            list.add(posY-2);
                        }
                    }
                    if(gameUpdate.isClear(posX+1, posY-2, 2, pieces)){
                        if(check == 1){
                            list.add(posX+1);
                            list.add(posY-2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 1, posY - 2, type, false, pieces)){
                            list.add(posX+1);
                            list.add(posY-2);
                        }
                    }
                    if(gameUpdate.isClear(posX+2, posY-1, 2, pieces)){
                        if(check == 1){
                            list.add(posX+2);
                            list.add(posY-1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 2, posY - 1, type, false, pieces)){
                            list.add(posX+2);
                            list.add(posY-1);
                        }
                    }
                    if(gameUpdate.isClear(posX+2, posY+1, 2, pieces)){
                        if(check == 1){
                            list.add(posX+2);
                            list.add(posY+1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 2, posY + 1, type, false, pieces)){
                            list.add(posX+2);
                            list.add(posY+1);
                        }
                    }
                    if(gameUpdate.isClear(posX+1, posY+2, 2, pieces)){
                        if(check == 1){
                            list.add(posX+1);
                            list.add(posY+2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX + 1, posY + 2, type, false, pieces)){
                            list.add(posX+1);
                            list.add(posY+2);
                        }
                    }
                    if(gameUpdate.isClear(posX-1, posY+2, 2, pieces)){
                        if(check == 1){
                            list.add(posX-1);
                            list.add(posY+2);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 1, posY + 2, type, false, pieces)){
                            list.add(posX-1);
                            list.add(posY+2);
                        }
                    }
                    if(gameUpdate.isClear(posX-2, posY+1, 2, pieces)){
                        if(check == 1){
                            list.add(posX-2);
                            list.add(posY+1);
                        }
                        else if(!gameUpdate.isChecked(posX, posY, posX - 2, posY + 1, type, false, pieces)){
                            list.add(posX-2);
                            list.add(posY+1);
                        }
                    }
                    break;
                case 8,9:
                    iIncrement = 0;
                    jIncrement = -1;

                    for(int i = posX, j = posY, k = 0; k < 4; k++){

                        i = posX;
                        j = posY;
                        if(k == 1){
                            iIncrement++;
                            jIncrement++;
                        }
                        else if(k == 2){
                            iIncrement--;
                            jIncrement ++;
                        }
                        else if(k == 3){
                            iIncrement --;
                            jIncrement--;
                        }

                        while(gameUpdate.isClear(i += iIncrement, j += jIncrement, 2, pieces)){

                            if(check == 1){
                                list.add(i);
                                list.add(j);
                            }
                            else if(!gameUpdate.isChecked(posX, posY, i, j, type, false, pieces)){
                                list.add(i);
                                list.add(j);
                            }
                            if(pieces[i][j] != null){
                                break;
                            }
                        }
                    }
                    break;
                case 10,11:
                    int factor;
                    if(gameUpdate.front && gameUpdate.getCurrentPlayer() == 1 || !gameUpdate.front && gameUpdate.getCurrentPlayer() == 0){
                        factor = -1;
                    }
                    else{
                        factor = 1;
                    }
                    if(check == 0){
                            if(gameUpdate.isClear(posX  + (factor), posY  + (factor), pieceColor, pieces) && !gameUpdate.isChecked(posX, posY, posX + (factor), posY + (factor), type, false, pieces)){
                                list.add(posX  + (factor));
                                list.add(posY  + (factor));
                            }

                            if(gameUpdate.isClear(posX  + (factor), posY  + (-factor), pieceColor, pieces) && !gameUpdate.isChecked(posX, posY, posX + (factor), posY + (-factor), type, false, pieces)){
                                list.add(posX  + (factor));
                                list.add(posY  + (-factor));
                            }

                    }
                    if(check == 1){
                        if(gameUpdate.isClear(posX  + (factor), posY  + (factor), 2, pieces)){
                            list.add(posX + (factor));
                            list.add(posY  + (factor));
                        }

                        if(gameUpdate.isClear(posX  + (factor), posY  + (factor), 2, pieces)){
                            list.add(posX + (factor));
                            list.add(posY  + (-factor));
                        }
                    }


                    break;

            }
        }
        return list;
    }
}

