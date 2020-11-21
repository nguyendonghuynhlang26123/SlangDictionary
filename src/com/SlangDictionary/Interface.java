package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.InterfaceCardPanel.*;

public class Interface {
    private static final String[] buttonLabels = {
            "Search by Slang word",
            "Search by Definition",
            "Search history",
            "Add a new Slang Word",
            "Modify existing Slang Word",
            "Random a Slang word",
            "Slang quiz"
    };
    private static MapController map = null;

    public static void addSearchBySlangCard(Container pane) {
        SearchBySlangCard card = new SearchBySlangCard();
        pane.add(card);
        card.setMapController(map);
    }

    public static void addHistoryCard(Container panel){
        HistoryCard card = new HistoryCard(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return map.getHistory();
            }
        });

        panel.add(card);
    }

    public static void addComponentsToPane(Container pane){
        pane.setLayout(new BorderLayout());
        pane.setSize(new Dimension(500,600 ));

        //Panel 1:
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        //Panel 2:
        JPanel mainPanel = new JPanel(new CardLayout());

        for (String buttonLabel : buttonLabels) {
            JButton newBtn = new JButton(buttonLabel);
            newBtn.setMargin(new Insets(15, 15, 15, 15));
            newBtn.setMaximumSize(new Dimension(100, 50));
            newBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    Object o = event.getSource();
                    JButton b = null;
                    String buttonText = "";

                    if(o instanceof JButton)
                        b = (JButton)o;

                    if(b != null)
                        buttonText = b.getText();

                    CardLayout cl = (CardLayout)(mainPanel.getLayout());
                    cl.next(mainPanel);
                }
            });
            sidebar.add(newBtn);
        }

        addSearchBySlangCard(mainPanel);
        addHistoryCard(mainPanel);

        pane.add(sidebar, BorderLayout.LINE_START);
        pane.add(mainPanel, BorderLayout.CENTER);
    }

    private static void createAndShowGUI()
    {
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("District Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        frame.setPreferredSize(new Dimension(900,400));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Helvetica Neue", Font.PLAIN,20));
        UIManager.put("Button.font", new Font("SF Mono", Font.BOLD,18));

        map = new MapController();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
