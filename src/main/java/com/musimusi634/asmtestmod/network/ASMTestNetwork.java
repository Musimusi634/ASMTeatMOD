package com.musimusi634.asmtestmod.network;

import com.musimusi634.asmtestmod.ASMTestMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ASMTestNetwork {
    public static final String PROTOCOL_VERSION = "1";
    private static int id = 0;
    @SuppressWarnings("removal")
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ASMTestMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(
            id++,
            ASMTestSyncPacket.class,
            ASMTestSyncPacket::encode,
            ASMTestSyncPacket::decode,
            ASMTestSyncPacket::handle
        );
    }
}
