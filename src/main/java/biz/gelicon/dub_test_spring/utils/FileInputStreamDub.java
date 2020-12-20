package biz.gelicon.dub_test_spring.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FileInputStreamDub {

    public static String readProperty(String fileName, String propertyName) {
        if (fileName == null || propertyName == null) {return null;}
        Properties properties;
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            properties = new Properties();
            stream = new FileInputStream(new File("fileName"));
            reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            properties.load(reader);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
