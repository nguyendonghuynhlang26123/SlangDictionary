package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/20/20
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.SlangDictionary.MapController;

public class SearchBySlangCard extends JPanel {
    private JTextField textInput = null;
    private JButton inputBtn = null;
    private JLabel definition = null;

    public SearchBySlangCard(){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        textInput = new JTextField(20);
        textInput.setFont(new Font("SF Mono", Font.PLAIN,18));
        JLabel inputLabel = new JLabel("Input slang word: ");
        textInput.setBorder(BorderFactory.createLineBorder(Color.red));
        inputBtn = new JButton();
        inputBtn.setIcon(new ImageIcon("./assets/icons/ic_search.png"));
        inputBtn.setPreferredSize(new Dimension(50,26));
        inputPanel.add(inputLabel);
        inputPanel.add(textInput);
        inputPanel.add(inputBtn);

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
                String def = map.getDefinition(textInput.getText());
                if (!def.equals("")) definition.setText(def);
                else definition.setText("Not Found!!");
            }
        });
    }
}
