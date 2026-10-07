package com.musimusi634.asmtestmod.agent;

import com.musimusi634.asmtestmod.ASMTestMod;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class AgentLoader {
    private static boolean loaded = false;
    public  static void loadAgent() throws Exception {
        if (loaded) return;
        loaded = true;
        String pid = String.valueOf(ProcessHandle.current().pid());
        VirtualMachine vm = null;
        String AgentJarPath = buildAgentJar().toAbsolutePath().toString();

        try {
            ASMTestMod.LOGGER.info(AgentJarPath);
            vm = VirtualMachine.attach(pid);
            vm.loadAgent(AgentJarPath);
        } catch (Throwable t) {
            ASMTestMod.LOGGER.error("agent load failed!", t);
        }finally {
            if (vm != null) vm.detach();
        }
    }

    private static Path buildAgentJar() throws Exception {
        Manifest manifest = new Manifest();
        Attributes manifestAttributes = manifest.getMainAttributes();
        manifestAttributes.putValue("Manifest-Version", "1.0");
        manifestAttributes.putValue("Agent-Class", "com.musimusi634.asmtestmod.agent.ASMTestModAgent");
        manifestAttributes.putValue("Premain-Class", "com.musimusi634.asmtestmod.agent.ASMTestModAgent");
        manifestAttributes.putValue("Can-Redefine-Classes","true");
        manifestAttributes.putValue("Can-Retransform-Classes","true");

        Path agentJar = Files.createTempFile("asmtestmod-agent-",".jar");
        JarOutputStream jaroutputstream = new JarOutputStream(Files.newOutputStream(agentJar),manifest);

        copyClassFromJar(jaroutputstream,"com/musimusi634/asmtestmod/agent/AgentTransformer.class");
        copyClassFromJar(jaroutputstream,"com/musimusi634/asmtestmod/agent/ASMTestModAgent.class");
        copyClassFromJar(jaroutputstream,"com/musimusi634/asmtestmod/transformer/GenericTransformer.class");
        jaroutputstream.close();
        
        return agentJar;
    }

    private static void copyClassFromJar(JarOutputStream jaroutputstream,String path) throws Exception {
        jaroutputstream.putNextEntry(new JarEntry(path));
        jaroutputstream.write(ASMTestMod.class.getClassLoader().getResourceAsStream(path).readAllBytes());
        jaroutputstream.closeEntry();;
    }
}
