package com.whatsmode.library.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FileUtils {
    private static File logFile;
    public static File writeLog(String filePath, String fileName, String content) {
        if (content == null)
            content = "";
        logFile = new File(filePath + File.separator + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(logFile);
            fos.write(content.getBytes());

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return logFile;
    }
}
