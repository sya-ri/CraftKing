package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.component.buildTextComponent
import com.github.syari.spigot.api.component.hoverItem
import com.github.syari.spigot.api.component.hoverText
import com.github.syari.spigot.api.component.translateComponent
import com.github.syari.spigot.api.sound.playSound
import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import net.md_5.bungee.api.ChatColor as BungeeChatColor

object PointCalculator {
    @OptIn(ExperimentalStdlibApi::class)
    private val allMaterial = buildSet {
        plugin.server.recipeIterator().forEach {
            when (it) {
                is ShapelessRecipe, is ShapedRecipe -> {
                    add(it.result.type)
                }
            }
        }
    }

    private val alreadyCrafted = mutableSetOf<Material>()
    private var pickUpMaterial = setOf<Material>()

    val Material.baseCraftPoint
        get() = if (allMaterial.contains(this)) {
            1
        } else {
            0
        }

    val Material.isFirstCraft
        get() = if (alreadyCrafted.contains(this)) {
            false
        } else {
            alreadyCrafted.add(this)
            true
        }

    val Material.isPickupCraft
        get() = pickUpMaterial.contains(this)

    @OptIn(ExperimentalStdlibApi::class)
    fun updatePickup(number: Int) {
        val materials = allMaterial.toMutableSet()
        pickUpMaterial = buildSet {
            repeat(number) {
                materials.randomOrNull()?.let {
                    materials.remove(it)
                    add(it)
                }
            }
        }
        plugin.server.spigot().broadcast(pickUpMessage)
        plugin.server.onlinePlayers.forEach {
            it.playSound(Sound.BLOCK_NOTE_BLOCK_HARP)
        }
    }

    val pickUpMessage
        get() = buildTextComponent {
            appendLine()
            append("&6&l-------[ ")
            append("&a&lピックアップ", hoverText("&dポイント3倍"))
            appendLine(" &6&l]-------")
            pickUpMaterial.forEach {
                appendLine(
                    translateComponent(it, hoverItem(ItemStack(it))).apply {
                        color = BungeeChatColor.AQUA
                    }
                )
            }
            append("")
        }
}
