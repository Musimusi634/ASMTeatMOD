package com.musimusi634.asmtestmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ASMTestMod.MODID)
public class ASMTestModCommands {
    public static ArgumentBuilder<CommandSourceStack, ?> KILL;
    public static ArgumentBuilder<CommandSourceStack, ?> INVINCIBLE;

    static{
        KILL = Commands.literal("kill").then(
                Commands.argument("value", BoolArgumentType.bool())
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    for (Entity entity : EntityArgument.getEntities(ctx, "entities")) {
                        ((IASMTest) entity).setASMTestKill(BoolArgumentType.getBool(ctx,"value"));
                    }
                    return 1;
                }));
        INVINCIBLE = Commands.literal("invincible").then(
                Commands.argument("value", BoolArgumentType.bool())
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            for (Entity entity : EntityArgument.getEntities(ctx, "entities")) {
                                ((IASMTest) entity).setASMTestInvincible(BoolArgumentType.getBool(ctx,"value"));
                            }
                            return 1;
                        }));
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ASMTestCommandRegister(event.getDispatcher());
    }

    public static void ASMTestCommandRegister(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
        Commands.literal("asmtest").then(
                Commands.argument("entities", EntityArgument.entities())
                            .then(INVINCIBLE)
                            .then(KILL)
                )
        );
    }
}
