package se.joelbinnquist.scala.scalaosgitest

import org.osgi.service.component.ComponentContext


class Hello {
    def activate(context: ComponentContext) = {
        println("Hello Scala in OSGi")
    }

    def deactivate(context: ComponentContext) = {
        println("Bye Scala in OSGi")
    }
}