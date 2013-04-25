package org.nauxiancc.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;

import org.nauxiancc.projects.Project;
import org.nauxiancc.projects.ProjectData;

/**
 * The selector is used to display the list of available projects and allow the
 * user to open them. This will display all correctly published Runners.
 *
 * @author Naux
 * @since 1.0
 */

public class ProjectSelector extends JPanel {

    private static final long serialVersionUID = 4869219241938861949L;
    private static final ArrayList<ProjectPanel> PROJECTS = new ArrayList<>();
    private static final String TOOL_TIP = " information. Press 'Open' to start it.";
    private final JPanel selector;

    /**
     * Constructs a new project selector. Only to be used inside of the
     * {@link GUI#openProject(Project)}.
     *
     * @since 1.0
     */

    public ProjectSelector() {
        super(new BorderLayout());
        selector = new JPanel(new SelectorLayout(FlowLayout.LEADING, 5, 5));
        for (final Project project : ProjectData.DATA) {
            final ProjectPanel temp = new ProjectPanel(project);
            PROJECTS.add(temp);
            temp.setToolTipText(project.getName().concat(TOOL_TIP));
        }
        Collections.sort(PROJECTS, new Comparator<ProjectPanel>() {

            @Override
            public int compare(ProjectPanel o1, ProjectPanel o2) {
                return o1.compareTo(o2);
            }

        });
        for (final ProjectPanel panel : PROJECTS) {
            selector.add(panel);
        }
        add(new JScrollPane(selector), BorderLayout.CENTER);
    }

    /**
     *
     * Filters out <tt>ProjectPanels</tt> by a search key or based
     * conditions. This method clears the panel, then re-adds components
     * that meet the required conditions if any.
     *
     * @param key           The key to search for in name
     * @param complete      Whether or not to include complete projects
     * @param name          Whether or not to search by name. This uses
     *                      the <tt>key</tt> for reference
     * @param incomplete    Whether or not to include incomplete projects
     */

    public void refine(final String key, boolean complete, boolean name, boolean incomplete) {
        selector.removeAll();
        revalidate();
        for (final ProjectPanel p : PROJECTS) {
            if (name && p.getProject().getName().toLowerCase().contains(key.toLowerCase()) || key.isEmpty()) {
                if (complete && p.getProject().isComplete() || incomplete && !p.getProject().isComplete()) {
                    selector.add(p);
                }
            }
        }
        revalidate();
        updateUI();
    }


    /**
     * Gets the list of all added projects to the selector pane.
     *
     * @return The list of available projects.
     * @since 1.0
     */

    public static ArrayList<ProjectPanel> getProjectList() {
        return PROJECTS;
    }
}
