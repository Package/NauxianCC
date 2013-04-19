package org.nauxiancc.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.configuration.Tip;

/**
 * The splash screen loaded before the main application. It displays information
 * regarding current status, tips to help you and gives you a nice heart-filled
 * welcome.
 *
 * @author Naux
 * @since 1.0
 */

public class Splash extends JFrame implements WindowListener, MouseListener {

    private static final long serialVersionUID = -5296459157992617129L;

    private static String status = "Loading";
    private String name;

    private static final Color COLOR = new Color(250, 250, 250, 200);
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 12);
    private String tip;
    private boolean should;

    /**
     * Constructs a new splash with default arguments.
     *
     * @since 1.0
     */

    public Splash() {
        tip = Tip.getRandom();
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final Panel splash = new Panel();

        name = System.getProperty("user.name");
        if (name == null || name.length() == 0) {
            name = "Mr. Anderson"; // matrix.
        }

        setUndecorated(true);
        setLocation((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 100);
        setSize(600, 200);
        setContentPane(splash);

        repaint();

        splash.addMouseListener(this);
        addWindowListener(this);
    }

    /**
     * Used to check if the boot went well and was not terminated prematurely.
     *
     * @param should Whether or not it should dispose and continue basic operation.
     * @since 1.0
     */

    public void shouldDispose(boolean should) {
        this.should = should;
    }

    /**
     * Sets the current status of the splash screen. This is only used during
     * boot, as is the splash itself.
     *
     * @param status The message you would like to relay to the user.
     * @since 1.0
     */

    public static void setStatus(String status) {
        Splash.status = status;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (!should) {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (!should) {
            System.exit(0);
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        final String next = Tip.getRandom();
        if (next != null && !next.equals(tip)) {
            tip = next;
            repaint();
        } else {
            mousePressed(arg0);
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

    /**
     * Panel to give access to the painting components. It is the content pane
     * for the splash screen.
     *
     * @author Legend
     * @since 1.0
     */

    public class Panel extends JPanel {

        private static final long serialVersionUID = 3579128244881997515L;

        @Override
        public void paintComponent(Graphics g1) {
            Graphics2D g = (Graphics2D) g1;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(Global.getImage(Global.SPLASH_IMAGE), 0, 0, this);
            g.setColor(COLOR);
            g.setFont(FONT);
            g.drawString(status, 10, 190);
            g.drawString("Welcome to Powerbat " + name, 10, 15);
            g.drawString(tip, 10, 90);
        }
    }

}
