package org.nauxiancc.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Date;

import org.nauxiancc.configuration.Global;
import org.nauxiancc.gui.GUI;
import org.nauxiancc.gui.JavaEditor;
import org.nauxiancc.methods.CustomClassLoader;
import org.nauxiancc.projects.Project;

/**
 * Source code compiler and executor. This is used internally during the run of
 * the input code.
 *
 * @author Naux
 * @since 1.0
 */

public class Executor {

    public static boolean hasJDKInstalled() {
        try {
            final Process r = Runtime.getRuntime().exec("javac -version");
            final BufferedReader read = new BufferedReader(new InputStreamReader(r.getErrorStream()));
            return !read.readLine().startsWith("'javac' is not recognized");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Runs the compiled code from the {@link Executor#compileClass(Project)
     * compileClass(Project)} method. This returns a list of <tt>Result</tt>s
     * dependent on the users input and the provided answer set.
     *
     * @param project The specific project to compile and run to provide results
     *                for.
     * @return An array of type {@link Result Result}.
     * @see Executor#compileClass(Project)
     * @since 1.0
     */

    public static Result[] runAndGetResults(final Project project) {
        if (project != null && compileClass(project)) {
            try {
                final Class<?> ref = project.getRunner();
                final Method method = ref.getMethod("getResults", Class.class);
                return (Result[]) method.invoke(ref.newInstance(), CustomClassLoader.loadClassFromFile(Global.Paths.JAVA + File.separator + project.getName() + ".class"));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This will use the located JDK directory during initialization and compile
     * the code provided by the user. This file is then handed off to the
     * {@link Executor#runAndGetResults(Project) runAndGetResults(Project)}
     * method where it is processed. This method will only return true should
     * the code be successfully compiled.
     *
     * @param project The project source you wish to compile, as provided by the
     *                user.
     * @return <tt>true</tt> should the class successfully compile and write to
     *         a file.
     * @see Executor#runAndGetResults(Project)
     * @since 1.0
     */

    private static boolean compileClass(Project project) {
        try {
            final String format = "[" + new Date(System.currentTimeMillis()) + "]";
            final JavaEditor editor = GUI.tabByName(project.getName());
            final Process p = Runtime.getRuntime().exec("javac -g  -d " + project.getFile().getParent() + " " + project.getFile().getPath(), null, null);
            p.waitFor();
            final BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            final StringBuilder replacement = new StringBuilder();
            while ((line = error.readLine()) != null) {
                final String build = line.substring(Math.max(line.indexOf("java:"), 0)).replace("java:", "line ");
                System.err.println(format + build);
                replacement.append(build);
                replacement.append('\n');
            }
            if(replacement.length() != 0){
                editor.setInstructionsText(replacement.toString());
                return false;
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
