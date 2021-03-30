package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import com.github.syari.spigot.api.string.toColor
import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scheduler.BukkitTask

object Manager {
    var limitTime = 15 * 60L

    var elapsedTime = 0L

    var pickupNumber = 5

    var pickupPeriod = 3 * 60L

    var spawnLocation = (plugin.server.getWorld("world") ?: plugin.server.worlds.first()).spawnLocation

    private var task: BukkitTask? = null

    private var timeBar = Bukkit.createBossBar(null, BarColor.GREEN, BarStyle.SOLID)

    var isWait
        get() = task == null
        set(value) {
            if (value) {
                timeBar.removeAll()
                task?.cancel()
                task = null
            } else if (task == null) {
                var countDown = 5
                task = plugin.runTaskTimer(20) {
                    if (countDown < 1) {
                        cancel()
                        task = plugin.runTaskTimer(20) {
                            if (limitTime < elapsedTime) {
                                isWait = true
                                plugin.server.onlinePlayers.forEach {
                                    it.sendTitle("&c&l終了".toColor(), null, 0, 50, 10)
                                    it.playSound(Sound.ENTITY_LIGHTNING_BOLT_IMPACT)
                                }
                                plugin.runTaskLater(60) {
                                    plugin.server.onlinePlayers.forEach {
                                        it.teleport(spawnLocation)
                                    }
                                    plugin.runTaskLater(100) {
                                        PointCounter.showRank()
                                    }
                                }
                            } else {
                                if (elapsedTime != limitTime && elapsedTime % pickupPeriod == 0L) {
                                    PointCalculator.updatePickup(pickupNumber)
                                }
                                val (barColor, chatColor) = getColor(elapsedTime)
                                timeBar.color = barColor
                                timeBar.setTitle("&6&lCraftKing &7- $chatColor&l${formatTime(elapsedTime)}".toColor())
                                timeBar.progress = elapsedTime / limitTime.toDouble()
                                plugin.server.onlinePlayers.forEach(timeBar::addPlayer)
                                elapsedTime ++
                            }
                        }
                    } else {
                        countDown --
                        plugin.server.onlinePlayers.forEach {
                            it.sendTitle("&a&l$countDown".toColor(), null, 0, 20, 0)
                            it.playSound(Sound.ENTITY_PLAYER_LEVELUP, pitch = 2F)
                        }
                    }
                }
            }
        }

    private fun getColor(time: Long): Pair<BarColor, ChatColor> {
        val remainTime = limitTime - time
        return when {
            remainTime < 15 -> BarColor.RED to ChatColor.RED
            remainTime < 60 -> BarColor.YELLOW to ChatColor.YELLOW
            else -> BarColor.GREEN to ChatColor.GREEN
        }
    }

    private fun formatTime(time: Long): String {
        val seconds = time % 60
        val minutes = time / 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
