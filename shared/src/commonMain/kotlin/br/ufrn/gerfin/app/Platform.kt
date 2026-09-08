package br.ufrn.gerfin.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform