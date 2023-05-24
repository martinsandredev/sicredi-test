package com.andremartins.sicredi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SicrediApplication

fun main(args: Array<String>) {
	runApplication<SicrediApplication>(*args)
}
