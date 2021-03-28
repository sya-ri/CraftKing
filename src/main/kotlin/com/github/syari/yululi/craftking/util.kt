package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.string.toColor
import org.bukkit.command.CommandSender

fun CommandSender.send(message: String) {
    sendMessage("&b[CraftKing] &r$message".toColor())
}
