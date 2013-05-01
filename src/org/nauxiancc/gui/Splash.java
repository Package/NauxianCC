package org.nauxiancc.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.configuration.Message;

/**
 * The splash screen loaded before the main application. It displays information
 * regarding current status, tips to help you and gives you a nice heart-filled
 * welcome.
 *
 * @author Naux
 * @since 1.0
 */

public class Splash {

    private final JFrame frame;

    private static String status = "Loading";
    private String name;
    private final String message;

    private static final Color COLOR = new Color(250, 250, 250, 200);
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 12);
    private boolean should;

    /**
     * Constructs a new splash with default arguments.
     *
     * @since 1.0
     */

    public Splash() {
        frame = new JFrame();
        final JPanel splash = new JPanel() {

            @Override
            public void paintComponent(Graphics g1) {
                Graphics2D g = (Graphics2D) g1;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(Global.getImage(Global.SPLASH_IMAGE), 0, 0, this);
                g.setColor(COLOR);
                g.setFont(FONT);
                g.drawString(status, 10, 190);
                g.drawString("Welcome to Nauxian Computing Challenges " + name, 10, 15);
                g.drawString(message, 10, 90);
            }
        };

        message = Message.getRandom();
        name = System.getProperty("user.name");
        if (name == null || name.length() == 0) {
            name = "Mr. Anderson"; // matrix.
        }

        frame.setIconImage(Global.getImage(Global.ICON_IMAGE));
        frame.setUndecorated(true);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(splash);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!should) {
                    System.exit(0);
                }
            }
        });
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

    /**
     * Used to call JFrame-specific calls such as <tt>dipose()</tt> or <tt>repaint()</tt>
     *
     * @return The splash screen's JFrame instance.
     * @since 1.0
     */

    public JFrame getFrame(){
        return frame;
    }

}
