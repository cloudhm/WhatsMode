package com.whatsmode.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by tom on 17-11-16.
 */

public class SerializableUtil {
    public static <T extends Serializable> T readObject(File filePath) {
        ObjectInputStream ois = null;
        T t = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filePath));
            t = (T) ois.readObject();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }finally {
            try {
                if(ois != null)
                    ois.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        return t;
    }

    public static <T extends Serializable> boolean writeObject(T object,String filePath) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(object);
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    public static File getSerializableFile(String rootPath, String fileName) {
        File file = new File(rootPath);
        if(!file.exists()) file.mkdirs();
        File serializableFile = new File(file,fileName);
        try {
            if(!serializableFile.exists())
                serializableFile.createNewFile();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return serializableFile;
    }
}
