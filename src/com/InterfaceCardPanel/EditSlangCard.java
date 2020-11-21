package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/21/20
*/

import com.SlangDictionary.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditSlangCard extends JPanel {
    private JTextField slangInput = null;
    private JTextArea descInput = null;
    private JButton inputBtn = null;
    private JButton submitBtn = null;
    private JButton deleteBtn = null;
    private JPanel descPanel = null;

    public EditSlangCard(){
        setLayout(new BorderLayout());

        JPanel slangPanel = new JPanel();
        slangPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        slangInput = new JTextField(20);
        slangInput.setFont(new Font("SF Mono", Font.PLAIN, 18));
        JLabel inputLabel = new JLabel("Slang word: ");
        slangInput.setBorder(BorderFactory.createLineBorder(Color.black));
        inputBtn = new JButton("Search");
        inputBtn.setPreferredSize(new Dimension(100,26));
        slangPanel.add(inputLabel);
        slangPanel.add(slangInput);
        slangPanel.add(inputBtn);

        descPanel = new JPanel();
        descPanel.setVisible(false);
        descInput = new JTextArea(3, 20);
        descInput.setLineWrap(true);
        descInput.setWrapStyleWord(true);
        descInput.setFont(new Font("SF Mono", Font.PLAIN, 18));
        JLabel descLabel = new JLabel("Meaning: ");
        descInput.setBorder(BorderFactory.createLineBorder(Color.black));
        descPanel.add(descLabel);
        descPanel.add(descInput);

        JPanel btnPanel = new JPanel();
        submitBtn = new JButton("Save");
        submitBtn.setPreferredSize(new Dimension(200, 40));
        submitBtn.setFont(new Font("SF Mono", Font.BOLD, 14));
        submitBtn.setBackground(Color.cyan);
        submitBtn.setOpaque(true);
        submitBtn.setVisible(false);

        deleteBtn = new JButton("Discard this word");
        deleteBtn.setPreferredSize(new Dimension(220, 40));
        deleteBtn.setFont(new Font("SF Mono", Font.BOLD, 14));
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setOpaque(true);
        deleteBtn.setVisible(false);
        btnPanel.add(submitBtn);
        btnPanel.add(deleteBtn);

        add(slangPanel, BorderLayout.PAGE_START);
        add(descPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.PAGE_END);
    }

    private void toggle( ){
        submitBtn.setVisible(!submitBtn.isVisible());
        deleteBtn.setVisible(!deleteBtn.isVisible());
        descPanel.setVisible(!descPanel.isVisible());
    }

    public void addActionEvent(MapController map){
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        inputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String slang = slangInput.getText();
                if (map.hasKey(slang)) {
                    descInput.setText(map.getDefinition(slang));
                    toggle();
                }
                else{
                    JOptionPane.showMessageDialog(frame,
                            "Cannot find this word!",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String slang = slangInput.getText();
                String mean = descInput.getText();
                if (map.getDefinition(slang).equals(mean)) {
                    JOptionPane.showMessageDialog(frame,
                            "Detect no change in meaning!",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int c = JOptionPane.showConfirmDialog(
                        frame,
                        "Update this word?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION){
                    boolean succeed = map.addToExFile(slang, mean);

                    if (succeed) {
                        JOptionPane.showMessageDialog(frame,
                                "Successfully update this word",
                                "Status",
                                JOptionPane.INFORMATION_MESSAGE);
                        toggle();
                    }
                    else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to update this word!! Please try again",
                                "Status",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String slang = slangInput.getText();

                int c = JOptionPane.showConfirmDialog(
                        frame,
                        "Delete this word?",
                        "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                if (c == JOptionPane.YES_OPTION){
                    boolean succeed = map.removeAWord(slang);

                    if (succeed) {
                        JOptionPane.showMessageDialog(frame,
                                "Successfully delete this word",
                                "Status",
                                JOptionPane.INFORMATION_MESSAGE);
                        toggle();
                    }
                    else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to delete this word!! Please try again",
                                "Status",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
