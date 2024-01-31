package chess.Frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import chess.Logic.Colors;
import chess.Logic.Settings;

public class GameTimerPanel extends JPanel {
    public JLabel timerLabel;
    public int i;
    public static int j;

    public GameTimerPanel() {
        setUpPanel();
        setUpLabel();
    }

    public void setUpPanel() {
        this.setPreferredSize(new Dimension(100, 200));
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(Colors.backgroundColor);
        this.setBorder(new MatteBorder(4, 0, 4, 0, Colors.borderColor));
    }

    public void setUpLabel() {
        timerLabel = new JLabel();
        timerLabel.setOpaque(true);
        timerLabel.setVerticalTextPosition(JLabel.CENTER);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setFont(new Font(null, Font.PLAIN, 60));
        timerLabel.setText("05:00");
        timerLabel.setBackground(Colors.sideColor);
        timerLabel.setForeground(Colors.foreground);

        this.add(timerLabel);
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }
    public void updateLabel(){
        if(Settings.time.length() == 4){
            timerLabel.setText("0"+Settings.time);
        }
        else{
            timerLabel.setText(Settings.time);
        }   
    }

   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);

       this.setBackground(Colors.backgroundColor);
        this.setBorder(new MatteBorder(4, 0, 4, 0, Colors.borderColor));

        timerLabel.setBackground(Colors.sideColor);
        timerLabel.setForeground(Colors.foreground);
   }
}
