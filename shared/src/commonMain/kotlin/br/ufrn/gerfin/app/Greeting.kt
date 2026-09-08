package br.ufrn.gerfin.app

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = sayHello(platform.name)
}
