package com.musimusi634.asmtestmod.handler;

import com.musimusi634.asmtestmod.ASMTestMod;
import com.musimusi634.asmtestmod.IASMTest;
import com.musimusi634.asmtestmod.network.ASMTestNetwork;
import com.musimusi634.asmtestmod.network.ASMTestSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ASMTestMod.MODID)
public class ASMTestForgeEventHandler {
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event){
        Entity entity = event.getTarget();
        ASMTestNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                new ASMTestSyncPacket(entity.getId(),((IASMTest) entity).isASMTestKilled(),((IASMTest) entity).isASMTestInvincible())
        );
    }
}
