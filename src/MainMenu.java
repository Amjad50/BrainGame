import BrainGame.GUI.MainApplication;
import java.awt.event.*;
import javax.swing.*;


public class MainMenu {

    JFrame Menu = new JFrame("Brain Game");
    JButton Start = new JButton("Play");
    JButton Exit = new JButton("Exit");
    ImageIcon picture = new ImageIcon("Brain.png");
    JLabel imageLabel = new JLabel(picture);
    int menuWidth = 100; //Width of each button/item on display
    int menuHeight = 30;//Height of each button/item on display
    int menuY = 460; //Button/item location on display
    int WIDTH = 490; 
    int HEIGHT = 530; 

    public MainMenu() {
        
        Menu.setResizable(false);
        Menu.setSize(WIDTH, HEIGHT);
        Menu.setLayout(null);
        Menu.setLocationRelativeTo(null);
        Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Start.setSize(menuWidth, menuHeight);
        Start.setLocation(10, menuY);
        Menu.add(Start);
        Start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                //here the action to move to next window
                Menu.setVisible(false);
                new MainApplication().run();
                Menu.dispose();
            }

        });
        
        
        Exit.setSize(menuWidth, menuHeight);
        Exit.setLocation(375, menuY);
        Menu.add(Exit);
        Exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        imageLabel.setBounds(0, 0, 500, 500);
        imageLabel.setVisible(true);
        Menu.add(imageLabel);
        Menu.setVisible(true);
    }

}