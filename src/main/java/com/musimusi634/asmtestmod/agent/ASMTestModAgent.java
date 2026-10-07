package com.musimusi634.asmtestmod.agent;

import java.lang.instrument.Instrumentation;

public class ASMTestModAgent {
    private static Instrumentation inst;
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[ASMTestModAgent] agentmain loaded!");
        inst = instrumentation;
        instrumentation.addTransformer(new AgentTransformer(), true);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation){
        agentmain(agentArgs, instrumentation);
    }

    public static void retransformALL(){
        //TODO:これが正常に完了しない問題を修正する
        long startTime = System.nanoTime();
        System.out.println("[ASMTestModAgent] retransform started");
        for (Class<?> Class : inst.getAllLoadedClasses()) {
            try {
                inst.retransformClasses(Class);
            }catch (Exception ignored) {
            }
        }
        System.out.println("[ASMTestModAgent] retransform finished");
        System.out.println("[ASMTestModAgent] retransform took " + ((System.nanoTime() - startTime) / 1000000) + " ms");
    }
}
