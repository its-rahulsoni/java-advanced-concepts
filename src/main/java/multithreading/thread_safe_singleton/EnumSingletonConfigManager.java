package multithreading.thread_safe_singleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum EnumSingletonConfigManager {
    INSTANCE;

    /**
     * ✅ Why use Enum?
     * Enum guarantees only one instance, even during serialization or reflection attacks.
     *
     * JVM handles thread safety for enums.
     */
    private Properties props;

    EnumSingletonConfigManager() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            props.load(input);
            System.out.println("Configs Loaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public static void main(String[] args) {
        /**
         * QUES: How does INSTANCE access getProperty()?
         * ANS: Even though it looks magical, it's actually simple:
         * In Java, an enum constant (INSTANCE) is just an object of that enum type.
         *
         * So when you write:
         * EnumSingletonConfigManager.INSTANCE.getProperty("db.host")
         *
         * you're calling the getProperty() method on the singleton object INSTANCE.
         */
        String dbHost = EnumSingletonConfigManager.INSTANCE.getProperty("db.host");
        System.out.println("DB Host: " + dbHost);
    }
}
