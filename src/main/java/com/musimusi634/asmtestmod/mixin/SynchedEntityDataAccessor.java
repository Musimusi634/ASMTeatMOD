package com.musimusi634.asmtestmod.mixin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SynchedEntityData.class, priority = Integer.MAX_VALUE)
public interface SynchedEntityDataAccessor {

    @Accessor("entity")
    Entity getEntity();
}
