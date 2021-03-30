package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.command.command
import com.github.syari.yululi.craftking.Main.Companion.plugin

object UsefulCommand {
    fun register() {
        plugin.command("pickup") {
            execute {
                sender.spigot().sendMessage(PointCalculator.pickUpMessage)
            }
        }
    }
}
