package com.musimusi634.asmtestmod.network;

import com.musimusi634.asmtestmod.IASMTest;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ASMTestSyncPacket {
    private final int EntityID;
    private final boolean Killed;
    private final boolean Invincible;
    private final boolean Removed;

    public ASMTestSyncPacket(Integer EntityID, Boolean Killed, Boolean Invincible,Boolean Removed) {
        this.EntityID = EntityID;
        this.Killed = Killed;
        this.Invincible = Invincible;
        this.Removed = Removed;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.EntityID);
        buf.writeBoolean(this.Killed);
        buf.writeBoolean(this.Invincible);
        buf.writeBoolean(this.Removed);
    }

    public static ASMTestSyncPacket decode(FriendlyByteBuf buf) {
        return new ASMTestSyncPacket(buf.readInt(),buf.readBoolean(),buf.readBoolean(),buf.readBoolean());
    }

    public static void handle(ASMTestSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(packet.EntityID,packet.Killed,packet.Invincible, packet.Removed, ctx))
        );
        ctx.get().setPacketHandled(true);
    }
    public static void handlePacket(Integer EntityID, Boolean Killed, Boolean Invincible,Boolean Removed, Supplier<NetworkEvent.Context> ctx) {
        if (!Minecraft.getInstance().level.isClientSide()) return;
        Entity entity = Minecraft.getInstance().level.getEntity(EntityID);
        ((IASMTest) entity).setASMTestInvincible(Invincible);
        ((IASMTest) entity).setASMTestKill(Killed);
        ((IASMTest) entity).setASMTestRemove(Removed);
    }
}
