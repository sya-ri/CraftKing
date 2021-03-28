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
                argument { addAll("start", "stop") }
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
                    else -> {
                        sender.send(
                            """
                                &fコマンド一覧
                                &a/$label start &7ゲームを開始します
                                &a/$label start &7ゲームを停止します
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
