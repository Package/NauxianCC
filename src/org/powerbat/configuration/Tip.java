package org.powerbat.configuration;

import java.util.Random;

/**
 * Used for the splash screen to display useful information about the client.
 *
 * @author Naux
 * @since 1.0
 */

public class Tip {

    private final static String[] TIPS = new String[]{
    };

    /**
     * Returns a method for which you can return a tip from the list of tips.
     * This should only really be used in the initial boot of the application.
     *
     * @return A random tip to be used in the splash screen.
     * @since 1.0
     */

    public static String getRandom() {
        return null;
        //final int tipNumber = new Random().nextInt(TIPS.length);
        //return "Tip #" + (tipNumber + 1) + ": " + TIPS[tipNumber];
    }

}
