package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/21/20
*/

import javax.swing.*;
import java.awt.*;

public class SlangQuizCard extends JPanel {
    JLabel quizLabel = null;
    public SlangQuizCard(){
        setLayout(new BorderLayout());

        quizLabel = new JLabel("Quiz#1");
    }
}
