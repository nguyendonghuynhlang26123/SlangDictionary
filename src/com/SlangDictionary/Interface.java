package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.InterfaceCardPanel.*;

public class Interface {
    private static int selected = 0;
    private static final Color primaryColor = Color.decode("#aee6e6");
    private static final Color secondaryColor = Color.decode("#fbf6f0");
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
        card.setMapController(map);

        pane.add(card, buttonLabels[0]);
    }

    public static void addHistoryCard(Container panel){
        HistoryCard card = new HistoryCard(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return map.getHistory();
            }
        });

        panel.add(card, buttonLabels[2]);
    }

    public static void addAddingNewSlangCard(Container pane) {
        AddSlangCard card = new AddSlangCard();

        card.addActionEvent(map);

        pane.add(card, buttonLabels[3]);
    }

    public static void addEditNewSlangCard(Container pane) {
        EditSlangCard card = new EditSlangCard();

        card.addActionEvent(map);

        pane.add(card, buttonLabels[4]);
    }

    public static void addComponentsToPane(Container pane){
        pane.setLayout(new BorderLayout());
        pane.setSize(new Dimension(500,600 ));

        //Panel 1:
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        //Panel 2:
        JPanel mainPanel = new JPanel(new CardLayout());

        for (int i = 0; i < buttonLabels.length; i++) {
            String buttonLabel = buttonLabels[i];
            final int indx = i;
            JButton newBtn = new JButton(buttonLabel);
            newBtn.setMargin(new Insets(15, 15, 15, 15));
            newBtn.setMaximumSize(new Dimension(100, 50));
            if (buttonLabel.equals(buttonLabels[selected]))
                newBtn.setBackground(secondaryColor);
            else newBtn.setBackground(primaryColor);

            newBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    sidebar.getComponent(selected).setBackground(primaryColor);
                    ((Component)event.getSource()).setBackground(secondaryColor);
                    selected = indx;
                    CardLayout cl = (CardLayout )(mainPanel.getLayout());
                    cl.show(mainPanel, buttonLabel);
                }
            });
            sidebar.add(newBtn);
        }

        addSearchBySlangCard(mainPanel);
        addHistoryCard(mainPanel);
        addAddingNewSlangCard(mainPanel);
        addEditNewSlangCard(mainPanel);

        pane.add(sidebar, BorderLayout.LINE_START);
        pane.add(mainPanel, BorderLayout.CENTER);
    }

    private static void createAndShowGUI(){
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                map = new MapController();
                createAndShowGUI();
            }
        });
    }

}
