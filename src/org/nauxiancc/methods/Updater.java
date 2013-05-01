package org.nauxiancc.methods;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.configuration.Global.Paths;
import org.nauxiancc.configuration.Global.URLs;
import org.nauxiancc.gui.Splash;
import org.nauxiancc.projects.Project;
import org.nauxiancc.projects.ProjectData;

import javax.swing.*;
import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Updates all Runner information and program data. This is used in conjunction
 * with the boot class and only should be used in there. This class handles most
 * client data and also calls upon the other loaders.
 *
 * @author Naux
 * @since 1.0
 */
public class Updater {

    private static double updatedClientVersion = 0.0;
    private static HashMap<String, Double> updatedRunnersList = new HashMap<>();

    private static double currentClientVersion = 1.00;
    private static HashMap<String, Double> currentRunnersList = new HashMap<>();

    private Updater() {
    }

    /**
     * Returns the current client version. This is hard-coded every update.
     *
     * @return Client version.
     * @since 1.0
     */

    public static double clientVersion() {
        return currentClientVersion;
    }

    /**
     * Updates the client and all public Runners. No alpha implementation yet.
     * Should only ever be ran once.
     *
     * @since 1.0
     */

    public static void update() {
        ProjectData.loadCurrent();
        if (!isInternetReachable()) {
            JOptionPane.showMessageDialog(null, "Unable to connect to internet; unable to check versions.", "Update", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final ArrayList<String> sources = new ArrayList<>();
        sources.add(URLs.BIN);
        try {
            final File sourceFile = new File(Paths.SETTINGS, "sources.txt");
            if ( sourceFile.exists() || sourceFile.createNewFile()) {
                final BufferedReader br = new BufferedReader(new FileReader(sourceFile));
                String next;
                while ((next = br.readLine()) != null) {
                    if (!next.startsWith("#")) {
                        sources.add(next);
                    }
                }
                br.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        for (final String src : sources) {
            byte[] updatedClientInfo = downloadCurrentClientInfo(src);
            if (updatedClientInfo == null) {
                return;
            }
            parseUpdated(updatedClientInfo, src);
            if (updatedClientVersion > currentClientVersion) {
                JOptionPane.showMessageDialog(null, "Update available - Please download from powerbot.org again.", "Update", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            final String runner = "Runner";
            for (final Project p : ProjectData.DATA) {
                currentRunnersList.put(p.getName() + runner, p.getProperties().getVersion());
            }
            for (final String key : updatedRunnersList.keySet()) {
                if (!currentRunnersList.containsKey(key) || updatedRunnersList.get(key) > currentRunnersList.get(key)) {
                    download(key, src);
                }
            }
        }
        currentRunnersList = null;
        updatedRunnersList = null;
        ProjectData.loadCurrent();
    }

    /**
     * Downloads a runner name from a URL. The URL is constructed from the
     * <tt>src</tt> followed by the <tt>runnerName</tt>.
     *
     * @param runnerName The name of the Runner, in pure form such as
     *                   <tt>SimpleAdditionRunner</tt>
     * @param src        The depot of the Runner.
     * @since 1.0
     */

    private static void download(String runnerName, String src) {
        try {
            Splash.setStatus("Downloading " + runnerName + ".class");

            final byte[] data = IOUtils.download(new URL(src + runnerName + ".class" + (src.contains("github.com") ? "?raw=true" : "")));
            final File out = new File(Global.Paths.SOURCE, runnerName + ".class");
            IOUtils.write(out, data);

            Splash.setStatus("Downloading " + runnerName + ".xml");
            final byte[] xdata = IOUtils.download(new URL(src + runnerName + ".xml" + (src.contains("github.com") ? "?raw=true" : "")));
            final File xout = new File(Global.Paths.SOURCE, runnerName + ".xml");
            IOUtils.write(xout, xdata);

        } catch (final IOException e) {
            System.err.println("Unable to download " + runnerName);
            e.printStackTrace();
        }

    }

    /**
     * Reads the update information and runner information from a source. Source
     * specific arguments do matter.
     *
     * @param data The byte data downloaded from the source.txt file.
     * @param src  The URL directory for special loads.
     * @since 1.0
     */

    private static void parseUpdated(byte[] data, String src) {
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
            if (src.equals(URLs.BIN)) {
                updatedClientVersion = Double.parseDouble(in.readLine());
            }
            String s;
            while ((s = in.readLine()) != null) {
                String[] split = s.split("-");
                updatedRunnersList.put(split[0], Double.parseDouble(split[1]));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads information from a source. This will automatically be called for
     * the default directory, as implemented by myself.
     *
     * @return The byte data to be read.
     * @see {@link Updater#parseUpdated(byte[], String)}
     * @since 1.0
     */

    private static byte[] downloadCurrentClientInfo(final String src) {
        try {
            return IOUtils.download(new URL(URLs.BIN + "version.txt" + (src.contains("github.com") ? "?raw=true" : "")));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ping common sites to check Internet availability.
     *
     * @return a boolean to check if Internet is available. Used before
     *         attempting to update.
     * @since {@link Updater#update()}
     */

    public static boolean isInternetReachable() {
        Splash.setStatus("Checking connection");
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                if (interfaces.nextElement().isUp()) {
                    return true;
                }
            }
        } catch (final SocketException e) {
            e.printStackTrace();
        }
        return false;
    }
}
