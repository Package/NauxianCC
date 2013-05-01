package org.nauxiancc.gui;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.projects.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabPane extends JPanel {

    private static final Dimension TAB_SIZE = new Dimension(170, 30);

    public TabPane(final Project project) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 2));
        final JLabel label = new JLabel(project.getName());
        final JPanel buffer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton close = new JButton(new ImageIcon(Global.getImage(Global.CLOSE_IMAGE).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        buffer.setOpaque(false);
        buffer.setPreferredSize(new Dimension(146, 24));
        buffer.add(label);

        close.setPreferredSize(new Dimension(24, 24));
        close.setOpaque(false);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.removeTab(project.getName());
            }
        });

        add(buffer);
        add(close);
        setOpaque(false);
        setPreferredSize(TAB_SIZE);
    }
}
