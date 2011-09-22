package cag.labs.osgi.upnpgui.impl

import gui.MainGui
import org.osgi.service.component.ComponentContext
import org.osgi.service.upnp.UPnPDevice
import collection.mutable.MutableList


class Main {
    val devices: MutableList[UPnPDevice] = new MutableList[UPnPDevice]
    var mainGui: MainGui = null

    /**
     * Activates the UPnP GUI. Invoked by the OSGi framework according to spec in Service-Component tag in the POM.
     */
    def activate(context: ComponentContext) = {
        println("Hello Scala in OSGi")
        mainGui = new MainGui
        mainGui.open()
    }

    /**
     * Deactivates the UPnP GUI. Invoked by the OSGi framework according to spec in Service-Component tag in the POM.
     */
    def deactivate(context: ComponentContext) = {
        println("Bye Scala in OSGi")
        mainGui.close()
        mainGui = null
    }

    /**
     * Adds the specified UPnP device to the set of IPnP Devices.<br>
     * Handled by Declarative Services according to the specification in the Service-Component tag in the POM.
     *
     * @param uPnPDevice the the UPnP device to add
     */
    def setUpnpDevice(uPnPDevice: UPnPDevice): Unit = {
        System.out.println("Added " + uPnPDevice.getDescriptions(null).get(UPnPDevice.FRIENDLY_NAME))
        devices += uPnPDevice
    }

    /**
     * Removes the specified UPnP device from the set of UPnP devices, when a UPnP device is deregistered
     * from the service registry.<br>
     * Handled by Declarative Services according to the specification in the Service-Component tag in the POM.
     *
     * @param uPnPDevice the UPnP device to remove
     */
    def unsetUpnpDevice(uPnPDevice: UPnPDevice): Unit = {
        System.out.println("Removed " + uPnPDevice.getDescriptions(null).get(UPnPDevice.FRIENDLY_NAME))
        devices += uPnPDevice
    }
}