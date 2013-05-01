package org.nauxiancc.gui;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.projects.Project;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for the construction of the
 * {@link ProjectSelector}. In regards to other usage, there is none.
 * This will get information from project and use that to render an appropriate
 * UI.
 *
 * @author Naux
 * @see ProjectSelector
 * @since 1.0
 */

public class ProjectPanel extends JPanel implements Comparable<ProjectPanel> {

    private static final long serialVersionUID = -3692838815172773196L;

    private final Project project;

    private static final Font SANS_18 = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    private static final Font SANS_15 = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private final JLabel complete;

    /**
     * Constructs a new panel dependent on the <tt>project</tt>.
     *
     * @param project The project to get required information such as name and
     *                difficulty.
     * @since 1.0
     */

    public ProjectPanel(final Project project) {
        super(new BorderLayout());

        final JPanel rightPane = new JPanel(new BorderLayout());
        final JPanel leftPane = new JPanel();
        final JPanel centerPane = new JPanel(new GridLayout(3, 1));

        final JLabel difficulty = new JLabel(Project.DIFFICULTY[project.getProperties().getCategory() - 1]);
        final JLabel name = new JLabel();
        final JLabel author = new JLabel(project.getProperties().getAuthor());

        final JButton open = new JButton("Open");

        final StringBuilder builder = new StringBuilder();
        final Pattern pattern = Pattern.compile("[A-Z]?[a-z]+|[0-9]+");
        final Matcher matcher = pattern.matcher(project.getName());
        while (matcher.find()) {
            builder.append(matcher.group());
            builder.append(' ');
        }
        builder.trimToSize();
        name.setText(builder.toString());
        if (name.getText().length() == 0) {
            name.setText(project.getName());
        }

        complete = new JLabel(project.isComplete() ? new ImageIcon(Global.getImage(Global.COMPLETE_IMAGE)) : null);

        this.project = project;

        leftPane.setPreferredSize(new Dimension(25, 150));

        rightPane.add(open, BorderLayout.SOUTH);
        rightPane.add(complete, BorderLayout.CENTER);
        rightPane.setPreferredSize(new Dimension(75, 150));

        setPreferredSize(new Dimension(320, 150));
        setBorder(new BevelBorder(BevelBorder.RAISED));

        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.openProject(project);
            }

        });

        complete.setPreferredSize(new Dimension(75, 30));

        difficulty.setPreferredSize(new Dimension(200, 30));
        difficulty.setFont(SANS_15);
        difficulty.setForeground(Project.DIFFICULTY_COLOR[project.getProperties().getCategory() - 1]);

        name.setPreferredSize(new Dimension(200, 50));
        name.setFont(SANS_18);
        name.setForeground(Project.DIFFICULTY_COLOR[project.getProperties().getCategory() - 1]);

        author.setPreferredSize(new Dimension(200, 30));
        author.setFont(SANS_15);
        author.setForeground(Project.DIFFICULTY_COLOR[project.getProperties().getCategory() - 1]);

        centerPane.add(name);
        centerPane.add(author);
        centerPane.add(difficulty);

        add(leftPane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
        add(rightPane, BorderLayout.EAST);

    }

    /**
     * Used to change the Icon for the completion marker. Only true usage is in
     * the instance construction and during execution.
     *
     * @param isComplete sets the project icon complete
     * @since 1.0
     */

    public void setComplete(boolean isComplete) {
        complete.setIcon(isComplete ? new ImageIcon(Global.getImage(Global.COMPLETE_IMAGE)) : null);
    }

    /**
     * This will return the project this panel is based off of.
     *
     * @return Project instance used to make this panel.
     * @since 1.0
     */

    public Project getProject() {
        return project;
    }

    @Override
    public int compareTo(ProjectPanel o) {
        return getProject().getSortName().compareTo(o.getProject().getSortName());
    }

}
