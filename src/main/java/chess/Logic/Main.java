package chess.Logic;


import java.util.ArrayList;

/*
 *
 * 
 * Timer
 * Game -> new Game         DONE
 *      -> game settings    DONE
 * new Game button          DONE
 * (design)
 * color checked king       DONE
 * drag and drop            DONE
 * colored fields diff      DONE
 * enpasssent rotation      DONE
 * enpassent add piece      DONE
 * center added pieces      DONE
 * Themes                   DONE
 * panel adjusting itself
 * lastmove promotion       DONE
 * themes menu closing(
 *enPassent rotate
 * board is switching when changing settings
 * if i rotate the board before first move programm crashes when i do a move
 * promotion panel
 * its possible to ff when the time is up   DONE
 * king can move into check of pawn DONE
 * Time doesnt work properly in rotate mode 
 * promotion doesnt work properly (if you watch the prevoius moves and come back there is a pawn instead of a queen)
 * unlimited Time option
 */
public class Main {
    public static void main(String[] args) {
        new Game();
    }
}

