package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.yululi.craftking.PointCalculator.craftPoint
import com.github.syari.yululi.craftking.PointCounter.Companion.craftPointCounter
import org.bukkit.entity.Player
import org.bukkit.event.inventory.CraftItemEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<CraftItemEvent> {
            val player = it.whoClicked as? Player ?: return@event
            val pointCounter = player.craftPointCounter
            val material = it.recipe.result.type
            if (pointCounter.contains(material).not()) {
                val point = material.craftPoint
                if (0 < point) {
                    pointCounter.add(material, point)
                    player.send("&f現在の得点は &a${pointCounter.points} &fです")
                }
            }
        }
    }
}
