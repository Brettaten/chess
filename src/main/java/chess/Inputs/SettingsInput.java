package chess.Inputs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import chess.Frame.SettingsFrame;
import chess.Logic.Game;
import chess.Logic.GameUpdate;
import chess.Logic.Settings;

public class SettingsInput implements MouseListener, ActionListener, ItemListener {
    private ArrayList<JComboBox<String>> comboComponents;
    private ArrayList<JLabel> labelComponents;
    private SettingsFrame settingsFrame;
    private GameUpdate gameUpdate;

    public SettingsInput(ArrayList<JComboBox<String>> comboComponents, ArrayList<JLabel> labelComponents,
            SettingsFrame settingsFrame) {
        this.comboComponents = comboComponents;
        this.labelComponents = labelComponents;
        this.settingsFrame = settingsFrame;
        /*
         * style
         * Time         combo
         * Increment
         * difficulty
         * 
         * White
         * Random
         * Black
         * Cancel       label
         * Save
         * checkbox
         */
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameUpdate = Game.gameUpdate;
        if (e.getSource() == labelComponents.get(4)) {
            reset();
            settingsFrame.setVisible(false);
            gameUpdate.settingsActive = false;
        } else if (e.getSource() == labelComponents.get(5)) {
            applyChanges();
            settingsFrame.changeImg();
            settingsFrame.setVisible(false);
            gameUpdate.settingsActive = false;
        } 
        
        
        else if (e.getSource() == labelComponents.get(0) && labelComponents.get(0).getBackground() != Color.GRAY) {
            changeColor(0);
        } else if (e.getSource() == labelComponents.get(1) && labelComponents.get(1).getBackground() != Color.GRAY) {
            changeColor(1);
        } else if (e.getSource() == labelComponents.get(2) && labelComponents.get(2).getBackground() != Color.GRAY) {
            changeColor(2);
        } else if (e.getSource() == labelComponents.get(3)) {
            settingsFrame.changeImg();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }


    private void applyChanges() {
        String time = getTime();
        int Increment = getIncrement();
        int color = getColor();
        String style = getStyle();
        int difficulty = getDifficulty();

        Settings.setTime(time);
        Settings.setIncrement(Increment);
        Settings.setColor(color);
        Settings.setRotateBoard(getRotate());
        Settings.setStyle(style);
        Settings.setDifficulty(difficulty);
        if (Game.gameUpdate.maxMove == 0) {
           //Game.gameUpdate.setSettings();
        }
        gameUpdate.newGame();
    }

    private int getDifficulty() {
        return Integer.parseInt((String)comboComponents.get(3).getSelectedItem());
    }

    private String getStyle() {
        return (String)comboComponents.get(0).getSelectedItem();
    }

    private boolean getRotate() {
        return settingsFrame.ImgActive;
    }

    private int getColor() {
        for (int i = 0; i < labelComponents.size(); i++) {
            if (labelComponents.get(i).getBackground() == Color.GRAY) {
                return i;
            }
        }
        return 0;
    }

    private int getIncrement() {
        String s = (String) comboComponents.get(2).getSelectedItem();
        String ges = "";
        for (int i = 2; i < s.length(); i++) {
            ges += s.charAt(i);
        }
        return Integer.parseInt(ges);
    }

    private String getTime() {
        return (String)comboComponents.get(1).getSelectedItem();
    }

    private void changeColor(int index) {
        labelComponents.get(index).setBackground(Color.GRAY);
        labelComponents.get(index).setBorder(new LineBorder(Color.WHITE, 2));

        if (index != 0) {
            labelComponents.get(0).setBackground(Color.DARK_GRAY);
            labelComponents.get(0).setBorder(new EmptyBorder(2, 2, 2, 2));
        }
        if (index != 1) {
            labelComponents.get(1).setBackground(Color.DARK_GRAY);
            labelComponents.get(1).setBorder(new EmptyBorder(2, 2, 2, 2));
        }
        if (index != 2) {
            labelComponents.get(2).setBackground(Color.DARK_GRAY);
            labelComponents.get(2).setBorder(new EmptyBorder(2, 2, 2, 2));
        }
    }

    private void reset() {
        for (int i = 0; i < labelComponents.size() - 2; i++) {
            if (Settings.color != i && labelComponents.get(i).getBackground() == Color.GRAY) {
                labelComponents.get(i).setBackground(Color.DARK_GRAY);
            }
        }
        for (int i = 0; i < comboComponents.size(); i++) {
            if (i == 0 && Settings.time != getTime()) {
                comboComponents.get(i).setSelectedItem(String.valueOf(Settings.time) + ":00");
            } else if (i == 1 && Settings.increment != getIncrement()) {
                comboComponents.get(i).setSelectedItem(String.valueOf(String.valueOf("+" + Settings.increment)));
            }
        }
        if (Settings.rotateBoard != settingsFrame.ImgActive) {
            settingsFrame.ImgActive = Settings.rotateBoard;
            if (settingsFrame.ImgActive) {
                settingsFrame.checkBoxImg.setIcon(new ImageIcon(settingsFrame.imgActive));
                settingsFrame.ImgActive = true;
            } else {
                settingsFrame.checkBoxImg.setIcon(new ImageIcon(settingsFrame.imgPassive));
                settingsFrame.ImgActive = false;
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Game.stopValidating();
            String selectedItem = (String) e.getItem();
            if (selectedItem.equals("Player vs. AI")) {
                settingsFrame.enableAI();
            }
            else if(selectedItem.equals("Player vs. Player")){
                settingsFrame.enablePlayerVsPlayer();
            }
            Game.startValidating();
        }
    
    }
}
