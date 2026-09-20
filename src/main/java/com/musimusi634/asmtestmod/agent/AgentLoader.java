package com.musimusi634.asmtestmod.agent;

import com.mojang.authlib.Agent;
import com.musimusi634.asmtestmod.ASMTestMod;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class AgentLoader {
    public  static void loadAgent() throws Exception {
        String pid = String.valueOf(ProcessHandle.current().pid());
        VirtualMachine vm = null;
        String AgentJarPath = buildAgentJar().toAbsolutePath().toString();

        try {
            ASMTestMod.LOGGER.info(AgentJarPath);
            vm = VirtualMachine.attach(pid);
            vm.loadAgent(AgentJarPath);
        } catch (Throwable t) {
            ASMTestMod.LOGGER.error("agent load failed!", t);
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

        byte[] transformerBytes = readResource("com/musimusi634/asmtestmod/agent/AgentTransformer.class");
        byte[] agentBytes = readResource("com/musimusi634/asmtestmod/agent/ASMTestModAgent.class");

        jaroutputstream.putNextEntry(new JarEntry("com/musimusi634/asmtestmod/agent/AgentTransformer.class"));
        jaroutputstream.write(transformerBytes);
        jaroutputstream.closeEntry();
        jaroutputstream.putNextEntry(new JarEntry("com/musimusi634/asmtestmod/agent/ASMTestModAgent.class"));
        jaroutputstream.write(agentBytes);
        jaroutputstream.closeEntry();
        jaroutputstream.close();
        return agentJar;
    }

    //All code below is from https://github.com/kosianodanngoo/ForbiddenThings/blob/master/src/main/java/io/github/kosianodangoo/forbiddenthings/agent/ForbiddenAgent.java
    private static byte[] readResource(String resource) throws IOException {
        try (InputStream in = ASMTestMod.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) throw new IOException("Resource not found: " + resource);
            return in.readAllBytes();
        }
    }
}
