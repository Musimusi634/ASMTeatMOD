package com.musimusi634.asmtestmod.transformer;

import com.musimusi634.asmtestmod.IASMTest;
import net.minecraft.world.entity.LivingEntity;

public class Methods {
    public static float onGetHealth(float health,LivingEntity entity) {
        if (((IASMTest) entity).isASMTestKilled()){
            return 0;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return entity.getMaxHealth();
        }
        return health;
    }
}
