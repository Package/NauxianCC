package org.powerbat.projects;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.powerbat.configuration.Global.Paths;
import org.powerbat.gui.ProjectPanel;
import org.powerbat.gui.ProjectSelector;
import org.powerbat.interfaces.Manifest;
import org.powerbat.methods.CustomClassLoader;
import org.powerbat.methods.IOUtils;

/**
 * Used for handling project attributes and other data
 *
 * @author Naux
 * @version 1.0
 */

public class Project {

    public static final String[] DIFFICULTY = new String[]{"Beginner", "Intermediate", "Advanced", "Challenging", "Legendary"};
    public static final Color[] DIFFICULTY_COLOR = new Color[]{
            new Color(51, 51, 51),
            new Color(91, 91, 221),
            new Color(162, 154, 82),
            new Color(132, 205, 72),
            new Color(107, 50, 30)
    };

    private final String name;
    private final File file;
    private final String instructions;
    private final String category;
    private final String className;
    private final String method;
    private final Class<?> runner;
    private final double version;
    private final int level;
    private boolean complete;

    /**
     * Constructs a new Project and reads file data
     *
     * @param name       Name of the Project
     * @param runnerFile The file to read data from
     */

    public Project(final String name, final File runnerFile) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(new File(Paths.SETTINGS + File.separator + "data.dat")));
            boolean b;
            try {
                final String in = br.readLine();
                b = (in.contains("|" + name + "|"));
            } catch (Exception e) {
                b = false;
            }
            complete = b;
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error getting instructions for " + name);
        }
        file = new File(Paths.JAVA + File.separator + name + ".java");
        runner = CustomClassLoader.loadClassFromFile(runnerFile);
        final Manifest manifest = runner.getAnnotation(Manifest.class);
        instructions = manifest.instructions();
        category = manifest.category();
        version = manifest.version();
        className = manifest.className();
        method = manifest.method();
        level = Math.min(5, Math.max(1, manifest.level()));
        this.name = name;
    }

    /**
     * This is used for loading dynamic files, including compiled files and soon to be XML/CSS files
     *
     * @return The name of the Runner
     */

    public String getName() {
        return name;
    }

    /**
     * Used to sort and distribute difficulties
     *
     * @return The category that this Runner falls into
     */

    public String getSortName() {
        return level + getName();
    }


    /**
     * @see org.powerbat.projects.Project#getName()
     */

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns the difficulty level as an <tt>int</tt> from 1-5.
     *
     * @return difficulty level of the project
     */

    public int getLevel() {
        return level;
    }

    /**
     * This is used only during loading to get the saved code if any
     *
     * @return formatted String containing user's code
     */

    public String getCurrentCode() {
        try {
            if (file.exists()) {
                final byte[] data = IOUtils.readData(file);
                return new String(IOUtils.readData(file), 0, data.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSkeleton();
    }

    /**
     * This method is used primarily in the GUI. It is used only once or twice during runtime per runner.
     *
     * @return the instructions to be displayed regarding the problem.
     */

    public String getInstructions() {
        return instructions;
    }

    /**
     * Returns the file of the runner. This is used nicely to read data
     *
     * @return The file type to read and store data
     */

    public File getFile() {
        return file;
    }

    /**
     * Useful for checking version information and updating.
     *
     * @return version of the Runner
     */

    public double getVersion() {
        return version;
    }

    /**
     * Used in displaying the Runner and sorting by difficulty
     *
     * @return The category as a <tt>String</tt>
     */

    public String getCategory() {
        return category;
    }

    /**
     * Returns the runner class for reflection to run the user-provided code
     *
     * @return the Class instance of the loaded Runner
     */

    public Class<?> getRunner() {
        return runner;
    }

    /**
     * Hashing for adding to a <tt>HashMap</tt> without overriding version sets
     *
     * @return hash code based off the name, file and instructions.
     */

    public int hashCode() {
        return name.hashCode() * 31 + file.hashCode() * 17 - instructions.hashCode() * 3;
    }

    /**
     * Used to filter complete projects and displaying detailed information about the runner
     *
     * @return whether or not the project is complete
     */

    public boolean isComplete() {
        return complete;
    }

    /**
     * Used only internally during execution of code.
     *
     * @param complete <tt>boolean</tt> representing if the project was 100% successful and complete.
     */

    public void setComplete(boolean complete) {
        this.complete = complete;
        for (final ProjectPanel panel : ProjectSelector.getProjectList()) {
            if (panel.getProject().equals(this)) {
                panel.setComplete(complete);
                return;
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Project && o.hashCode() == this.hashCode();
    }

    /**
     * Saves the code so the user can resume the project at a later time
     * <p/>
     * Compilation is not required, so non-working code can be accepted.
     *
     * @param code The <tt>String</tt> of code to save
     * @return <tt>true</tt> if it saved correctly.
     */

    public boolean save(final String code) {
        try {
            IOUtils.write(getFile(), code.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Used when code can't be loaded or the project is restarted
     *
     * @return <tt>String</tt> of the basic skeleton as read from the manifest
     */

    public String getSkeleton() {
        return "public class " + className + " {\n\t\n\tpublic " + method + "{\n\t\n\t}\n\n}";
    }

}