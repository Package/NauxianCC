package org.nauxiancc.methods;

import java.io.File;

/**
 * Loads classes from predefined files. Doesn't do any file checks so ensure it
 * exists before loading it's class.
 *
 * @author Naux
 * @see {@link ClassLoader}
 * @since 1.0
 */

public class CustomClassLoader extends ClassLoader {

    /**
     * Only used for access to non-static methods.
     *
     * @since 1.0
     */

    private CustomClassLoader() {
    }

    /**
     * Loads a class from a predefined file. Creates a new file instance, so if
     * you already have a file, use the other one.
     *
     * @param file <tt>String instance for a file to load. File must exist.</tt>
     * @return A class instance from a defined file.
     * @see {@link CustomClassLoader#loadClassFromFile(File)}
     */
    public static Class<?> loadClassFromFile(String file) {
        return loadClassFromFile(new File(file));
    }

    /**
     * Loads a class from a file instance. Reads the bytes and creates a class
     * instance.
     *
     * @param file <tt>File</tt> instance to load the class from. This file must
     *             exist.
     * @return A class instance from a defined file.
     */

    public static Class<?> loadClassFromFile(File file) {
        return new CustomClassLoader().loadClassFromFile_(file);
    }

    /**
     * Loads a class and defines it using methods inherited from
     * {@link ClassLoader}.
     *
     * @param file File to load a class from.
     * @return A class instance from a defined file.
     */

    private Class<?> loadClassFromFile_(File file) {
        try {
            final byte[] data = IOUtils.readData(file);
            return super.defineClass(file.getName().substring(0, file.getName().length() - 6), data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
