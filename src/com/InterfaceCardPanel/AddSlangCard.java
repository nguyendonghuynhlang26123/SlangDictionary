package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/21/20
*/

import com.SlangDictionary.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSlangCard extends JPanel {
    private JTextField newSlangInput = null;
    private JTextArea descInput = null;
    private JButton submitBtn = null;
    public AddSlangCard() {
        setLayout(new BorderLayout());

        JPanel slangPanel = new JPanel();
        slangPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        newSlangInput = new JTextField(20);
        newSlangInput.setFont(new Font("SF Mono", Font.PLAIN, 18));
        JLabel inputLabel = new JLabel("Slang word: ");
        newSlangInput.setBorder(BorderFactory.createLineBorder(Color.red));
        slangPanel.add(inputLabel);
        slangPanel.add(newSlangInput);

        JPanel descPanel = new JPanel();
        descInput = new JTextArea(3, 20);
        descInput.setLineWrap(true);
        descInput.setWrapStyleWord(true);
        descInput.setFont(new Font("SF Mono", Font.PLAIN, 18));
        JLabel descLabel = new JLabel("Meaning: ");
        descInput.setBorder(BorderFactory.createLineBorder(Color.red));
        descPanel.add(descLabel);
        descPanel.add(descInput);

        JPanel btnPanel = new JPanel();

        submitBtn = new JButton("Add slang word");
        submitBtn.setPreferredSize(new Dimension(200, 40));
        submitBtn.setBackground(Color.cyan);
        submitBtn.setOpaque(true);
        btnPanel.add(submitBtn);

        add(slangPanel, BorderLayout.PAGE_START);
        add(descPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.PAGE_END);
    }

    public void addActionEvent(MapController map){
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String slang = newSlangInput.getText();
                String mean = descInput.getText();
                boolean succeed = false;

                if (slang.equals("") || mean.equals("")){
                    JOptionPane.showMessageDialog(frame,
                            "Input is not supposed to be empty!",
                            "Empty error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    boolean res = map.hasKey(slang);
                    System.out.print(map.getDefinition(slang));

                    if (res){
                        int c = JOptionPane.showConfirmDialog(
                                frame,
                                "This word is existed\nOverwrite?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);
                        if (c == JOptionPane.YES_OPTION){
                            succeed = map.addToExFile(slang, mean);
                        }
                    }
                    else {
                        succeed = map.addToExFile(slang, mean);
                    }
                }

                if (succeed){
                    JOptionPane.showMessageDialog(frame,
                            "Successfully add this word",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(frame,
                            "Add word failed! Please try again",
                            "Status",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
