package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;

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
    private static HashMap<String, String> map = null;

    public static void addSearchBySlangCard(Container pane) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JTextField textInput = new JTextField(30);
        textInput.setFont(new Font("SF Mono", Font.PLAIN,18));
        JLabel inputLabel = new JLabel("Input slang word: ");
        textInput.setBorder(BorderFactory.createLineBorder(Color.red));
        inputPanel.add(inputLabel);
        inputPanel.add(textInput);

        JPanel descPanel = new JPanel();
        descPanel.setAlignmentX(0);
        descPanel.setLayout(new FlowLayout());
        JLabel descLabel = new JLabel("Description: ");
        JLabel definition = new JLabel("Happy");
        descPanel.add(descLabel);
        descPanel.add(definition);

        card.add(inputPanel, BorderLayout.PAGE_START);
        card.add(descPanel, BorderLayout.CENTER);
        pane.add(card);
    }

    public static void addComponentsToPane(Container pane){
        pane.setLayout(new BorderLayout());
        pane.setSize(new Dimension(500,600 ));

        //Panel 1:
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        for (String buttonLabel : buttonLabels) {
            JButton newBtn = new JButton(buttonLabel);
            newBtn.setMargin(new Insets(15, 15, 15, 15));
            newBtn.setMaximumSize(new Dimension(100, 50));
            sidebar.add(newBtn);
        }

        //Panel 2:
        JPanel mainPanel = new JPanel(new CardLayout());
        addSearchBySlangCard(mainPanel);

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

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
