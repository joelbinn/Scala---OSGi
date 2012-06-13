package cag.labs.osgi.upnpgui.impl.gui

import swing.{Frame, Button}
import scala.swing.event.WindowClosing

/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-09-22
 * Time: 23.00
 */

class MainGui extends Frame {
    title = "Hello, World!"
    contents = new Button {
        text = "Click Me!"
    }

    listenTo(this)

    reactions += {
        case wc: WindowClosing => println("closing window")
    }

}