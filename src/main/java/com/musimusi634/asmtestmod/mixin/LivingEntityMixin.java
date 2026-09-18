package com.musimusi634.asmtestmod.mixin;

import com.musimusi634.asmtestmod.IASMTest;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = Integer.MAX_VALUE)
public class LivingEntityMixin {
    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    public  void tickDeathInject(CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        if (((IASMTest) entity).isASMTestInvincible()) ci.cancel();
    }
}
