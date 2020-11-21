package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/20/20
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.SlangDictionary.MapController;

public class SearchBySlangCard extends JPanel {
    private JTextField textInput = null;
    private JButton inputBtn = null;
    private JButton randomBtn = null;
    private JLabel definition = null;

    public SearchBySlangCard(){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        textInput = new JTextField(20);
        textInput.setFont(new Font("SF Mono", Font.PLAIN,18));
        JLabel inputLabel = new JLabel("Input slang word: ");
        inputBtn = new JButton();
        inputBtn.setIcon(new ImageIcon("./assets/icons/ic_search.png"));
        inputBtn.setPreferredSize(new Dimension(50,26));
        randomBtn = new JButton();
        randomBtn.setIcon(new ImageIcon("./assets/icons/ic_dice.png"));
        randomBtn.setPreferredSize(new Dimension(50,26));
        randomBtn.setToolTipText("Randomly generate an icon");
        inputPanel.add(inputLabel);
        inputPanel.add(textInput);
        inputPanel.add(inputBtn);
        inputPanel.add(randomBtn);

        JPanel descPanel = new JPanel();
        descPanel.setAlignmentX(0);
        descPanel.setLayout(new FlowLayout());
        JLabel descLabel = new JLabel("Description: ");
        definition = new JLabel("Happy");
        descPanel.add(descLabel);
        descPanel.add(definition);

        add(inputPanel, BorderLayout.PAGE_START);
        add(descPanel, BorderLayout.CENTER);
    }

    public void setMapController(MapController map){
        inputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String def = map.getDefinitionWithRecord(textInput.getText());
                if (!def.equals("")) {
                    textInput.setBorder(BorderFactory.createLineBorder(Color.black));
                    definition.setText(def);
                }
                else {
                    textInput.setBorder(BorderFactory.createLineBorder(Color.red));
                    definition.setText("Not Found!!");
                }
            }
        });

        randomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String randomKey = map.getRandomKeys(1)[0];
                textInput.setText(randomKey);
                textInput.setBorder(BorderFactory.createLineBorder(Color.black));
                definition.setText(map.getDefinition(randomKey));
            }
        });

        textInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                inputBtn.doClick();
            }
        });
    }
}
