package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.component.buildTextComponent
import com.github.syari.spigot.api.component.translateComponent
import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.yululi.craftking.PointCalculator.baseCraftPoint
import com.github.syari.yululi.craftking.PointCalculator.isFirstCraft
import com.github.syari.yululi.craftking.PointCalculator.isPickupCraft
import com.github.syari.yululi.craftking.PointCounter.Companion.craftPointCounter
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockCanBuildEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRecipeDiscoverEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<CraftItemEvent>(ignoreCancelled = true) {
            if (Manager.isWait) {
                it.isCancelled = true
            } else {
                val player = it.whoClicked as? Player ?: return@event
                val pointCounter = player.craftPointCounter
                val material = it.recipe.result.type
                if (pointCounter.contains(material).not()) {
                    var point = material.baseCraftPoint
                    if (0 < point) {
                        val bonusMessage = StringBuilder()
                        if (material.isFirstCraft) {
                            bonusMessage.append(" &6[First]")
                            point *= 2
                        }
                        if (material.isPickupCraft) {
                            bonusMessage.append(" &d[Pickup]")
                            point *= 3
                        }
                        pointCounter.add(material, point)
                        plugin.server.spigot().broadcast(
                            buildTextComponent {
                                append("&7&l>> &a${player.displayName} &fが ")
                                append(
                                    translateComponent(material).apply {
                                        color = ChatColor.AQUA
                                    }
                                )
                                append("&f をクラフトしました")
                                append(bonusMessage.toString())
                            }
                        )
                        player.send("&f現在の得点は &a${pointCounter.points} &fです")
                    }
                }
            }
        }
        event<BlockCanBuildEvent> {
            if (it.isBuildable) {
                val player = it.player ?: return@event
                it.isBuildable = player.isOp || Manager.isWait.not()
            }
        }
        cancelEventIf<BlockBreakEvent> {
            it.player.isOp.not() && Manager.isWait
        }
        cancelEventIf<PlayerInteractEvent> {
            it.player.isOp.not() && Manager.isWait
        }
        cancelEventIf<EntityDamageEvent> {
            Manager.isWait
        }
        cancelEventIf<PlayerRecipeDiscoverEvent> {
            Manager.isWait
        }
        cancelEventIf<FoodLevelChangeEvent> {
            Manager.isWait
        }
        cancelEventIf<EntityTargetEvent> {
            Manager.isWait
        }
    }
}
