package org.jboss.shamrock.runtime;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

/**
 * Helper method that is invoked from generated bytecode during image processing
 */
public class ResourceHelper {

    public static void registerResources(String resource) {
        try {
            Class resourcesClass = Class.forName("com.oracle.svm.core.jdk.Resources");
            Method register = resourcesClass.getDeclaredMethod("registerResource", String.class, InputStream.class);

            Enumeration<URL> resources = ResourceHelper.class.getClassLoader().getResources(resource);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream in = url.openStream()) {
                    register.invoke(null, resource, in);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource " + resource, e);
        }
    }

}