package com.musimusi634.asmtestmod.transformer;

import com.musimusi634.asmtestmod.IASMTest;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Methods {
    public static float onGetHealth(float OriginalValue,LivingEntity entity) {
        if (((IASMTest) entity).isASMTestKilled()){
            return 0;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return entity.getMaxHealth();
        }
        return OriginalValue;
    }

    public static float onGetMaxHealth(float OriginalValue,LivingEntity entity) {
        if (((IASMTest) entity).isASMTestKilled()){
            return 0;
        }else if (((IASMTest) entity).isASMTestInvincible() && (20 > OriginalValue)){
            return 20;
        }
        return OriginalValue;
    }

    public static boolean onIsAlive(boolean OriginalValue,Entity entity) {
        if (((IASMTest) entity).isASMTestKilled()){
            return false;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return true;
        }
        return OriginalValue;
    }

    public static boolean onIsDeadOrDying(boolean OriginalValue,LivingEntity entity) {
        return !onIsAlive(OriginalValue,entity);
    }

    public static boolean onIsRemoved(boolean OriginalValue, Entity entity) {
        if (((IASMTest) entity).isASMTestRemoved()){
            return true;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return false;
        }
        return OriginalValue;
    }

    public static Entity.RemovalReason onGetRemovalReason(Entity.RemovalReason OriginalValue, Entity entity) {
        if (((IASMTest) entity).isASMTestRemoved()){
            return Entity.RemovalReason.KILLED;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return null;
        }
        return OriginalValue;
    }

    public static float hookGetHealth(LivingEntity entity,float OriginalValue){ return onGetHealth(OriginalValue, entity); }
    public static float hookGetMaxHealth(LivingEntity entity,float OriginalValue){ return onGetMaxHealth(OriginalValue, entity); }
    public static boolean hookIsAlive(LivingEntity entity,boolean OriginalValue){ return onIsAlive(OriginalValue, entity); }
    public static boolean hookIsDeadOrDying(LivingEntity entity,boolean OriginalValue){ return onIsDeadOrDying(OriginalValue, entity); }
    public static boolean hookIsRemoved(LivingEntity entity,boolean OriginalValue){ return onIsRemoved(OriginalValue, entity); }
    public static Entity.RemovalReason hookGetRemovalReason(LivingEntity entity,Entity.RemovalReason OriginalValue){ return onGetRemovalReason(OriginalValue, entity); }
}
