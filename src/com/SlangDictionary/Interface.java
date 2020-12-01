package com.SlangDictionary;/*
    @author Nguyen Dang Huynh Long
    @date 11/19/20
*/

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;

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
            "Slang quiz 1",
            "Slang quiz 2"
    };
    private static MapController map = null;

    public static void addSearchBySlangCard(Container pane) {
        SearchCard card = new SearchCard(true);
        Function<String, String> getDefFn = (s) -> {
            String def = map.getDefinitionWithRecord(s);
            if (def.equals("")) return "";
            String result = String.join("<br/>+", def.split("\\|"));
            return "<html>+" + result + "</html>" ;
        } ;
        Callable<String[]> getRandomValuesFn = () ->{
           String[] result = new String[2];
           result[0] = map.getRandomKeys(1)[0];
           result[1] = map.getDefinition(result[0]);
           return result;
        };
        card.setMapController(getDefFn, getRandomValuesFn);

        pane.add(card, buttonLabels[0]);
    }

    public static void addSearchByDefCard(Container pane) {
        SearchCard card = new SearchCard(false);
        Function<String, String> getDefFn = (s) -> {
            String[] keys = map.getSlangWordsByDef(s);
            if (keys == null) return "";
            StringBuilder result = new StringBuilder();
            for (String key : keys) {
                String temp = String.format("+ <b>%s</b> - %s <br/>", key, map.getDefinition(key));
                result.append(temp);
            }
            return "<html>" + result + "</html>";
        };
        card.setMapController(getDefFn, null);

        pane.add(card, buttonLabels[1]);
    }

    public static void addHistoryCard(Container panel){
        HistoryCard card = new HistoryCard(() -> map.getHistory());

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

    public static void addSlangQuizCard(Container pane) {
        SlangQuizCard card = new SlangQuizCard("definition", () -> {
            String[] data = new String[5];
            String[] keys = map.getRandomKeys(4);
            int randomIdx = new Random().nextInt(4);

            data[0] = map.getDefinition(keys[randomIdx]) + "," + keys[randomIdx];
            for (int i = 0; i < 4; i++) {
                data[i+1] = map.getDefinition(keys[i]);
            }
            return data;
        });

        pane.add(card, buttonLabels[5]);
    }

    public static void addSlangQuiz2Card(Container pane) {
        SlangQuizCard card = new SlangQuizCard("slang word", () -> {
            String[] data = new String[5];
            String[] keys = map.getRandomKeys(4);
            int randomIdx = new Random().nextInt(4);

            data[0] = keys[randomIdx] + "," + map.getDefinition(keys[randomIdx]);
            System.arraycopy(keys, 0, data, 1, 4);
            return data;
        });

        pane.add(card, buttonLabels[6]);
    }

    public static void addResetFunction(JFrame frame){
        int res = JOptionPane.showConfirmDialog(frame,
                "By clicking OK all of your additional data will be deleted!\nAre you sure about that?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if(res == JOptionPane.YES_OPTION) {
            boolean succeed = map.resetExFile();
            if (succeed) {
                JOptionPane.showMessageDialog(frame,
                        "Successfully reset Slang Dictionary",
                        "Status",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Failed to reset Slang Dictionary!! Please try again",
                        "Status",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
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

            newBtn.addActionListener(event -> {
                sidebar.getComponent(selected).setBackground(primaryColor);
                ((Component)event.getSource()).setBackground(secondaryColor);
                selected = indx;
                CardLayout cl = (CardLayout )(mainPanel.getLayout());
                cl.show(mainPanel, buttonLabel);
            });
            sidebar.add(newBtn);
        }

        JButton resetBtn = new JButton("Reset Dictionary");
        resetBtn.setBackground(Color.decode("#ffa45b"));
        resetBtn.setForeground(Color.white);
        resetBtn.setMargin(new Insets(15, 15, 15, 15));
        resetBtn.setMaximumSize(new Dimension(100, 50));
        resetBtn.addActionListener(actionEvent -> {
            JFrame frame = (JFrame) SwingUtilities.getRoot((Component) actionEvent.getSource());
            addResetFunction(frame);
        });
        sidebar.add(resetBtn);

        addSearchBySlangCard(mainPanel);
        addSearchByDefCard(mainPanel);
        addHistoryCard(mainPanel);
        addAddingNewSlangCard(mainPanel);
        addEditNewSlangCard(mainPanel);
        addSlangQuizCard(mainPanel);
        addSlangQuiz2Card(mainPanel);

        pane.add(sidebar, BorderLayout.LINE_START);
        pane.add(mainPanel, BorderLayout.CENTER);
    }

    private static void createAndShowGUI(){
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Slang Dictionary");
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
        javax.swing.SwingUtilities.invokeLater(() -> {
            map = new MapController();
            createAndShowGUI();
        });
    }

}
