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
                argument { addAll("start", "stop", "pickup", "limit", "elapsed") }
                argument("limit") { add(Manager.limitTime.toString()) }
                argument("elapsed") { add(Manager.elapsedTime.toString()) }
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
                    "limit" -> {
                        args.getOrNull(1)?.let {
                            val limitTime = it.toLongOrNull() ?: return@execute sender.send("&c制限時間を秒数で入力してください")
                            Manager.limitTime = limitTime
                        } ?: sender.send("&c現在の制限時間は ${Manager.limitTime} 秒です")
                    }
                    "elapsed" -> {
                        args.getOrNull(1)?.let {
                            val elapsedTime = it.toLongOrNull() ?: return@execute sender.send("&c経過時間を秒数で入力してください")
                            Manager.elapsedTime = elapsedTime
                        } ?: sender.send("&c現在の経過時間は ${Manager.limitTime} 秒です")
                    }
                    else -> {
                        sender.send(
                            """
                                &fコマンド一覧
                                &a/$label start &7ゲームを開始します
                                &a/$label start &7ゲームを停止します
                                &a/$label pickup &7ピックアップアイテムを確認します
                                &a/$label pickup <種類数> &7ピックアップを手動で更新します
                                &a/$label limit &7制限時間を確認します
                                &a/$label limit <秒数> &7制限時間を変更します
                                &a/$label elapsed &7経過時間を確認します
                                &a/$label elapsed <秒数> &7経過時間を変更します
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
