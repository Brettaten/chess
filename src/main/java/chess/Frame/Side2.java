package chess.Frame;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import chess.Logic.Colors;
import chess.Logic.Game;

public class Side2 extends JPanel{
    private JPanel spacePanelBottom;
    private JPanel spacePanelTop;
    
    public GameTimerPanel gameTimerPanel2;

    public Side2(Player player, int i){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        spacePanelTop = new JPanel();
        spacePanelTop.setPreferredSize(new Dimension(0, 200));

        gameTimerPanel2 = new GameTimerPanel();

        this.add(spacePanelTop);
        this.add(Game.gameUpdate.player2);
        this.add(gameTimerPanel2);
        
        spacePanelBottom = new JPanel();
        spacePanelBottom.setPreferredSize(new Dimension(0, 200));

        this.add(spacePanelBottom);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        spacePanelTop.setBackground(Colors.backgroundColor);
        spacePanelBottom.setBackground(Colors.backgroundColor);
    }
}