package com.musimusi634.asmtestmod.transformer;

import com.mojang.logging.LogUtils;

public class Methods {
    public static void onGetHealth() {
        LogUtils.getLogger().info("getHealth called!");
    }
}
