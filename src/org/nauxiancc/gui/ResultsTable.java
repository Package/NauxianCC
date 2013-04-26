package org.nauxiancc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.nauxiancc.configuration.Global.Paths;
import org.nauxiancc.executor.Result;
import org.nauxiancc.methods.IOUtils;
import org.nauxiancc.projects.Project;

/**
 * This is always linked to a specific Java editor and project. It displays the
 * results of that runner openly with color coordination.
 *
 * @author Naux
 * @since 1.0
 */

public class ResultsTable extends JTable implements TableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 5610470469686875396L;

    private boolean[] resultsCorrect;
    private final Project project;
    private static final Color CORRECT = new Color(0x00A30C);
    private static final String[] HEADERS = new String[]{"Correct Answer", "Your Answer", "Parameters"};

    /**
     * Constructs a new <tt>ResultsTable</tt> instance. This sets all
     * static information in terms of display.
     *
     * @param project The project to which this table will display results of.
     * @since 1.0
     */

    public ResultsTable(final Project project) {
        this.project = project;

        final DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 7189238275994159770L;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        setModel(model);
        setEnabled(false);

        model.setColumnIdentifiers(HEADERS);
        model.setColumnCount(3);
        model.insertRow(0, HEADERS);
    }

    /**
     * Sets the results of the table. This will also check whether or not the
     * result is true and change the color of the cells accordingly. Note: may
     * not work for color blind people.
     *
     * @param results The results returned from the execution of the runner.
     * @since 1.0
     */

    public void setResults(final Result[] results) {
        final DefaultTableModel m = new DefaultTableModel();
        m.setColumnIdentifiers(HEADERS);
        m.insertRow(0, HEADERS);
        m.setColumnCount(3);
        if (results != null) {
            resultsCorrect = new boolean[results.length];
            boolean wrong = false;
            for (int i = 0; i < results.length; i++) {
                resultsCorrect[i] = results[i].isCorrect();
                if (!wrong && !resultsCorrect[i]) {
                    wrong = true;
                }
                final Object[] arr = {results[i].getCorrectAnswer(), results[i].getResult(), Arrays.toString(results[i].getParameters())};
                m.insertRow(i + 1, arr);
            }
            if (!wrong) {
                try {
                    final File complete = new File(Paths.SETTINGS + File.separator + "data.dat");
                    final byte[] data = IOUtils.readData(complete);
                    String info = new String(data);
                    if (!info.contains("|" + project.getName() + "|")) {
                        info = info.concat("|" + project.getName() + "|");
                    }
                    IOUtils.write(complete, info.getBytes());
                    project.setComplete(true);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        setModel(m);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final JLabel label = new JLabel(value == null ? "" : value.toString());
        if (row == 0) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            return label;
        }
        label.setForeground(resultsCorrect[row - 1] ? CORRECT : Color.RED);
        return label;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return this;
    }

}
