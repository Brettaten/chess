package chess.Inputs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import chess.Frame.Frame;
import chess.Frame.SettingsFrame;
import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.GameUpdate;

public class TopInputs implements ActionListener, MouseListener{
    private JMenuItem[] gameItems;
    GameUpdate gameUpdate;
    Frame frame;

    public TopInputs(JMenuItem[] gameItems, JMenu[] menus){
        this.gameItems = gameItems;
        gameUpdate = Game.gameUpdate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameUpdate = Game.gameUpdate;
        if(e.getSource() == gameItems[0]){
            gameUpdate.newGame();
        }
        else if(e.getSource() == gameItems[1]){
           if(gameUpdate.settingsActive == false){
             SettingsFrame settingsFrame = new SettingsFrame();
            Game.board.add(settingsFrame, BorderLayout.CENTER);  
            gameUpdate.settingsActive = true;
           }
        }
        else if(e.getSource() == gameItems[2]){
            Colors.changeTheme(0);
        }
        else if(e.getSource() == gameItems[3]){
            Colors.changeTheme(1);
        }
         else if(e.getSource() == gameItems[4]){
            Colors.changeTheme(2);
        }
         else if(e.getSource() == gameItems[5]){
            Colors.changeTheme(3);
        }
         else if(e.getSource() == gameItems[6]){
            Colors.changeTheme(4);
        }
        
        else if(e.getSource() == gameItems[7]){
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        
       
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
     
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    } 
}
