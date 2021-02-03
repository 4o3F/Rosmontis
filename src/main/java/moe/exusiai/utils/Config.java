package moe.exusiai.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static Long accountid;
    public static String accountpassword;
    public static String lastdynamicid;
    public static Long group;
    public static void loadConfig() {
        String filename = "setting.properties";
        Properties properties = new Properties();
        try {
            properties.load(new java.io.FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountid = Long.parseLong(properties.getProperty("accountid").toString());
        accountpassword = properties.getProperty("accountpassword").toString();
        group = Long.parseLong(properties.getProperty("group").toString());
        lastdynamicid = properties.getProperty("lastdynamicid").toString();
    }

    public static void saveConfig() {
        String filename = "setting.properties";
        Properties properties = new Properties();
        properties.setProperty("lastdynamicid", lastdynamicid);
        properties.setProperty("accountid", String.valueOf(accountid));
        properties.setProperty("group", String.valueOf(group));
        properties.setProperty("accountpassword", accountpassword);
        try {
            properties.store(new java.io.FileOutputStream(filename),null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
