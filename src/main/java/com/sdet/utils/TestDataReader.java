
package com.sdet.utils;

import java.io.InputStream;
import java.util.Properties;

public class TestDataReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                TestDataReader.class.getClassLoader()
                    .getResourceAsStream("testdata.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "testdata.properties file not found"
                );
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to load test data", e
            );
        }
    }

    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {
            throw new IllegalArgumentException(
                "Test data key not found: " + key
            );
        }

        return value;
    }
}
