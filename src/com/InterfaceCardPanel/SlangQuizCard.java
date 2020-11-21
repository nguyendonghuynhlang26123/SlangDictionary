package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/21/20
*/

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class SlangQuizCard extends JPanel {
    private JLabel quizLabel = null;
    private JLabel quizQuestion = null;
    private JButton[] btns = new JButton[4];
    private String[] data = null;
    private int answer = -1;
    private Callable<String[]> getQuizData = null;

    private String[] getKeysAndAnswer(){
        try {
            String[] rawString = getQuizData.call();
            String[] temp = rawString[0].split(",");
            answer = Integer.parseInt(temp[0]);
            rawString[0] = temp[1];
            return rawString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SlangQuizCard(Callable<String[]> generateKeys){
        getQuizData = generateKeys;
        data = getKeysAndAnswer();
        if (data == null) add(new JLabel("ERROR"));
        setLayout(new BorderLayout());

        quizLabel = new JLabel("Quiz#1");

        JPanel quizContent = new JPanel();
        quizContent.setLayout(new BorderLayout());
        quizQuestion = new JLabel("What is the definition of " + data[0]);
        quizContent.add(quizQuestion, BorderLayout.PAGE_START);
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2,2));
        for (int i = 0; i < 4; i++) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new FlowLayout());
            btns[i] = new JButton(data[i+1]);
            btns[i].setFont(new Font("SF Mono", Font.PLAIN,14));
            btns[i].setMargin(new Insets(0,0,0,0));
            btns[i].setPreferredSize(new Dimension(200, 100));
            btns[i].setBackground(Color.decode("#16697a"));
            btns[i].setForeground(Color.white );
            wrapper.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

            wrapper.add(btns[i]);
            answerPanel.add(wrapper);
        }
        quizContent.add(answerPanel);

        add(quizLabel, BorderLayout.PAGE_START);
        add(quizContent, BorderLayout.CENTER);
    }
}
