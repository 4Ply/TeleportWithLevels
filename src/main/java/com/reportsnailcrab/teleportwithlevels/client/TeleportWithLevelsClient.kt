package com.reportsnailcrab.teleportwithlevels.client

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.impl.util.log.Log
import net.fabricmc.loader.impl.util.log.LogCategory

@Suppress("UNUSED")
@Environment(EnvType.CLIENT)
object TeleportWithLevelsClient : ClientModInitializer {
    override fun onInitializeClient() {
        val activeDispatcher = ClientCommandManager.getActiveDispatcher()
        val createBuilder = activeDispatcher!!.findNode(listOf("tp")).createBuilder().requires { true }
        Log.info(LogCategory.GENERAL, "Registering fake /tp command")
        Log.info(LogCategory.GENERAL, activeDispatcher.root.getChild("tp").command.toString())
        activeDispatcher.register(createBuilder as LiteralArgumentBuilder<FabricClientCommandSource>?)

        Log.info(LogCategory.GENERAL, "TeleportWithLevelsClient has been initialized")
    }
}
