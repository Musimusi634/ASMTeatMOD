package com.musimusi634.asmtestmod;

import com.mojang.logging.LogUtils;
import com.musimusi634.asmtestmod.agent.AgentLoader;
import com.musimusi634.asmtestmod.network.ASMTestNetwork;
import com.musimusi634.asmtestmod.transformer.GenericTransformer;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ASMTestMod.MODID)
public class ASMTestMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "asmtestmod";

    public ASMTestMod() {
        ASMTestNetwork.register();
        GenericTransformer.initialize();
        try {
            AgentLoader.loadAgent();
        } catch (Exception e) {
            LOGGER.error("Agent Load Failed",e);
        }
    }
}
