package de.pangaea.xref.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties properties;

    public AppConfig() {
        properties = new Properties();
        InputStream is;
        try {
            // Try loading from external file conf/application.properties
            is = new FileInputStream("conf/application.properties");
        } catch (IOException e) {
            // Fallback to classpath
            is = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (is == null) {
                throw new RuntimeException("Resource 'application.properties' not found in filesystem or classpath.");
            }
        }

        try (InputStream finalIs = is) {
            properties.load(finalIs);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
    }

    // DB properties
    public String getDbUrl() {return properties.getProperty("db.url");}
    public String getDbUser() {return properties.getProperty("db.user");}
    public String getDbPassword() {return properties.getProperty("db.password");}

    //ENA FTP properties
    public String getFtpHost() { return properties.getProperty("ftp.host"); }
    public String getFtpPath() { return properties.getProperty("ftp.path"); }
    public String getFtpUser() { return properties.getProperty("ftp.user"); }
    public String getFtpPassword() { return properties.getProperty("ftp.password"); }

    // Application settings
    public int getDbConnectionRetries() { return Integer.parseInt(properties.getProperty("db.connection.retries", "3")); }
    public long getDbConnectionRetryDelay() { return Long.parseLong(properties.getProperty("db.connection.retry.delay.ms", "5000")); }



}