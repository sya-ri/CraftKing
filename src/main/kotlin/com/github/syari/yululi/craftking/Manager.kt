package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.string.toColor
import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scheduler.BukkitTask

object Manager {
    var limitTime = 15 * 60L

    var elapsedTime = 0L

    var pickupNumber = 5

    var pickupPeriod = 3 * 60L

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
                task = plugin.runTaskTimer(20) {
                    if (limitTime < elapsedTime) {
                        isWait = true
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
