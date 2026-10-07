package com.musimusi634.asmtestmod.agent;

import com.musimusi634.asmtestmod.transformer.GenericTransformer;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classfileBuffer);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        
        if (!(GenericTransformer.transform(classNode) == ILaunchPluginService.ComputeFlags.SIMPLE_REWRITE)) return null;

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}