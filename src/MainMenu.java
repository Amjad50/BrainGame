import BrainGame.GUI.MainApplication;

import javax.swing.*;


public class MainMenu {

    private static final int menuWidth = 100, //Width of each button/item on display
            menuHeight = 30, //Height of each button/item on display
            menuY = 460, //Button/item location on display
            WIDTH = 490,
            HEIGHT = 530;
    private JFrame Menu;

    public MainMenu() {

        Menu = new JFrame("Brain Game");
        Menu.setResizable(false);
        Menu.setSize(WIDTH, HEIGHT);
        Menu.setLayout(null);
        Menu.setLocationRelativeTo(null);
        Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton start = new JButton("Play");
        start.setSize(menuWidth, menuHeight);
        start.setLocation(10, menuY);
        Menu.add(start);
        start.addActionListener(e -> {
            //here the action to move to next window
            Menu.setVisible(false);
            new MainApplication().run();
            Menu.dispose();
        });


        JButton exit = new JButton("Exit");
        exit.setSize(menuWidth, menuHeight);
        exit.setLocation(375, menuY);
        Menu.add(exit);
        exit.addActionListener(e -> System.exit(0));

        ImageIcon picture = new ImageIcon("images/Brain.png");
        JLabel imageLabel = new JLabel(picture);
        imageLabel.setBounds(0, 0, 500, 500);
        imageLabel.setVisible(true);
        Menu.add(imageLabel);
        Menu.setVisible(true);
    }

}