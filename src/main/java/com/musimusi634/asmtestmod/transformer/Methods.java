package com.musimusi634.asmtestmod.transformer;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class Methods {
    public static float onGetHealth(LivingEntity entity,float health) {
        if (entity.hasEffect(MobEffects.GLOWING)){
            return entity.getMaxHealth();
        }
        return health;
    }
}
