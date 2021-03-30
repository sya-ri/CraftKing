package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.component.buildTextComponent
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player

class PointCounter {
    private val list = mutableMapOf<Material, Int>()

    fun contains(material: Material) = list.contains(material)

    fun add(material: Material, point: Int) {
        list[material] = point
    }

    val points
        get() = list.values.sum()

    companion object {
        private val list = mutableMapOf<UUIDPlayer, PointCounter>()

        private val UUIDPlayer.pointCounter
            get() = list.getOrPut(this, ::PointCounter)

        val Player.craftPointCounter
            get() = UUIDPlayer.from(this).pointCounter

        @OptIn(ExperimentalStdlibApi::class)
        fun showRank() {
            plugin.server.spigot().broadcast(
                buildTextComponent {
                    appendLine()
                    append("&6&l-------[ ")
                    append("&a&lランキング")
                    appendLine(" &6&l]-------")
                    var rank = 1
                    buildMap<Int, MutableSet<UUIDPlayer>> {
                        list.forEach {
                            getOrPut(it.value.points, ::mutableSetOf).add(it.key)
                        }
                    }.toList().sortedBy { it.first }.forEach { (point, players) ->
                        players.forEach {
                            appendLine("&a$rank &6${it.offlinePlayer.name} &a$point pt")
                        }
                        rank += players.size
                    }
                }
            )
        }
    }
}
