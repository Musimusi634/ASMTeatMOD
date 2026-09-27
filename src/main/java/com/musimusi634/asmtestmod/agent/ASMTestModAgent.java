package com.musimusi634.asmtestmod.agent;

import java.lang.instrument.Instrumentation;

public class ASMTestModAgent {
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[ASMTestModAgent] agentmain loaded!");
        instrumentation.addTransformer(new AgentTransformer(), true);
        for (Class<?> Class : instrumentation.getAllLoadedClasses()) {
            try {
                instrumentation.retransformClasses(Class);
            }catch (Exception ignored) {
            }
        }
    }
    public static void premain(String agentArgs, Instrumentation instrumentation){
        agentmain(agentArgs, instrumentation);
    }
}
