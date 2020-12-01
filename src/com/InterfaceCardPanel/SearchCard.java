package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/20/20
*/

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class SearchCard extends JPanel {
    private JTextField textInput = null;
    private JButton inputBtn = null;
    private JButton randomBtn = null;
    private JLabel definition = null;
    //private final String searchIcon = "./assets/icons/ic_search.png";
    //private final String randomIcon = "./assets/icons/ic_dice.png";
    private final URL searchIcon = getClass().getClassLoader().getResource("icons/ic_search.png");
    private final URL randomIcon = getClass().getClassLoader().getResource("icons/ic_dice.png");

    public SearchCard(boolean hasRandom){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        textInput = new JTextField(20);
        textInput.setFont(new Font("SF Mono", Font.PLAIN,18));
        JLabel inputLabel = new JLabel("Input keyword: ");
        inputBtn = new JButton();
        inputBtn.setIcon(new ImageIcon(searchIcon));
        inputBtn.setPreferredSize(new Dimension(50,26));
        inputPanel.add(inputLabel);
        inputPanel.add(textInput);
        inputPanel.add(inputBtn);
        if (hasRandom){
            randomBtn = new JButton();
            randomBtn.setIcon(new ImageIcon(randomIcon));
            randomBtn.setPreferredSize(new Dimension(50,26));
            randomBtn.setToolTipText("Randomly generate an icon");
            inputPanel.add(randomBtn);
        }

        JPanel descPanel = new JPanel();
        descPanel.setAlignmentX(20);
        descPanel.setLayout(new FlowLayout());
        JLabel descLabel = new JLabel("Result: ");
        definition = new JLabel("");
        descPanel.add(descLabel);
        descPanel.add(definition);

        add(inputPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(descPanel), BorderLayout.CENTER);
    }

    public void setMapController(Function<String, String> getDef, Callable<String[]> randomHanlder){
        inputBtn.addActionListener(actionEvent -> {
            String def = getDef.apply(textInput.getText());
            if (!def.equals("") && def != null) {
                textInput.setBorder(BorderFactory.createLineBorder(Color.black));
                definition.setText(def);
            }
            else {
                textInput.setBorder(BorderFactory.createLineBorder(Color.red));
                definition.setText("Not Found!!");
            }
        });

        if (randomBtn != null) {
            randomBtn.addActionListener(actionEvent -> {
                String[] randomKey = new String[0];
                try {
                    randomKey = randomHanlder.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textInput.setText(randomKey[0]);
                textInput.setBorder(BorderFactory.createLineBorder(Color.black));
                definition.setText(randomKey[1]);
            });
        }

        textInput.addActionListener(actionEvent -> inputBtn.doClick());
    }
}
