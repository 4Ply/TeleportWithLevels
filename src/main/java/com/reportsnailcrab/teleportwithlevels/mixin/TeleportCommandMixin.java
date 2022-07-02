package com.reportsnailcrab.teleportwithlevels.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.Cancellable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Optional;

/**
 * Requires XP to teleport using the `/teleport` command. Cost is exponential based on distance.
 */
@Mixin(TeleportCommand.class)
abstract class TeleportCommandMixin {
    @Inject(method = "execute", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private static void executeTeleportToEntity(ServerCommandSource source, Collection<? extends Entity> targets, Entity destination, CallbackInfoReturnable<Integer> callbackInfo) throws CommandSyntaxException {
        handleTeleportMultiTargetEvent(source, targets, callbackInfo, destination.getPos());
    }

    @Inject(method = "Lnet/minecraft/server/command/TeleportCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/command/argument/PosArgument;Lnet/minecraft/command/argument/PosArgument;Lnet/minecraft/server/command/TeleportCommand$LookTarget;)I", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private static void executeTeleportToLocation(ServerCommandSource source, Collection<? extends Entity> targets, ServerWorld destinationWorld, PosArgument location, @Nullable PosArgument rotation, @Coerce Object facingLocation, CallbackInfoReturnable<Integer> callbackInfo) throws CommandSyntaxException {
        handleTeleportMultiTargetEvent(source, targets, callbackInfo, location.toAbsolutePos(source));
    }

    private static void handleTeleportMultiTargetEvent(ServerCommandSource source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> callbackInfo, Vec3d destination) throws CommandSyntaxException {
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            // Console commands, etc are ignored.
            if (source.getEntity() == null) {
                Log.warn(LogCategory.MIXIN, "Source entity is null, cannot determine if type is " + ServerPlayerEntity.class.getSimpleName());
            } else {
                Log.warn(LogCategory.MIXIN, source.getEntity().getClass().getSimpleName() + " is not " + ServerPlayerEntity.class.getSimpleName());
            }
            return;
        }

        if (targets.size() > 1) {
            source.sendFeedback(new LiteralText("Too many targets! Unable to handle such complicated science.").formatted(Formatting.RED), true);
            callbackInfo.cancel();
            return;
        }

        Optional<? extends Entity> optionalEntity = targets.stream().findFirst();
        if (optionalEntity.isEmpty()) {
            return;
        }

        Entity entity = optionalEntity.get();
        handleTeleportEvent(source, entity, destination, callbackInfo);
    }

    private static void handleTeleportEvent(ServerCommandSource source, Entity entity, Vec3d destination, Cancellable callbackInfo) throws CommandSyntaxException {
        long distance = Math.round(getDistanceToEntity(entity.getPos(), destination));

        double xpCost = Math.round((5.8311 * Math.exp(0.0018 * distance)) * 100) / 100.0;
        int xpRequired = (int) Math.min(1395, xpCost); // Max levels to consume is 30
        int playerXP = source.getPlayer().totalExperience;

        if (xpRequired > playerXP) {
            String insufficientXPMessage = String.format("You have insufficient XP to teleport %s blocks (cost = %s XP [%s], you have %s XP)", distance, xpRequired, xpCost, playerXP);
            source.sendFeedback(new LiteralText(insufficientXPMessage).formatted(Formatting.RED), false);
            callbackInfo.cancel();
        } else {
            String teleportSuccessMessage = String.format("Teleporting %s blocks (cost = %s XP)", distance, xpRequired);
            source.getPlayer().addExperience(-xpRequired);
            source.sendFeedback(new LiteralText(teleportSuccessMessage).formatted(Formatting.GRAY), true);
        }
    }

    private static double getDistanceToEntity(Vec3d source, Vec3d target) {
        double deltaX = source.getX() - target.getX();
        double deltaY = source.getY() - target.getY();
        double deltaZ = source.getZ() - target.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }
}
