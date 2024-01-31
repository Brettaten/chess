package chess.Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import chess.Inputs.TopInputs;
import chess.Logic.Colors;

public class MenuBar extends JMenuBar {

    public JMenu[] menus;

    private JMenuItem[] gameItems;
    private String[] gameNames = { "New Game", "Game Settings" };
    private String[] settingsNames = { "Themes", "Exit" };
    private String[] themes = { "Dark", "Bright", "Blue", "Green", "Red" };

    private TopInputs topInputs;
    public boolean isImgActive;
    private JMenu menuThemes;

    MenuBar(String[] menuNames) {
        setGUI();
        this.setBackground(Colors.menuBarColor);
        this.setBorder(null);

        gameItems = new JMenuItem[gameNames.length + settingsNames.length - 2 + themes.length + 1];
        menus = createMenuItems(menuNames);
        isImgActive = false;

        topInputs = new TopInputs(gameItems, menus);
        addListener();
    }

    private void setGUI() {
        UIManager.put("Menu.foreground", Colors.foreground);
        UIManager.put("Menu.font", new Font(null, Font.PLAIN, 30));
        UIManager.put("Menu.border", new LineBorder(Colors.menuBarColor, 0));
        UIManager.put("Menu.selectionBackground", Colors.menuBarColor);
        UIManager.put("Menu.selectionForeground", Colors.foreground);
        UIManager.put("Menu.arrowIcon", arrow());

        UIManager.put("MenuItem.background", Colors.menuBarColor);
        UIManager.put("MenuItem.foreground", Colors.foreground);
        UIManager.put("MenuItem.border", new LineBorder(Colors.menuBarColor, 0));
        UIManager.put("MenuItem.selectionBackground", Colors.menuBarColor);
        UIManager.put("MenuItem.selectionForeground", Colors.foreground);
        UIManager.put("MenuItem.font", new Font(null, Font.PLAIN, 20));

        UIManager.put("PopupMenu.border", new LineBorder(Colors.menuBarColor));
    }

    private JMenu[] createMenuItems(String[] itemNames) {
        menus = new JMenu[itemNames.length];
        for (int i = 0; i < itemNames.length; i++) {
            menus[i] = new JMenu(itemNames[i]) ;
           
            addItems(menus[i]);
            this.add(menus[i]);
        }
        return menus;
    }

    private void addItems(JMenu menu) {
        switch (menu.getText()) {
            case "Game":
                for (int i = 0; i < gameNames.length; i++) {
                    gameItems[i] = new JMenuItem(gameNames[i]);

                    if (i == 0) {
                        gameItems[i].setBorder(new MatteBorder(2, 0, 2, 0, Colors.borderColor));
                    } else {
                        gameItems[i].setBorder(new MatteBorder(0, 0, 2, 0, Colors.borderColor));
                    }
                    menu.add(gameItems[i]);

                }
                break;
            case "Settings":
                for (int i = gameNames.length, j = 0; j < settingsNames.length; i++, j++) {
                    if (j == 0) {
                        menu.add(createMenu(j, i, themes));
                    } else {
                        gameItems[7] = new JMenuItem(settingsNames[j]);
                        gameItems[7].setBorder(new MatteBorder(0, 0, 2, 0, Colors.borderColor));
                        menu.add(gameItems[7]);
                    }
                }
                break;
        }
    }

    private JMenu createMenu(int j, int i, String[] items) {
        menuThemes = new JMenu(settingsNames[j]);
        menuThemes.setFont(new Font(null, Font.PLAIN, 20));
        menuThemes.setBackground(Colors.menuBarColor);
        menuThemes.setOpaque(true);
        menuThemes.setBorder(new MatteBorder(2, 0, 2, 0, Colors.borderColor));
        menuThemes.setDelay(500);

        for (int k = 0; k < items.length; k++, i++) {
            JMenuItem menuItem = new JMenuItem(items[k]);
            menuItem.addActionListener(topInputs);
            if (k == 0) {
                menuItem.setBorder(new MatteBorder(2, 2, 1, 2, Colors.borderColor));
            } else if (k == 4) {
                menuItem.setBorder(new MatteBorder(1, 2, 2, 2, Colors.borderColor));
            } else {
                menuItem.setBorder(new MatteBorder(1, 2, 1, 2, Colors.borderColor));
            }
            gameItems[i] = menuItem;
            menuThemes.setLayout(new BorderLayout());
            menuThemes.add(menuItem);
            menuThemes.getComponents();
        }
        return menuThemes;
    }

    private void addListener() {
        for (int i = 0; i < gameItems.length; i++) {
            gameItems[i].addActionListener(topInputs);
        }
    }

    // public void resetImg(){
    // if(isImgActive == true){
    // checkBoxImg.setIcon(new ImageIcon(imgPassive));
    // isImgActive = false;
    // }
    // }
    private ImageIcon arrow() {
        BufferedImage image = new BufferedImage(7, 10, BufferedImage.TRANSLUCENT);

        Graphics2D g = image.createGraphics();
        int[] x = { 0, 0, image.getWidth() };
        int[] y = { 0, image.getHeight(), image.getHeight() / 2 };
        g.fillPolygon(x, y, x.length);

        return new ImageIcon(image);
    }
}
