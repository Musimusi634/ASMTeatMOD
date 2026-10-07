package com.musimusi634.asmtestmod.handler;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.musimusi634.asmtestmod.ASMTestMod;
import com.musimusi634.asmtestmod.IASMTest;
import com.musimusi634.asmtestmod.network.ASMTestNetwork;
import com.musimusi634.asmtestmod.network.ASMTestSyncPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = ASMTestMod.MODID)
public class ASMTestModCommands {
    public static ArgumentBuilder<CommandSourceStack, ?> KILL;
    public static ArgumentBuilder<CommandSourceStack, ?> INVINCIBLE;
    public static ArgumentBuilder<CommandSourceStack, ?> REMOVE;

    static{
        KILL = Commands.literal("kill").then(
                Commands.argument("value", BoolArgumentType.bool())
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    boolean value = BoolArgumentType.getBool(ctx,"value");
                    for (Entity entity : EntityArgument.getEntities(ctx, "entities")) {
                        ((IASMTest) entity).setASMTestKill(value);
                        ASMTestNetwork.CHANNEL.send(
                                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                                new ASMTestSyncPacket(entity.getId(),((IASMTest) entity).isASMTestKilled(),value,((IASMTest) entity).isASMTestRemoved())
                        );
                    }
                    return 1;
                }));
        INVINCIBLE = Commands.literal("invincible").then(
                Commands.argument("value", BoolArgumentType.bool())
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            boolean value = BoolArgumentType.getBool(ctx,"value");
                            for (Entity entity : EntityArgument.getEntities(ctx, "entities")) {
                                ((IASMTest) entity).setASMTestInvincible(value);
                                ASMTestNetwork.CHANNEL.send(
                                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                                        new ASMTestSyncPacket(entity.getId(),((IASMTest) entity).isASMTestKilled(),value,((IASMTest) entity).isASMTestRemoved())
                                );
                            }
                            return 1;
                        }));
        REMOVE = Commands.literal("remove").then(
                Commands.argument("value", BoolArgumentType.bool())
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            boolean value = BoolArgumentType.getBool(ctx,"value");
                            for (Entity entity : EntityArgument.getEntities(ctx, "entities")) {
                                ((IASMTest) entity).setASMTestRemove(value);
                                ASMTestNetwork.CHANNEL.send(
                                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                                        new ASMTestSyncPacket(entity.getId(),((IASMTest) entity).isASMTestKilled(), ((IASMTest) entity).isASMTestInvincible(),value)
                                );
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
                            .then(REMOVE)
                ).then(
                        Commands.literal("retransformALL").executes(x -> {
                            try {
                                Method method = Class.forName("com.musimusi634.asmtestmod.agent.ASMTestModAgent", true, ClassLoader.getSystemClassLoader()).getMethod("retransformALL");
                                method.invoke(null);
                            } catch (Exception ignored) {
                            }
                            return 1;
                                }
                        )
        )
        );
    }
}
