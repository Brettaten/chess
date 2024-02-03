package chess.Frame;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import chess.Logic.Colors;
import chess.Logic.Game;

public class Side1 extends JPanel{
    private JPanel spacePanelBottom;
    private JPanel spacePanelTop;

    public GameTimerPanel gameTimerPanel1;

    public Side1(Player player, int i){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        spacePanelTop = new JPanel();
        spacePanelTop.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * ((double) 200/1080))));

        gameTimerPanel1 = new GameTimerPanel();

        this.add(spacePanelTop);
        this.add(player);
        this.add(gameTimerPanel1);
        
        spacePanelBottom = new JPanel();
        spacePanelBottom.setPreferredSize(new Dimension(0, (int) (Game.screenHeight * ((double) 200/1080))));

        this.add(spacePanelBottom);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        spacePanelTop.setBackground(Colors.backgroundColor);
        spacePanelBottom.setBackground(Colors.backgroundColor);
    }
}