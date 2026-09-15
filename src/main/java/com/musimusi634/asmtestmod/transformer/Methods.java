package com.musimusi634.asmtestmod.transformer;

import com.mojang.logging.LogUtils;
import com.musimusi634.asmtestmod.ASMTestMod;

public class Methods {
    public static void onGetHealth() {
        LogUtils.getLogger().info("getHealth called!");
    }
}
