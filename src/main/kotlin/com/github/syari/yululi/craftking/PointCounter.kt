package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.uuid.UUIDPlayer
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
    }
}
