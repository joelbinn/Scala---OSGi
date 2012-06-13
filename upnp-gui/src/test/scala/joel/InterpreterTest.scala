package joel

import tools.nsc.interpreter.IMain

/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-09-25
 * Time: 22.20
 */

object InterpreterTest extends App {
    val interpreter: IMain = new IMain {
        override protected def parentClassLoader = getClass.getClassLoader
    }
    val result = interpreter.interpret("println(\"hej\")\nval a = 3\n")
    println(result)
}