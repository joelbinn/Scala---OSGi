package se.joelbinnquist.scala.scalaosgitest

import org.osgi.framework.{BundleContext, BundleActivator}


class Activator extends BundleActivator {
    def start(context: BundleContext) = {
        println("Hello Scala in OSGi")
    }

    def stop(context: BundleContext) = {
        println("Bye bye, Scala in OSGi")
    }
}