package org.powerbat.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.powerbat.configuration.Global;
import org.powerbat.methods.Updater;
import org.powerbat.projects.Project;

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

    private static final Border PRESET = new BevelBorder(BevelBorder.RAISED);
    private static final Border PRESS = new BevelBorder(BevelBorder.LOWERED);

    private static final Dimension TAB_SIZE = new Dimension(170, 30);

    /**
     * Creates a new GUI instance. Should only be done once per
     * <tt>Runtime</tt>. This will set all listeners and handle the
     * initialization of the static arguments to be used later.
     *
     * @since 1.0
     */
    public GUI() {
        final JFrame frame = new JFrame("Powerbat v" + Updater.clientVersion());
        final JPanel main = new JPanel(new BorderLayout());
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel mainpane = new JPanel();
        final ProjectSelector selector = new ProjectSelector();
        final JPanel homeFill = new JPanel();
        final JLabel home = new JLabel("Home", JLabel.CENTER);

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
        final JavaEditor temp = new JavaEditor(project);
        temp.setInstructionsText(project.getInstructions());
        if (tabByName(project.getName()) == null) {
            final Image image = Global.getImage(Global.CLOSE_IMAGE);
            final ImageIcon icon = new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            final JLabel close = new JLabel(icon, SwingConstants.CENTER);
            final JLabel label = new JLabel(project.getName());
            final JPanel pane = new JPanel(new BorderLayout());
            final JPanel main = new JPanel(new BorderLayout());

            pane.setName(project.getName());
            pane.setPreferredSize(new Dimension(140, 50));
            pane.setOpaque(false);
            pane.add(label, BorderLayout.CENTER);

            tabs.add(temp, tabs.getTabCount());
            tabs.setTabComponentAt(tabs.getTabCount() - 1, main);

            main.add(close, BorderLayout.EAST);
            main.add(pane, BorderLayout.CENTER);
            main.setOpaque(false);
            main.setPreferredSize(TAB_SIZE);

            label.setLocation(pane.getWidth() - label.getWidth(), pane.getY());

            close.setBorder(PRESET);
            close.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    close.setBorder(PRESS);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    close.setBorder(PRESET);
                    removeTab(project.getName());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

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
     * @param name Always represented through {@link org.powerbat.interfaces.Runner#getClassName()}
     * @see GUI#tabByName(String)
     * @since 1.0
     */

    public synchronized static void removeTab(String name) {
        JavaEditor cur = tabByName(name);
        if (cur != null) {
            tabs.remove(cur);
            cur.removeAll();
            //cur = null;
            System.gc();
            return;
        }
        System.err.println("Failed to close tab " + name);
    }

}