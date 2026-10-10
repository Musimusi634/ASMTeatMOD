package com.musimusi634.asmtestmod.transformer;

import com.musimusi634.asmtestmod.IASMTest;
import com.musimusi634.asmtestmod.mixin.LivingEntityAccessor;
import com.musimusi634.asmtestmod.mixin.SynchedEntityDataAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
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
        if (((IASMTest) entity).isASMTestKilled()){
            return true;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return false;
        }
        return OriginalValue;
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

    @SuppressWarnings("unchecked")
    public static <T> T onSynchedEntityDataGet(T OriginalValue, EntityDataAccessor<T> accessor, SynchedEntityData data) {
        if (!accessor.equals(LivingEntityAccessor.getDataHealthId())) return OriginalValue;
        if (!(OriginalValue instanceof Float)) return OriginalValue;
        if (!(((SynchedEntityDataAccessor) data).getEntity() instanceof LivingEntity entity)) return OriginalValue;

        if (((IASMTest) entity).isASMTestKilled()){
            //ASMTestMod.LOGGER.info("oo");
            return (T) (Float) 0.0f;
        }else if (((IASMTest) entity).isASMTestInvincible()){
            return (T) (Float) entity.getMaxHealth();
        }
        return OriginalValue;
    }

    public static <T> SynchedEntityData.DataItem<T> onSynchedEntityDataGetItem(SynchedEntityData.DataItem<T> dataItem, EntityDataAccessor<T> accessor, SynchedEntityData data) {
        dataItem.setValue(onSynchedEntityDataGet(dataItem.getValue(),accessor,data));
        return dataItem;
    }
}
