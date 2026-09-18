package com.musimusi634.asmtestmod.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEntity.class, priority = Integer.MAX_VALUE)
public class LivingEntityMixin {
}
