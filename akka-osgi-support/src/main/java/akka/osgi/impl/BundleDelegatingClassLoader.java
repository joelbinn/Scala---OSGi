package akka.osgi.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * A bundle delegating classloader implemenation - this will try to load classes and resources from the bundle
 * specified first and if there's a classloader specified, that will be used as a fallback
 */
public class BundleDelegatingClassLoader extends ClassLoader {

    private Bundle bundle;
    private ClassLoader classLoader;

    public BundleDelegatingClassLoader(Bundle bundle, ClassLoader classLoader) throws ClassNotFoundException {
        this.bundle = bundle;
        this.classLoader = classLoader;

    }

    public BundleDelegatingClassLoader(Bundle bundle) throws ClassNotFoundException {
        this(bundle, null);
    }

    /**
     * Create a bundle delegating classloader for the bundle context's bundle
     */
    public static BundleDelegatingClassLoader apply(BundleContext context) throws ClassNotFoundException {
        return new BundleDelegatingClassLoader(context.getBundle());
    }

    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
        return bundle.loadClass(name);
    }

    @Override
    protected URL findResource(String name) {
        URL resource = bundle.getResource(name);
        if (resource == null && classLoader != null) {
            return classLoader.getResource(name);
        } else {
            return resource;
        }
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return bundle.getResources(name);
    }

    @Override
    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clazz = null;
        try {
            try {
                clazz = findClass(name);
            } catch (ClassNotFoundException e) {
                // First fall back to secondary loader
                if (classLoader != null) clazz = classLoader.loadClass(name);
            }
        } catch (ClassNotFoundException cnfe) {
            // IF we have no secondary loader or that failed as well, wrap and rethrow
            throw new ClassNotFoundException("%s from bundle %s (%s)".format(name, bundle.getBundleId(), bundle.getSymbolicName()), cnfe);
        }

        if (resolve) resolveClass(clazz);

        return clazz;
    }

    @Override
    public String toString() {
        return String.format("BundleDelegatingClassLoader(%s)", bundle.getBundleId());
    }
}

