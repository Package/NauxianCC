package org.nauxiancc.methods;

import java.io.*;
import java.net.URL;

/**
 * File downloader utility
 *
 * @author Naux, Xhin
 * @since 1.0
 */

public class IOUtils {

    private IOUtils() {
    }

    /**
     * Will download file data ready to be exported to a file
     *
     * @param url The valid URL you wish to read data from
     * @return byte array of data. Building a file from this is safe
     * @throws IOException
     * @since 1.0
     */
    public static byte[] download(final URL url) throws IOException {
        return readData(url.openStream());
    }

    /**
     * This will read a local file and return data which can be manipulated easily
     *
     * @param file Will load data from a local file
     * @return byte array of data. Building a file or modification of a file from this is safe
     * @throws IOException
     * @since 1.0
     */

    public static byte[] readData(final File file) throws IOException {
        return readData(new FileInputStream(file));
    }

    /**
     * Reads data from an input stream, either a File or URL
     *
     * @param in input stream to read data from. Allocates 1024 bytes per cycle
     * @return a mutable byte array for file storing or manipulation
     * @throws IOException
     * @see {org.powerbat.methods.IOUtils.download(URL);}
     * @see {org.powerbat.methods.IOUtils.readData(File);}
     * @since 1.0
     */

    private static byte[] readData(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] data = new byte[1024];
        int read;
        while ((read = in.read(data, 0, 1024)) != -1) {
            out.write(data, 0, read);
        }
        in.close();
        return out.toByteArray();
    }

    /**
     * Writes a file and byte data to a specific destination
     *
     * @param file The file to be written to, does not necessarily have to exist.
     * @param data The data to be written. It is best used with url-based files
     * @throws IOException
     * @see {IOUtils.download(URL);}
     * @since 1.0
     */

    public static void write(final File file, final byte[] data) throws IOException {
        final FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
    }

}
