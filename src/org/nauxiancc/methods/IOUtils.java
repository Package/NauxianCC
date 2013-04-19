package org.nauxiancc.methods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * File downloader utility
 *
 * @since 1.0
 * @author Naux
 */

public class IOUtils {

    private IOUtils() {
    }

    /**
     *  Will download file data ready to be exported to a file
     *
     * @since 1.0
     * @param url The valid URL you wish to read data from
     * @return byte array of data. Building a file from this is safe
     * @throws IOException
     */
    public static byte[] download(URL url) throws IOException {
        return readData(url.openStream());
    }

    /**
     *  This will read a local file and return data which can be manipulated easily
     *
     * @since 1.0
     * @param file Will load data from a local file
     * @return byte array of data. Building a file or modification of a file from this is safe
     * @throws IOException
     */

    public static byte[] readData(File file) throws IOException {
        return readData(new FileInputStream(file));
    }

    /**
     * Reads data from an input stream, either a File or URL
     *
     * @see {org.powerbat.methods.IOUtils.download(URL);}
     * @see {org.powerbat.methods.IOUtils.readData(File);}
     *
     * @since 1.0
     * @param in input stream to read data from. Allocates 1024 bytes per cycle
     * @return a mutable byte array for file storing or manipulation
     * @throws IOException
     */

    private static byte[] readData(InputStream in) throws IOException {
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
     * @see {IOUtils.download(URL);}
     *
     * @since 1.0
     * @param file The file to be written to, does not necessarily have to exist.
     * @param data The data to be written. It is best used with url-based files
     * @throws IOException
     */

    public static void write(File file, byte[] data) throws IOException {
        final FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
    }

}
