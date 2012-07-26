package akka.osgi;

import akka.actor.ActorSystem;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Properties;

/**
 * Abstract bundle activator implementation to bootstrap and configure an actor system in an
 * OSGi environment.  It also provides a convenience method to register the actor system in
 * the OSGi Service Registry for sharing it with other OSGi bundles.
 * <p/>
 * This convenience activator is mainly useful for setting up a single [[akka.actor.ActorSystem]] instance and sharing that
 * with other bundles in the OSGi Framework.  If you want to set up multiple systems in the same bundle context, look at
 * the [[OsgiActorSystemFactory]] instead.
 */
public abstract class ActorSystemActivator implements BundleActivator {
    private ActorSystem system;
    private ServiceRegistration registration;

    /**
     * Implement this method to add your own actors to the ActorSystem.  If you want to share the actor
     * system with other bundles, call the `registerService(BundleContext, ActorSystem)` method from within
     * this method.
     *
     * @param context the bundle context
     * @param system  the ActorSystem that was created by the activator
     */
    protected abstract void configure(BundleContext context, ActorSystem system);

    /**
     * Sets up a new ActorSystem
     *
     * @param context the BundleContext
     */
    public void start(BundleContext context) {
        system = new OsgiActorSystemFactory(context).createActorSystem(getActorSystemName(context));
        configure(context, system);
    }

    /**
     * Shuts down the ActorSystem when the bundle is stopped and, if necessary, unregisters a service registration.
     *
     * @param context the BundleContext
     */
    public void stop(BundleContext context) {
        if (registration != null) registration.unregister();
        system.shutdown();
    }

    /**
     * Register the actor system in the OSGi service registry.  The activator itself will ensure that this service
     * is unregistered again when the bundle is being stopped.
     * <p/>
     * Only one ActorSystem can be registered at a time, so any previous registration will be unregistered prior to registering the new.
     *
     * @param context the bundle context
     * @param system  the actor system
     */
    private void registerService(BundleContext context, ActorSystem system) {
        registration.unregister(); //Cleanup
        Properties properties = new Properties();
        properties.put("name", system.name());
        registration = context.registerService(ActorSystem.class.getName(), system, properties);
    }

    /**
     * By default, the [[akka.actor.ActorSystem]] name will be set to `bundle-<bundle id>-ActorSystem`.  Override this
     * method to define another name for your [[akka.actor.ActorSystem]] instance.
     *
     * @param context the bundle context
     * @return the actor system name
     */
    protected String getActorSystemName(BundleContext context) {
        return null;
    }
}
