package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.yululi.craftking.Main.Companion.plugin

object CraftKingCommand {
    fun register() {
        plugin.command("craftking") {
            aliases = listOf("ck")
            permission = "craftking.command"
            tab {
                argument { addAll("start", "stop", "pickup") }
            }
            execute {
                when (args.lowerOrNull(0)) {
                    "start" -> {
                        if (Manager.isWait) {
                            Manager.isWait = false
                            sender.send("&fゲームが開始されました")
                        } else {
                            sender.send("&c既にゲームは開始しています")
                        }
                    }
                    "stop" -> {
                        if (Manager.isWait) {
                            sender.send("&c既にゲームは停止しています")
                        } else {
                            Manager.isWait = true
                            sender.send("&fゲームが停止されました")
                        }
                    }
                    "pickup" -> {
                        args.getOrNull(1)?.let {
                            val number = it.toIntOrNull() ?: return@execute sender.send("&cピックアップする種類数を整数で入力してください")
                            PointCalculator.updatePickup(number)
                        } ?: sender.spigot().sendMessage(PointCalculator.pickUpMessage)
                    }
                    else -> {
                        sender.send(
                            """
                                &fコマンド一覧
                                &a/$label start &7ゲームを開始します
                                &a/$label start &7ゲームを停止します
                                &a/$label pickup &7ピックアップアイテムを確認します
                                &a/$label pickup <種類数> &7ピックアップを手動で更新します
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
