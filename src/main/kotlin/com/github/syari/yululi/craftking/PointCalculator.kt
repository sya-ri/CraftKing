package com.github.syari.yululi.craftking

import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

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

    val Material.craftPoint
        get() = if (allMaterial.contains(this)) {
            1 * firstCraftBonus
        } else {
            0
        }

    private val Material.firstCraftBonus: Int
        get() = if (alreadyCrafted.contains(this)) {
            1
        } else {
            alreadyCrafted.add(this)
            3
        }
}
