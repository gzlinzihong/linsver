package cn.ilanky.linsver.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 嘿 林梓鸿
 * @date 2020年 12月31日 11:53:27
 */
public class MimeProperties {

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(MimeProperties.class.getClassLoader().getResource("mime.properties").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }
}
