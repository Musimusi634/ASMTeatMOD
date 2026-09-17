package com.musimusi634.asmtestmod.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import com.musimusi634.asmtestmod.IASMTest;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Entity.class, priority = Integer.MAX_VALUE)
public abstract class EntityMixin implements IASMTest {
    @Unique private boolean ASMTestKill = false;
    @Unique private boolean ASMTestInvincible = false;

    @Override public void setASMTestKill(boolean value) {ASMTestKill = value;}
    @Override public void setASMTestInvincible(boolean value) {ASMTestInvincible = value;}

    @Override public boolean isASMTestKilled() {return ASMTestKill;}
    @Override public boolean isASMTestInvincible() {return ASMTestInvincible;}
}
