package chess.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.GameUpdate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class Top extends JPanel{

    public JLabel textLabel;
    public JMenuBar menuBar;
    public JPanel bar;
    private Promotion promotion;
    private JPanel panel;

    //private GameUpdate gameUpdate;
    
    Top(GameUpdate gameUpdate){
        //this.gameUpdate = gameUpdate;
        double heightFactor = (double) 140 /1080;
        this.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * heightFactor)));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Colors.backgroundColor);

        textLabel = new JLabel();
        textLabel.setAlignmentX(CENTER_ALIGNMENT);
        textLabel.setFont(new Font(null, Font.PLAIN, (int) (Game.screenHeight*0.0462)));
        textLabel.setOpaque(true);
        textLabel.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * ((double) 100/1080))));
        textLabel.setBackground(Colors.backgroundColor);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textLabel);
        String[] labelNames = {"Game", "Settings"};

        bar = new JPanel();
        bar.setLayout(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * ((double) 40/1080))));

        menuBar = new MenuBar(labelNames);

        bar.add(menuBar);

        this.add(bar);
        this.add(Box.createHorizontalGlue());
        this.add(panel);
        this.add(Box.createHorizontalGlue());
    }
    public void promtotion(Promotion promotion) {
        this.promotion = promotion;
        promotion.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * ((double) 100/1080))));
        panel.add(promotion);
    }
    public void removePromotion(){
        panel.remove(promotion);
    }
    public boolean isPromotionNull(){
        if(promotion == null){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Colors.backgroundColor);

        textLabel.setForeground(Colors.foreground);
        textLabel.setBackground(Colors.backgroundColor);
        panel.setForeground(Colors.foreground);
        panel.setBackground(Colors.backgroundColor);
    }
}
