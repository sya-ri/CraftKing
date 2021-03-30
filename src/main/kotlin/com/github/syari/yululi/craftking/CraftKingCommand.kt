package com.github.syari.yululi.craftking

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.spigot.api.config.type.data.ConfigLocationDataType
import com.github.syari.yululi.craftking.Main.Companion.plugin
import org.bukkit.entity.Player

object CraftKingCommand {
    fun register() {
        plugin.command("craftking") {
            aliases = listOf("ck")
            permission = "craftking.command"
            tab {
                argument { addAll("start", "stop", "pickup", "limit", "elapsed", "spawn") }
                argument("pickup") { addAll("list", "force", "number", "period") }
                argument("pickup force", "pickup number") { add(Manager.pickupNumber.toString()) }
                argument("pickup period") { add(Manager.pickupPeriod.toString()) }
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
                        when (args.lowerOrNull(1)) {
                            "list" -> {
                                sender.spigot().sendMessage(PointCalculator.pickUpMessage)
                            }
                            "force" -> {
                                args.getOrNull(2)?.toIntOrNull()?.let {
                                    PointCalculator.updatePickup(it)
                                } ?: return@execute sender.send("&cピックアップする種類数を整数で入力してください")
                            }
                            "number" -> {
                                args.getOrNull(2)?.toIntOrNull()?.let {
                                    Manager.pickupNumber = it
                                    sender.send("&f自動ピックアップの種類数を &a$it &fにしました")
                                } ?: return@execute sender.send("&c自動ピックアップする種類数を整数で入力してください")
                            }
                            "period" -> {
                                args.getOrNull(2)?.toLongOrNull()?.let {
                                    Manager.pickupPeriod = it
                                    sender.send("&f自動ピックアップの間隔を &a$it &f秒にしました")
                                } ?: return@execute sender.send("&c自動ピックアップする間隔を整数で入力してください")
                            }
                            else -> {
                                sender.send(
                                    """
                                        &fコマンド一覧
                                        &a/$label pickup list &7ピックアップアイテムを確認します
                                        &a/$label pickup force <種類数> &7ピックアップを手動で更新します
                                        &a/$label pickup number <種類数> &7自動ピックアップ時の種類数を変更します
                                        &a/$label pickup period <秒数> &7自動ピックアップの間隔を変更します
                                    """.trimIndent()
                                )
                            }
                        }
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
                    "spawn" -> {
                        val player = sender as? Player ?: return@execute sender.send("&cプレイヤーからのみ実行出来るコマンドです")
                        Manager.spawnLocation = player.location
                        player.send("&f終了後のスポーン地点を &a${ConfigLocationDataType.locationToString(player.location)} &fに変更しました")
                    }
                    else -> {
                        sender.send(
                            """
                                &fコマンド一覧
                                &a/$label start &7ゲームを開始します
                                &a/$label stop &7ゲームを停止します
                                &a/$label pickup &7ピックアップアイテムを確認します
                                &a/$label pickup force <種類数> &7ピックアップを手動で更新します
                                &a/$label limit &7制限時間を確認します
                                &a/$label limit <秒数> &7制限時間を変更します
                                &a/$label elapsed &7経過時間を確認します
                                &a/$label elapsed <秒数> &7経過時間を変更します
                                &a/$label spawn &7終了後のスポーン地点を変更します
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
