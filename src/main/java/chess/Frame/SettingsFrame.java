package chess.Frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;

import chess.Inputs.SettingsInput;
import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.Settings;

public class SettingsFrame extends JPanel {
    private JPanel buttonPanel;
    private JPanel panelL;
    private JPanel panelR;
    private JLabel save;
    private JLabel cancel;
    private JLabel text;
    private JPanel header;
    private JPanel textPanel;
    private JPanel comPanel;
    private JPanel componentPanel;
    private JComboBox<String> combobox;
    private JPanel panel;
    private JLabel white;
    private JLabel black;
    private JLabel random;
    private JPanel checkPanel;
    private JPanel checkTextPanel;
    private JLabel checkTextLabel;
    private JPanel difficultyPanel;
    private JPanel difficultyTextPanel;
    private JLabel difficultyTextLabel;
    public BufferedImage imgPassive;
    public BufferedImage imgActive;
    public JLabel checkBoxImg;
    public boolean ImgActive;

    public static class MyComboBoxUI extends BasicComboBoxUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            LookAndFeel.uninstallBorder(comboBox); // Uninstalls the LAF border for both button and label of combo box.
        }

        @Override
        protected JButton createArrowButton() {
            // Feel free to play with the colors:
            final JButton button = new JButton(arrow());
            button.setName("ComboBox.arrowButton"); // Mandatory, as per BasicComboBoxUI#createArrowButton().
            button.setBorder(new EmptyBorder(5, 5, 5, 5));
            // button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            // button.setBorderPainted(false);
            return button;

        }

    }

    private BufferedImage img;
    private BufferedImage[] piecesImg;
    private String[] timeSettings = { "1:00", "3:00", "5:00", "10:00", "15:00", "30:00", "unlimited" };
    private String[] incrementSettings = { "+ 0", "+ 1", "+ 3", "+ 5", "+ 10", "+ 20", "+ 30" };
    private String[] styleSettings = { "Player vs. Player", "Player vs. AI" };
    private String[] difficultySettings = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private ArrayList<JComboBox<String>> comboComponents;
    private ArrayList<JLabel> labelComponents;
    Settings settings;

    public SettingsFrame() {
        piecesImg = new BufferedImage[3];
        comboComponents = new ArrayList<>();
        labelComponents = new ArrayList<>();
        setGUI();
        importImg();
        addImg();
        createFrame();
        createListener();
        setSettings();
    }

    private void setGUI() {
        UIManager.put("ComboBox.selectionBackground", Color.DARK_GRAY);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("Label.font", new Font(null, Font.PLAIN, (int) (Game.screenHeight*0.0185)));
    }

    private void setSettings() {
        comboComponents.get(0).setSelectedItem(Settings.style);
        comboComponents.get(1).setSelectedItem(Settings.time);
        comboComponents.get(2).setSelectedItem("+ " + String.valueOf(Settings.increment));
        comboComponents.get(3).setSelectedItem(String.valueOf(Settings.difficulty));
        if (Settings.rotateBoard && Settings.style == "Player vs. Player") {
            checkBoxImg.setIcon(new ImageIcon(imgActive));
            ImgActive = true;
        } else if (Settings.rotateBoard == false || Settings.style != "Player vs. Player") {
            checkBoxImg.setIcon(new ImageIcon(imgPassive));
            ImgActive = false;
        }
    
        if(Settings.style == "Player vs. Player"){
            enablePlayerVsPlayer();
        }
        else{
            enableAI();
        }
    }

    private void createListener() {
        SettingsInput settingsInput = new SettingsInput(comboComponents, labelComponents, this);

        for (JComboBox<String> c : comboComponents) {
            c.addMouseListener(settingsInput);
            c.addItemListener(settingsInput);
        }
        for (JLabel l : labelComponents) {
            l.addMouseListener(settingsInput);
        }
    }

    private void createFrame() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.DARK_GRAY);
        this.add(createHeader());
        this.add(createCom(4));
        this.add(createCom(2));
        this.add(createCom(3));
        this.add(createCom(1));
        this.add(createCheckBox());
        this.add(createCom(5));
        this.add(createButtons());
    }

    private JPanel createCheckBox() {
        JLabel checkBox;
        checkBox = new JLabel("Rotate Board");
        checkBox.setForeground(Colors.foreground);
        checkBox.setVerticalAlignment(JLabel.CENTER);

        JPanel textPanelC = new JPanel();
        textPanelC.add(checkBox);
        textPanelC.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 100/1920)), (int) (Game.screenHeight * ((double) 40/1080))));
        textPanelC.setBackground(Color.DARK_GRAY);

        checkBoxImg = new JLabel();
        if (Settings.rotateBoard) {
            checkBoxImg.setIcon(new ImageIcon(imgActive));
        } else {
            checkBoxImg.setIcon(new ImageIcon(imgPassive));

        }

        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        imgPanel.add(checkBoxImg);
        imgPanel.setBackground(Color.DARK_GRAY);
        imgPanel.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 125/1920)), (int) (Game.screenHeight * ((double) 40/1080))));

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));

        checkBoxPanel.setBackground(Color.DARK_GRAY);
        checkBoxPanel.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 320/1920)), (int) (Game.screenHeight * ((double) 40/1080))));
        checkBoxPanel.add(textPanelC);
        checkBoxPanel.add(imgPanel);
        checkBoxPanel.setBorder(new MatteBorder(0, 0, 2, 0, Colors.borderColor));
        labelComponents.add(checkBoxImg);

        checkPanel = imgPanel;
        checkTextPanel = textPanelC;
        checkTextLabel = checkBox;

        return checkBoxPanel;
    }

    public void changeImg() {
        if (ImgActive  || comboComponents.get(0).getSelectedItem() != "Player vs. Player") {
            checkBoxImg.setIcon(new ImageIcon(imgPassive));
            ImgActive = false;
        } else {
            checkBoxImg.setIcon(new ImageIcon(imgActive));
            ImgActive = true;
        }
    }

    private Component createButtons() {
        buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (Game.screenHeight * ((double) 50/1080))));
        buttonPanel.setBackground(Color.DARK_GRAY);

        panelL = new JPanel();
        panelL.setLayout(new BoxLayout(panelL, BoxLayout.X_AXIS));
        panelL.setBackground(Color.DARK_GRAY);

        panelR = new JPanel();
        panelR.setLayout(new BoxLayout(panelR, BoxLayout.X_AXIS));
        panelR.setBackground(Color.DARK_GRAY);

        cancel = new JLabel("Cancel");
        cancel.setOpaque(true);
        cancel.setBackground(Color.GRAY);
        cancel.setForeground(Color.WHITE);
        cancel.setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 2), new EmptyBorder(2, 4, 2, 4)));

        save = new JLabel("Save");
        save.setOpaque(true);
        save.setBackground(Color.GRAY);
        save.setForeground(Color.WHITE);
        save.setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 2), new EmptyBorder(2, 4, 2, 4)));

        panelL.add(cancel);
        panelR.add(save);
        labelComponents.add(cancel);
        labelComponents.add(save);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(panelL);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(panelR);
        buttonPanel.add(Box.createHorizontalGlue());
        return buttonPanel;
    }

    private Component createHeader() {
        header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(Color.DARK_GRAY);

        text = new JLabel("Game Settings");
        text.setFont(new Font(null, Font.PLAIN, (int) (Game.screenHeight*0.0277)));
        text.setBackground(Color.DARK_GRAY);
        text.setForeground(Color.WHITE);
        text.setOpaque(true);

        header.add(text);
        return header;
    }

    private Component createCom(int i) {
        comPanel = new JPanel();
        comPanel.setBackground(Color.DARK_GRAY);
        if (i == 1) {
            comPanel.setBorder(new MatteBorder(2, 0, 2, 0, Color.WHITE));
        } else if (i == 5) {
            comPanel.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        } else {
            comPanel.setBorder(new MatteBorder(2, 0, 0, 0, Color.WHITE));
        }

        textPanel = new JPanel();
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 100/1920)), (int) (Game.screenHeight * ((double) 40/1080))));

        text = new JLabel();
        text.setForeground(Color.WHITE);
        text.setBackground(Color.DARK_GRAY);
        text.setOpaque(true);
        switch (i) {
            case 1:
                text.setText("Color");
                textPanel.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 100/1920)), (int) (Game.screenHeight * ((double) 40/1080))));
                comPanel.setLayout(new BoxLayout(comPanel, BoxLayout.Y_AXIS));
                break;
            case 2:
                text.setText("Time");
                comPanel.setLayout(new BoxLayout(comPanel, BoxLayout.X_AXIS));
                break;
            case 3:
                text.setText("Increment");
                comPanel.setLayout(new BoxLayout(comPanel, BoxLayout.X_AXIS));
                break;
            case 4:
                text.setText("Style");
                comPanel.setLayout(new BoxLayout(comPanel, BoxLayout.X_AXIS));
                break;
            case 5:
                text.setText("Difficulty");
                comPanel.setLayout(new BoxLayout(comPanel, BoxLayout.X_AXIS));
                break;
        }
        textPanel.add(text);

        componentPanel = new JPanel();
        componentPanel.setBackground(Color.DARK_GRAY);
        switch (i) {
            case 1:
                componentPanel.add(createColorSelection());
                break;
            case 2:
                componentPanel.add(createTimeSelection(timeSettings, i));
                break;
            case 3:
                componentPanel.add(createTimeSelection(incrementSettings, i));
                break;
            case 4:
                componentPanel.add(createTimeSelection(styleSettings, i));
                break;
            case 5:
                componentPanel.add(createTimeSelection(difficultySettings, i));
                break;
        }
        comPanel.add(textPanel);
        comPanel.add(componentPanel);

        if (i == 5) {
            difficultyPanel = componentPanel;
            difficultyTextPanel = textPanel;
            difficultyTextLabel = text;
        }

        return comPanel;
    }

    private Component createTimeSelection(String[] items, int i) {
        combobox = new JComboBox<>(items);
        combobox.setFocusable(false);
        combobox.setBackground(Color.BLACK);
        combobox.setForeground(Color.WHITE);
        combobox.setBorder(new LineBorder(Color.WHITE, 1));
        combobox.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 150/1920)), (int) (Game.screenHeight * ((double) 30/1080))));
        combobox.setUI(new MyComboBoxUI());
        combobox.setFont(new Font(null, Font.PLAIN, (int) (Game.screenHeight*0.0138)));
        combobox.setMaximumRowCount(combobox.getItemCount());

        if (i == 2) {
            combobox.setSelectedItem("5:00");
        } else if (i == 3) {
            combobox.setSelectedItem("+ 3");
        } else if (i == 4) {
            combobox.setSelectedItem("Player vs. Player");
        } else if (i == 5) {
            combobox.setSelectedItem("1");
        }

        comboComponents.add(combobox);
        return combobox;
    }

    private Component createColorSelection() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension((int) (Game.screenWidth * ((double) 320/1920)), (int) (Game.screenHeight * ((double) 100/1080))));
        panel.setBackground(Color.DARK_GRAY);

        white = new JLabel(new ImageIcon(piecesImg[0]));
        white.setOpaque(true);
        white.setBackground(Color.DARK_GRAY);
        white.setBorder(new EmptyBorder(2, 2, 2, 2));

        random = new JLabel(new ImageIcon(piecesImg[1]));
        random.setOpaque(true);
        random.setBackground(Color.DARK_GRAY);
        random.setBorder(new EmptyBorder(2, 2, 2, 2));

        black = new JLabel(new ImageIcon(piecesImg[2]));
        black.setOpaque(true);
        black.setBackground(Color.DARK_GRAY);
        black.setBorder(new EmptyBorder(2, 2, 2, 2));

        panel.add(Box.createHorizontalGlue());
        panel.add(white);
        panel.add(random);
        panel.add(black);
        panel.add(Box.createHorizontalGlue());
        labelComponents.add(white);
        labelComponents.add(random);
        labelComponents.add(black);

        labelComponents.get(Settings.color).setBorder(new LineBorder(Color.WHITE, 2));
        labelComponents.get(Settings.color).setBackground(Color.GRAY);

        return panel;
    }

    private void importImg() {
        InputStream is1 = getClass().getResourceAsStream("/main/ressources/Pieces.png");
        InputStream is2 = getClass().getResourceAsStream("/main/ressources/Random.png");
        InputStream is3 = getClass().getResourceAsStream("/main/ressources/CheckboxClear.png");
        InputStream is4 = getClass().getResourceAsStream("/main/ressources/CheckboxFull.png");

        try {
            img = ImageIO.read(is1);
            piecesImg[1] = scaleImage(ImageIO.read(is2), Board.squareSize, Board.squareSize);
            imgPassive = scaleImage(ImageIO.read(is3), (int) (Game.screenHeight * ((double)25 / 1080)), (int) (Game.screenHeight * ((double)25 / 1080)));
            imgActive = scaleImage(ImageIO.read(is4), (int) (Game.screenHeight * ((double)25 / 1080)), (int) (Game.screenHeight * ((double)25 / 1080)));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void addImg() {
        piecesImg[0] = scaleImage(img.getSubimage(0, 0, 100, 100), Board.squareSize, Board.squareSize);
        piecesImg[2] = scaleImage(img.getSubimage(0, 100, 100, 100), Board.squareSize, Board.squareSize);
    }

    public BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return scaledImage;
    }

    private static ImageIcon arrow() {
        BufferedImage image = new BufferedImage(8, 6, BufferedImage.TRANSLUCENT);

        Graphics2D g = image.createGraphics();
        int[] x = { 0, image.getWidth() / 2, image.getWidth() };
        int[] y = { 0, image.getHeight(), 0 };
        g.fillPolygon(x, y, x.length);

        return new ImageIcon(image);
    }

    public void enableAI() {
        changeImg();
        ImgActive = false;

        difficultyPanel.setBackground(Color.DARK_GRAY);
        difficultyTextPanel.setBackground(Color.DARK_GRAY);
        difficultyTextLabel.setBackground(Color.DARK_GRAY);
        difficultyTextLabel.setForeground(Color.WHITE);

        checkPanel.setBackground(Colors.disabledBackground);
        checkTextPanel.setBackground(Colors.disabledBackground);
        checkTextLabel.setForeground(Colors.disabledText);

        comboComponents.get(3).setUI(new MyComboBoxUI());
        comboComponents.get(3).setEnabled(true);
    }

    public void enablePlayerVsPlayer() {
        difficultyPanel.setBackground(Colors.disabledBackground);
        difficultyTextPanel.setBackground(Colors.disabledBackground);
        difficultyTextLabel.setBackground(Colors.disabledBackground);
        difficultyTextLabel.setForeground(Colors.disabledText);

        checkPanel.setBackground(Color.DARK_GRAY);
        checkTextPanel.setBackground(Color.DARK_GRAY);
        checkTextLabel.setForeground(Color.WHITE);

        comboComponents.get(3).setUI(new DisabledComboBoxUI());
        comboComponents.get(3).setEnabled(false);
    }

    public static class DisabledComboBoxUI extends BasicComboBoxUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            LookAndFeel.uninstallBorder(comboBox); // Uninstalls the LAF border for both button and label of combo box.
        }

        @Override
        protected JButton createArrowButton() {
            // Feel free to play with the colors:
            final JButton button = new JButton(arrow());
            button.setName("ComboBox.arrowButton"); // Mandatory, as per BasicComboBoxUI#createArrowButton().
            button.setBorder(new EmptyBorder(5, 5, 5, 5));
            // button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            // button.setBorderPainted(false);
            return button;

        }

        @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer<Object> renderer = comboBox.getRenderer();
        Component c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
        c.setFont(comboBox.getFont());
        c.setForeground(comboBox.getForeground());
        c.setBackground(comboBox.getBackground());
        if (hasFocus && !isPopupVisible(comboBox)) {
            c.setForeground(listBox.getSelectionForeground());
            c.setBackground(listBox.getSelectionBackground());
        }
        Rectangle r = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        SwingUtilities.paintComponent(g, c, comboBox, r);
    }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            
            Color t = g.getColor();

            g.setColor(Color.BLACK);

            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(t);
        }
    }
}
