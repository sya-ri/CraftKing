package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.command.command
import com.github.syari.yululi.craftking.Main.Companion.plugin
import com.github.syari.yululi.craftking.PointCounter.Companion.craftPointCounter
import org.bukkit.entity.Player

object UsefulCommand {
    fun register() {
        plugin.command("help") {
            execute {
                sender.send(
                    """
                        &fコマンド一覧
                        &a/pickup &7ピックアップアイテムを確認します
                        &a/point &7現在のポイントを確認します
                    """.trimIndent()
                )
            }
        }
        plugin.command("pickup") {
            execute {
                sender.spigot().sendMessage(PointCalculator.pickUpMessage)
            }
        }
        plugin.command("point") {
            execute {
                val player = sender as? Player ?: return@execute sender.send("&cプレイヤーからのみ実行出来るコマンドです")
                sender.send("&f現在のポイントは &a${player.craftPointCounter.points} &fです")
            }
        }
    }
}
