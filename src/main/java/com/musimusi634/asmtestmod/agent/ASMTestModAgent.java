package com.musimusi634.asmtestmod.agent;

import com.musimusi634.asmtestmod.ASMTestMod;

import java.lang.instrument.Instrumentation;

public class ASMTestModAgent {
    public static void agentmain(
            String agentArgs,
            Instrumentation instrumentation) {

        ASMTestMod.LOGGER.info("[ASMTestModAgent] agentmain loaded!");
    }
}
