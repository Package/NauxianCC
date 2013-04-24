package org.nauxiancc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Used to refine the project panels based off the user's
 * discretion and what they want to do.
 *
 * @author Naux
 * @version 1.0
 * @since 1.0
 */

public class SearchPanel extends JPanel {

    private static final String DEFAULT = "Search...";
    private static final Dimension OPTION_SIZE = new Dimension(100, 35);

    private final JCheckBox complete;
    private final JCheckBox name;
    private final JCheckBox incomplete;

    /**
     * Constructs a new search panel. This ideally
     * should only be done once.
     *
     * @since 1.0
     */

    public SearchPanel() {
        super(new BorderLayout());
        final JPanel options = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JPanel padding = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JTextField search = new JTextField();
        final ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                search(search.getText());
            }
        };

        complete = new JCheckBox("Complete");
        incomplete = new JCheckBox("Incomplete");
        name = new JCheckBox("Name");

        complete.setPreferredSize(OPTION_SIZE);
        incomplete.setPreferredSize(OPTION_SIZE);
        name.setPreferredSize(OPTION_SIZE);

        options.add(complete);
        options.add(incomplete);
        options.add(name);

        complete.setSelected(true);
        incomplete.setSelected(true);
        name.setSelected(true);

        name.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                search.setEnabled(name.isSelected());
                search(search.getText());
            }
        });
        complete.addItemListener(listener);
        incomplete.addItemListener(listener);

        padding.add(search);
        search.setPreferredSize(new Dimension(310, 20));
        search.setText(DEFAULT);
        search.setForeground(Color.DARK_GRAY);
        search.setFont(search.getFont().deriveFont(Font.ITALIC));
        search.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals(DEFAULT)) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                    search.setFont(search.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().trim().isEmpty()) {
                    search.setText(DEFAULT);
                    search.setForeground(Color.DARK_GRAY);
                    search.setFont(search.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search(search.getText());
            }
        });
        add(padding, BorderLayout.CENTER);
        add(options, BorderLayout.SOUTH);
    }

    /**
     * Standardizes the search key and passes conditions into
     * the refining process for the panels.
     *
     * @since 1.0
     * @param key       The key to search for. Defaults to empty.
     */

    public synchronized void search(final String key) {
        GUI.getSelector().refine(name.isSelected() ? key.replace(" ", "").replace(DEFAULT, "") : "", complete.isSelected(), name.isSelected(), incomplete.isSelected());
    }

}
