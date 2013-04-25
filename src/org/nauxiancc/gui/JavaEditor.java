package org.nauxiancc.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.nauxiancc.configuration.Global.Paths;
import org.nauxiancc.executor.Executor;
import org.nauxiancc.methods.IOUtils;
import org.nauxiancc.projects.Project;

/**
 * The main panel for editing the runner's code, displaying instructions and
 * displaying results.
 *
 * @author Naux
 * @since 1.0
 */

public class JavaEditor extends JPanel {

    private static final long serialVersionUID = 4203077483497169333L;
    private final JTextPane codePane;
    private final ResultsTable resultsTable;
    private final JTextArea instructions;
    private final Project project;
    private static final SimpleAttributeSet KEYWORD_SET = new SimpleAttributeSet();
    private static final SimpleAttributeSet NORMAL_SET = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(KEYWORD_SET, new Color(0x7036BE));
        StyleConstants.setForeground(NORMAL_SET, new Color(0x000000));
    }

    private static final String WORD_SPLIT = "[(\\[);\\n\\t\\u0020]";
    private static final String[] KEYWORDS = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
            "void", "volatile", "while"};

    /**
     * Constructs a new Java editor based off of the project. Will load code and
     * skeleton for the runner if necessary.
     *
     * @param project The project to base this runner off of.
     */

    public JavaEditor(final Project project) {
        super(new BorderLayout());

        this.project = project;

        instructions = new JTextArea();
        codePane = new JTextPane();
        resultsTable = new ResultsTable(project);
        final JButton run = new JButton("Run");
        final JButton clear = new JButton("Clear Project");
        final JPanel rightSide = new JPanel(new BorderLayout());
        final JPanel buttons = new JPanel();
        final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, resultsTable, rightSide);
        final JSplitPane textSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(instructions), split);

        run.setHorizontalTextPosition(SwingConstants.CENTER);
        run.setPreferredSize(new Dimension(200, run.getPreferredSize().height));
        run.setToolTipText("Runs the project. (Ctrl+R)");
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAndRun();
            }
        });

        codePane.setContentType("java");
        codePane.setFont(new Font("Consolas", Font.PLAIN, 12));
        codePane.requestFocus();
        codePane.setText(project.getCurrentCode());
        codePane.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                highlightKeywords();
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
                    saveAndRun();
                }
            }
        });
        codePane.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                setInstructionsText(project.getProperties().getDescription());
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });


        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSaveFiles();
            }
        });

        textSplit.setDividerLocation(100);

        split.setDividerLocation(300);

        buttons.add(clear);
        buttons.add(run);

        rightSide.add(new JScrollPane(codePane), BorderLayout.CENTER);
        rightSide.add(buttons, BorderLayout.SOUTH);

        instructions.setEditable(false);
        instructions.setFont(new Font("Consolas", Font.PLAIN, 12));
        instructions.setToolTipText("Instructions and any errors will appear here.");

        add(textSplit, BorderLayout.CENTER);
        setName(project.getName());

        highlightKeywords();
    }

    /**
     * Used to change the instruction panel's text. Most commonly, this is done
     * for error representation.
     *
     * @param string The <tt>String</tt> you would like to set the text to.
     * @see {@link JavaEditor#append(String)}
     * @since 1.0
     */

    public void setInstructionsText(String string) {
        instructions.setText(string);
    }

    /**
     * Appends a String to the instructions pane. This will be mostly used for
     * direct error sourcing.
     *
     * @param string The <tt>String</tt> you would like to append to the current
     *               text.
     * @since 1.0
     */

    public void append(String string) {
        instructions.append(string);
    }

    /**
     * This will highlight the Java keywords, from a list of reserved ones. It
     * will change the color of them, given by specific character attribute.
     *
     * @since 1.0
     */

    private void highlightKeywords() {
        int i = 0;
        for (final String word : codePane.getText().split(WORD_SPLIT)) {
            final boolean keyword = Arrays.binarySearch(KEYWORDS, word) >= 0;
            codePane.getStyledDocument().setCharacterAttributes(i, word.length(), keyword ? KEYWORD_SET : NORMAL_SET, true);
            i += word.length() + 1;
        }
    }


    public void clearSaveFiles() {
        final int n = JOptionPane.showConfirmDialog(null, "This will delete all progress on this project.\nDo you wish to continue?", "Continue?", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            if (project.getFile().exists()) {
                final File classF = new File(project.getFile().getAbsolutePath().replace(".java", ".class"));
                final File data = new File(Paths.SETTINGS + File.separator + "data.dat");
                project.getFile().delete();
                classF.delete();
                try {
                    final String words = new String(IOUtils.readData(data)).replace("|" + project + "|", "");
                    IOUtils.write(data, words.getBytes());
                    codePane.setText(project.getProperties().getSkeleton());
                    highlightKeywords();
                    return;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Error deleting current code!");
        }
    }

    public void saveAndRun() {
        if (codePane.getText().length() == 0 || project == null) {
            return;
        }
        if (!project.save(codePane.getText())) {
            JOptionPane.showMessageDialog(null, "Error saving current code!");
            return;
        }
        resultsTable.setResults(Executor.runAndGetResults(project));
    }

}
