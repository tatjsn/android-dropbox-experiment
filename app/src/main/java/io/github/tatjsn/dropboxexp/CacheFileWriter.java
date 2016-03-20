package io.github.tatjsn.dropboxexp;

import android.text.format.DateFormat;

import java.io.File;
import java.io.FileOutputStream;

public class CacheFileWriter {
    static String writeCacheFile(File path) throws Exception {
        String fileName = DateFormat.format(
                "yyyy-MM-dd-HH-mm-ss.txt", System.currentTimeMillis()).toString();
        File file = new File(path, fileName);
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write("Test 123".getBytes("UTF-8"));
            stream.close();
        } catch (java.lang.Exception e) {
            throw new Exception(e);
        }
        return fileName;
    }

    static class Exception extends java.lang.Exception {
        public Exception(Throwable throwable) {
            super("Error writing cache", throwable);
        }
    }
}
