package com.InterfaceCardPanel;/*
    @author Nguyen Dang Huynh Long
    @date 11/20/20
*/

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.Callable;

public class HistoryCard extends JPanel {
    private JList<String> list = null;
    DefaultListModel<String> model = new DefaultListModel<>();

    public HistoryCard(Callable<String[]> loadHistoryFunction){
        setLayout(new GridLayout(0,1));
        setBorder(BorderFactory.createLineBorder(Color.red));
        list = new JList<>(model);
        list.setFixedCellHeight(50);

        list.setCellRenderer(new MyCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(listScroller);

        //Action Event
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                model.clear();
                try {
                    String [] history = loadHistoryFunction.call();
                    for (String s : history) {
                        model.addElement(s);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                super.componentShown(e);
            }
        });
    }

}

class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
    public MyCellRenderer() {
        setFont(new Font("Helvetica Neue", Font.PLAIN,18));
        setBorder(new EmptyBorder(10,10, 10, 10));
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        setText(value.toString());

        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

            // check if this cell is selected
        } else if (isSelected) {
            background = Color.RED;
            foreground = Color.WHITE;

            // unselected, and not the DnD drop location
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        };

        setBackground(background);
        setForeground(foreground);

        return this;
    }
}
