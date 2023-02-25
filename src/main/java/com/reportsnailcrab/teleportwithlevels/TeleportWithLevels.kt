package com.reportsnailcrab.teleportwithlevels

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.impl.util.log.Log
import net.fabricmc.loader.impl.util.log.LogCategory
import net.minecraft.server.command.ServerCommandSource

@Suppress("UNUSED")
object TeleportWithLevels : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            Log.info(LogCategory.GENERAL, "Registering /xtp command")

            val redirect = dispatcher.root.getChild("tp").redirect as LiteralCommandNode<ServerCommandSource>
            dispatcher.register(LiteralArgumentBuilder.literal<ServerCommandSource?>("xtp").redirect(redirect))
        })

        Log.info(LogCategory.GENERAL, "TeleportWithLevels has been initialized")
    }
}
