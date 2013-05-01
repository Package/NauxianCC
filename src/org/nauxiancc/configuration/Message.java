package org.nauxiancc.configuration;

import java.util.Random;

/**
 * Used for the splash screen to display useful information about the client.
 *
 * @author Naux
 * @since 1.0
 */

public class Message {

    private final static String[] MESSAGES = new String[]{

            "Most of Nauxian CC's code is from before Java 7.",
            "There is no cow level",
            "Only through suffering, does one attain wisdom",
            "Now with 0 calories!"

    };

    /**
     * Returns a method for which you can return a tip from the list of tips.
     * This should only really be used in the initial boot of the application.
     *
     * @return A random tip to be used in the splash screen.
     * @since 1.0
     */

    public static String getRandom() {
        final int tipNumber = new Random().nextInt(MESSAGES.length);
        return "Message #" + (tipNumber + 1) + ": " + MESSAGES[tipNumber];
    }

}
