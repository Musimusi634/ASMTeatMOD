package com.musimusi634.asmtestmod.transformer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HookMethods {
    public static float hookGetHealth(LivingEntity entity, float OriginalValue){ return Methods.onGetHealth(OriginalValue, entity); }
    public static float hookGetMaxHealth(LivingEntity entity,float OriginalValue){ return Methods.onGetMaxHealth(OriginalValue, entity); }
    public static boolean hookIsAlive(Entity entity,boolean OriginalValue){ return Methods.onIsAlive(OriginalValue, entity); }
    public static boolean hookIsDeadOrDying(LivingEntity entity,boolean OriginalValue){ return Methods.onIsDeadOrDying(OriginalValue, entity); }
    public static boolean hookIsRemoved(Entity entity,boolean OriginalValue){ return Methods.onIsRemoved(OriginalValue, entity); }
    public static Entity.RemovalReason hookGetRemovalReason(Entity entity, Entity.RemovalReason OriginalValue){ return Methods.onGetRemovalReason(OriginalValue, entity); }
}
