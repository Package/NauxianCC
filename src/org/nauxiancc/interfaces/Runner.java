package org.nauxiancc.interfaces;

import org.nauxiancc.executor.Result;

/**
 * All Runners must extend this class. It will handle all manifest properties
 * and project-specific builds.
 *
 * @author Naux
 * @since 1.0
 */

public abstract class Runner {

    /**
     * Runs the user's code and is used to set up specific tests. This is the
     * only required method in a Runner.
     *
     * @since 1.0
     */
    public abstract Result[] getResults(Class<?> clazz);
}
