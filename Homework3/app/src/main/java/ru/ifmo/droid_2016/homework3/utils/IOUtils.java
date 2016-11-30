package ru.ifmo.droid_2016.homework3.utils;

import java.io.Closeable;

/**
 * Created by Roman on 28/11/2016.
 */

public class IOUtils {
    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {} // ignore
    }

    /**
     * @return буфер размеров в 8кб для I/O. Потокобезопасный.ы
     */
    public static byte[] getIOBuffer() {
        byte[] buffer = bufferThreadLocal.get();
        if (buffer == null) {
            buffer = new byte[8192];
            bufferThreadLocal.set(buffer);
        }
        return buffer;
    }

    private static final ThreadLocal<byte[]> bufferThreadLocal = new ThreadLocal<>();
}
