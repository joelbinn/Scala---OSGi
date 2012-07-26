package akka.osgi;

import akka.actor.ActorSystem;
import akka.osgi.impl.BundleDelegatingClassLoader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.osgi.framework.BundleContext;

/**
 * Factory class to create ActorSystem implementations in an OSGi environment.  This mainly involves dealing with
 * bundle classloaders appropriately to ensure that configuration files and classes get loaded properly
 */
public class OsgiActorSystemFactory {

    /**
     * Classloader that delegates to the bundle for which the factory is creating an ActorSystem
     */
    private BundleDelegatingClassLoader classloader;
    private BundleContext context;

    public OsgiActorSystemFactory(BundleContext context) {
        this.context = context;
        try {
            this.classloader = new BundleDelegatingClassLoader(context.getBundle());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static OsgiActorSystemFactory apply(BundleContext context) {
        return new OsgiActorSystemFactory(context);
    }

    /**
     * Creates the [[akka.actor.ActorSystem]], using the name specified.
     * <p/>
     * A default name (`bundle-<bundle id>-ActorSystem`) is assigned when you pass along [[scala.None]] instead.
     */
    public ActorSystem createActorSystem(String name) {
        return ActorSystem.apply(getActorSystemName(name), getActorSystemConfig(context), classloader);
    }

    /**
     * Strategy method to create the Config for the ActorSystem, ensuring that the default/reference configuration is
     * loaded from the akka-actor bundle.
     */
    public Config getActorSystemConfig(BundleContext context) {
        return ConfigFactory.load(classloader).withFallback(ConfigFactory.defaultReference(ActorSystem.class.getClassLoader()));
    }

    /**
     * Determine the name for the [[akka.actor.ActorSystem]]
     * Returns a default value of `bundle-<bundle id>-ActorSystem` is no name is being specified
     */
    public String getActorSystemName(String name) {
        if (name != null) return name;
        return "bundle-" + context.getBundle().getBundleId() + "-ActorSystem";
    }
}
