package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/21/20
*/

import com.SlangDictionary.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

public class SlangQuizCard extends JPanel {
    private final int LINE_SIZE = 10;
    private JLabel quizLabel;
    private JLabel countLabel;
    private JLabel quizQuestion;
    private JButton[] btns = new JButton[4];
    private String[] data;
    private String answer = "";
    private int answerCount = 1;
    private int rightAnsCount = 0;
    private Callable<String[]> getQuizData;

    private String wrapText(String text){
        String[] splited = text.split(" ");
        if (text.length() >= LINE_SIZE){
            int line = 0;
            for (int i = 0; i < splited.length; i++) {
                line += splited[i].length();
                if (line > LINE_SIZE){
                    splited[i] += "<br>";
                    line = 0;
                }
            }
        }

        return "<html>" + String.join(" ", splited) + "</html>";
    }

    private String[] getKeysAndAnswer(){
        try {
            String[] rawString = getQuizData.call();
            String[] temp = rawString[0].split(",");
            answer = temp[0];
            rawString[0] = temp[1];
            return rawString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SlangQuizCard(String label, Callable<String[]> generateKeys){
        getQuizData = generateKeys;
        data = getKeysAndAnswer();
        if (data == null) add(new JLabel("ERROR"));
        setLayout(new BorderLayout());

        quizLabel = new JLabel("Quiz#1", SwingConstants.CENTER);
        countLabel = new JLabel("Answer: 1/1", SwingConstants.CENTER);

        JPanel quizContent = new JPanel();
        quizContent.setLayout(new BorderLayout());
        quizQuestion = new JLabel("<html>What is the " + label + " for <b>"
                + data[0] + "</b>?</html>", SwingConstants.CENTER);
        quizContent.setAlignmentX(CENTER_ALIGNMENT);
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2,2));
        for (int i = 0; i < 4; i++) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new FlowLayout());
            btns[i] = new JButton(wrapText(data[i+1]));
            btns[i].setFont(new Font("SF Mono", Font.PLAIN,14));
            btns[i].setMargin(new Insets(0,0,0,0));
            btns[i].setPreferredSize(new Dimension(200, 100));
            btns[i].setBackground(Color.decode("#16697a"));
            btns[i].setForeground(Color.white );
            wrapper.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

            wrapper.add(btns[i]);
            answerPanel.add(wrapper);
        }

        quizContent.add(quizQuestion, BorderLayout.PAGE_START);
        quizContent.add(answerPanel, BorderLayout.CENTER);

        add(quizLabel, BorderLayout.PAGE_START);
        add(quizContent, BorderLayout.CENTER);
        add(countLabel, BorderLayout.PAGE_END);

        addActionEvent();
    }

    private void popup(String msg){
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        JOptionPane.showMessageDialog(frame,
                msg,
                "Quiz",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeQuiz(){
        answerCount++;
        quizLabel.setText("Quiz#" + answerCount);

        data = getKeysAndAnswer();
        if (data == null) add(new JLabel("ERROR"));
        quizQuestion.setText("<html>What is the definition of <b>"
                + data[0] + "</b>?</html>");
        for (int i = 0; i < btns.length; i++) {
            btns[i].setText(wrapText(data[i + 1]));
        }

        countLabel.setText("Right Answer: " + rightAnsCount + "/" + answerCount);
    }

    private void addActionEvent(){
        for (int i = 0; i < btns.length; i++) {
            JButton btn = btns[i];
            int finalI = i;
            btn.addActionListener(actionEvent -> {
                JButton btn1 = (JButton) actionEvent.getSource();

                if (data[finalI+1].equals(answer)) {
                    popup("Amazing! You get it right!\n");
                    rightAnsCount++;
                }
                else popup("Wrong answer!\nThe answer is " + answer);
                changeQuiz();
            });
        }
    }
}
