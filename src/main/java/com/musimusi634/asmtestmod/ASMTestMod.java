package com.musimusi634.asmtestmod;

import com.musimusi634.asmtestmod.network.ASMTestNetwork;
import net.minecraftforge.fml.common.Mod;

@Mod(ASMTestMod.MODID)
public class ASMTestMod {
    public static final String MODID = "asmtestmod";
    static{
        ASMTestNetwork.register();
    }
}
