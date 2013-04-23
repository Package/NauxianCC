package org.nauxiancc.gui;

import java.awt.*;

import javax.swing.*;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.methods.Updater;
import org.nauxiancc.projects.Project;

/**
 * Only for use in the boot and the setting and getting of Projects.
 *
 * @author Naux
 * @see GUI#removeTab(String)
 * @see GUI#tabByName(String)
 * @since 1.0
 */
public class GUI {

    private static JTabbedPane tabs;
    private static ProjectSelector selector;

    private static final Dimension TAB_SIZE = new Dimension(170, 30);

    /**
     * Creates a new GUI instance. Should only be done once per
     * <tt>Runtime</tt>. This will set all listeners and handle the
     * initialization of the static arguments to be used later.
     *
     * @since 1.0
     */
    public GUI() {
        final JFrame frame = new JFrame("Nauxian Computing Challenges v" + Updater.clientVersion());
        final JPanel main = new JPanel(new BorderLayout());
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel mainpane = new JPanel();
        final JPanel homeFill = new JPanel();
        final JLabel home = new JLabel("Home", JLabel.CENTER);
        final SearchPanel search  = new SearchPanel();

        selector = new ProjectSelector();
        tabs = new JTabbedPane();

        frame.setContentPane(main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setLocationRelativeTo(frame.getParent());

        try {
            frame.setIconImage(Global.getImage(Global.ICON_IMAGE));
        } catch (Exception e) {
            Splash.setStatus("Downloading icon failed");
        }

        content.add(search, BorderLayout.NORTH);
        content.add(selector, BorderLayout.CENTER);

        mainpane.setOpaque(false);
        homeFill.setLayout(new BorderLayout());
        homeFill.setPreferredSize(TAB_SIZE);
        homeFill.add(home, SwingConstants.CENTER);
        homeFill.setOpaque(false);
        home.setOpaque(false);

        mainpane.add(homeFill);
        tabs.setTabPlacement(SwingConstants.LEFT);
        tabs.add(content, tabs.getTabCount());
        tabs.setTabComponentAt(tabs.getTabCount() - 1, mainpane);

        main.add(tabs);
        frame.pack();
        frame.setVisible(true);
        Splash.setStatus("Complete");
    }

    /**
     * Adds a project selected from the ProjectSelector.
     *
     * @param project The selected Project you wish to add.
     * @since 1.0
     */

    protected synchronized static void openProject(final Project project) {
        if (project == null) {
            return;
        }
        final JavaEditor editor = new JavaEditor(project);
        editor.setInstructionsText(project.getProperties().getDescription());
        if (tabByName(project.getName()) == null) {

            tabs.add(editor, tabs.getTabCount());
            tabs.setTabComponentAt(tabs.getTabCount() - 1, new TabPane(project));

        }
        tabs.setSelectedComponent(tabByName(project.getName()));
    }

    /**
     * This will return the <tt>JavaEditor</tt> that corresponds to the
     * given name.
     *
     * @param name The name of the tab, always portrayed through the Runner's name.
     * @return The tab instance of the Java editor.
     * @since 1.0
     */

    public synchronized static JavaEditor tabByName(String name) {
        for (final Component c : tabs.getComponents()) {
            if (c != null && c instanceof JavaEditor) {
                final JavaEditor c1 = (JavaEditor) c;
                if (name.equals(c1.getName())) {
                    return c1;
                }
            }
        }
        return null;
    }

    /**
     * Removes the tab from the tabbed pane. Loads the tab by name.
     *
     * @see GUI#tabByName(String)
     * @since 1.0
     */

    public synchronized static void removeTab(String name) {
        JavaEditor cur = tabByName(name);
        if (cur != null) {
            tabs.remove(cur);
            cur.removeAll();
            System.gc();
            return;
        }
        System.err.println("Failed to close tab " + name);
    }

    public static ProjectSelector getSelector(){
        return selector;
    }

}