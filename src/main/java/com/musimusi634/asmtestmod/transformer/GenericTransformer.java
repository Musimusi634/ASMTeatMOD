package com.musimusi634.asmtestmod.transformer;

import com.musimusi634.asmtestmod.ASMTestMod;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class GenericTransformer {

    static boolean initialized = false;


    public static int transform(ClassNode classNode) {
        boolean transformed = false;
        for (MethodNode method : classNode.methods) {
            if (transformMethodBody(classNode,method)) transformed = true;
            if (transformMethodCalls(method, classNode.name)) transformed = true;
        }

        if (transformed) {
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] inject completed!");
            return ILaunchPluginService.ComputeFlags.COMPUTE_FRAMES;
        }
        return ILaunchPluginService.ComputeFlags.NO_REWRITE;
    }

    private static boolean transformMethodBody(ClassNode classNode, MethodNode method) {
        if (isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21223_", "getHealth", "()F", false)) {
            injectBody(method,
                    "onGetHealth",
                    "(FLnet/minecraft/world/entity/LivingEntity;)F",
                    "getHealth",
                    Opcodes.FRETURN
            );
            return true;
        }else if (isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21233_", "getMaxHealth", "()F", false)) {
            injectBody(method,
                    "onGetMaxHealth",
                    "(FLnet/minecraft/world/entity/LivingEntity;)F",
                    "getMaxHealth",
                    Opcodes.FRETURN
            );
            return true;
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21224_", "isDeadOrDying", "()Z", false)){
            injectBody(method,
                    "onIsDeadOrDying",
                    "(ZLnet/minecraft/world/entity/LivingEntity;)Z",
                    "isDeadOrDying",
                    Opcodes.IRETURN
            );
            return true;
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_6084_", "isAlive", "()Z", false)){
            injectBody(method,
                    "onIsAlive",
                    "(ZLnet/minecraft/world/entity/Entity;)Z",
                    "isAlive",
                    Opcodes.IRETURN
            );
            return true;
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/Entity", "m_146910_", "isRemoved", "()Z", false)){
            injectBody(method,
                    "onIsRemoved",
                    "(ZLnet/minecraft/world/entity/Entity;)Z",
                    "isRemoved",
                    Opcodes.IRETURN
            );
            return true;
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/Entity", "m_146911_", "getRemovalReason", "()Lnet/minecraft/world/entity/Entity$RemovalReason;", false)) {
            injectBody(method,
                    "onGetRemovalReason",
                    "(Lnet/minecraft/world/entity/Entity$RemovalReason;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity$RemovalReason;",
                    "getRemovalReason",
                    Opcodes.ARETURN
            );
            return true;
        }
        return false;
    }


    private static boolean transformMethodCalls(MethodNode method, String owner) {
        boolean transformed = false;
        //TODO:これをAnalyzer式の高度なやつで作り直す
        /*for (AbstractInsnNode Insn : method.instructions) {
            if (!(Insn.getOpcode() == Opcodes.INVOKESTATIC)) continue;
            MethodInsnNode methodInsn = (MethodInsnNode) Insn;
            if (!(methodInsn.owner.equals("com/musimusi634/asmtestmod/transformer/HookMethods"))) continue;
            if (!(methodInsn.getPrevious().getPrevious().getOpcode() == Opcodes.DUP)) method.instructions.remove(Insn.getPrevious().getPrevious());

            method.instructions.remove(Insn);
            method.maxStack--;
        }*/

        for (AbstractInsnNode Insn : method.instructions) {
            if (!(Insn.getOpcode() == Opcodes.INVOKEVIRTUAL || Insn.getOpcode() == Opcodes.INVOKEINTERFACE)) continue;

            MethodInsnNode methodInsn = (MethodInsnNode) Insn;
            if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21223_", "getHealth", "()F", false)) {
                injectCalls(owner, method, Insn,
                        "hookGetHealth",
                        "(Lnet/minecraft/world/entity/LivingEntity;F)F",
                        Type.FLOAT_TYPE //getHealth
                );
                transformed = true;
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21233_", "getMaxHealth", "()F", false)) {
                injectCalls(owner, method, Insn,
                        "hookGetMaxHealth",
                        "(Lnet/minecraft/world/entity/LivingEntity;F)F",
                        Type.FLOAT_TYPE //getMaxHealth
                );
                transformed = true;
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21224_", "isDeadOrDying", "()Z", false)) {
                injectCalls(owner, method, Insn,
                        "hookIsDeadOrDying",
                        "(Lnet/minecraft/world/entity/LivingEntity;Z)Z",
                        Type.BOOLEAN_TYPE //isDeadOrDying
                );
                transformed = true;
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_6084_", "isAlive", "()Z", false)) {
                injectCalls(owner, method, Insn,
                        "hookIsAlive",
                        "(Lnet/minecraft/world/entity/Entity;Z)Z",
                        Type.BOOLEAN_TYPE //isAlive
                );
                transformed = true;
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/Entity", "m_146910_", "isRemoved", "()Z", false)) {
                injectCalls(owner, method, Insn,
                        "hookIsRemoved",
                        "(Lnet/minecraft/world/entity/Entity;Z)Z",
                        Type.BOOLEAN_TYPE //isRemoved
                );
                transformed = true;
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/Entity", "m_146911_", "getRemovalReason", "()Lnet/minecraft/world/entity/Entity$RemovalReason;", false)) {
                injectCalls(owner, method, Insn,
                        "hookGetRemovalReason",
                        "(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$RemovalReason;)Lnet/minecraft/world/entity/Entity$RemovalReason;",
                        Type.getObjectType("net/minecraft/world/entity/Entity$RemovalReason")//getRemovalReason
                );
                transformed = true;
            }
        }
        return transformed;
    }

    private static void injectBody(MethodNode method, String name, String desc, String target, int returnType) {
        for (AbstractInsnNode Insn : method.instructions) {
            if (Insn.getOpcode() != Opcodes.INVOKESTATIC) continue;
            MethodInsnNode methodInsn = (MethodInsnNode) Insn;
            if (!methodInsn.owner.equals("com/musimusi634/asmtestmod/transformer/Methods")) continue;
            if (methodInsn.getPrevious().getOpcode() != Opcodes.ALOAD) continue;
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] previous body inject found!");
            method.instructions.remove(Insn.getPrevious());
            method.instructions.remove(Insn);
            method.maxStack--;
        }

        for (AbstractInsnNode Insn : method.instructions) {
            if ((Insn.getOpcode() != returnType)) continue;
            InsnList instructions = new InsnList();
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] return found!");
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            instructions.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "com/musimusi634/asmtestmod/transformer/Methods",
                    name,
                    desc,
                    false
            ));
            method.maxStack++;
            method.instructions.insertBefore(Insn, instructions);
        }
    }

    private static void injectCalls(String owner,MethodNode method, AbstractInsnNode Insn, String name, String desc, Type targetType){
        //ASMTestMod.LOGGER.info("[ASMTestModTransformer] " + target + " call found!");
        AbstractInsnNode LastWrapperInsn;
        try {
            LastWrapperInsn = findLastWrapper(Insn,owner,method,targetType);
            ASMTestMod.LOGGER.info(((MethodInsnNode) LastWrapperInsn).name);
        }catch (AnalyzerException e){
            ASMTestMod.LOGGER.error("Analyze Failed",e);
            LastWrapperInsn = Insn;
        }

        InsnList instructions = new InsnList();
        instructions.add(new InsnNode(Opcodes.DUP));
        method.maxStack++;
        method.instructions.insertBefore(Insn, instructions);

        instructions = new InsnList();
        instructions.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "com/musimusi634/asmtestmod/transformer/HookMethods",
                name,
                desc,
                false
        ));
        method.instructions.insert(LastWrapperInsn, instructions);
    }

    private static AbstractInsnNode findLastWrapper(AbstractInsnNode insn,String owner, MethodNode method, Type targetType) throws AnalyzerException{
        Analyzer<SourceValue> analyzer = new Analyzer<>(new FlowSourceInterpreter());
        Frame<SourceValue>[] frames = analyzer.analyze(owner, method);
        AbstractInsnNode target = insn;
        AbstractInsnNode consumer;

        int count = 0;
        while (true) {
            count++;
            if (count > 100) {
                ASMTestMod.LOGGER.error("findLastWrapper() took too long and was terminated " + method.name);
                break;
            }
            consumer = getConsumer(target,frames,method);
            if (consumer == null) break;
            if (!(consumer instanceof MethodInsnNode methodConsumer)) break;
            if (!(Type.getReturnType(methodConsumer.desc).equals(targetType))) break;
            Type[] argumentTypes = Type.getArgumentTypes(methodConsumer.desc);
            boolean found = false;
            for (Type argumentType : argumentTypes) {
                if (argumentType.getSort() != Type.OBJECT) continue;
                if (!(isSubclass(argumentType.getInternalName(),"net/minecraft/world/entity/Entity", false) || argumentType.getInternalName().equals("java/lang/Object")/*←これ後で変えたい*/)) continue;

                found = true;
                break;
            }
            if (!found) break;
            target = consumer;
        }
        return target;
    }

    private static AbstractInsnNode getConsumer(AbstractInsnNode target, Frame<SourceValue>[] frames, MethodNode method){
        for (int i = method.instructions.indexOf(target) + 1; i < method.instructions.size(); i++) {
            Frame<SourceValue> frame = frames[i];
            if (frame == null) continue;

            for (int s = 0; s < frame.getStackSize(); s++) {
                SourceValue sourceValue = frame.getStack(s);
                if (!sourceValue.insns.contains(target)) continue;
                AbstractInsnNode instruction = method.instructions.get(i);
                if (!(instruction instanceof MethodInsnNode methodInsn)) continue;

                Type[] argumentTypes = Type.getArgumentTypes(methodInsn.desc);

                boolean found = false;
                int consumed = frame.getStackSize();
                for (int arg = argumentTypes.length - 1; arg >= 0; arg--) {
                    consumed -= argumentTypes[arg].getSize();
                }

                for (int arg = 0; argumentTypes.length > arg; arg++) {
                    if (frame.getStack(consumed + arg).insns.contains(target)) {
                        found = true;
                        break;
                    }
                }
                if (!found) break;
                /*ASMTestMod.LOGGER.info(
                        "target={} / instruction={} / source={}",
                        ((MethodInsnNode) target).name,
                        instruction.getClass().getSimpleName(),
                        sourceValue.insns
                );*/
                return instruction;
            }
        }
        return null;
    }

    private static class FlowSourceInterpreter extends SourceInterpreter {
        public FlowSourceInterpreter() {
            super(Opcodes.ASM9);
        }
        @Override
        public SourceValue copyOperation(AbstractInsnNode insn, SourceValue source){
            return source;
        }
    }

    //All code below is from https://github.com/kosianodanngoo/TheTrialMonolith/blob/master/src/main/java/io/github/kosianodangoo/trialmonolith/transformer/GenericTransformer.java
    public static void initialize() {
        //ASMTestMod.LOGGER.info("[ASMTestModTransformer] starting initialize...");
        if (initialized) return;

        try {
            ILaunchPluginService plugin = new ASMTestModLaunchPlugin();

            Field field = Launcher.class.getDeclaredField("launchPlugins");
            field.setAccessible(true);
            LaunchPluginHandler pluginHandler = (LaunchPluginHandler) field.get(Launcher.INSTANCE);
            field = LaunchPluginHandler.class.getDeclaredField("plugins");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, ILaunchPluginService> map = (Map<String, ILaunchPluginService>) field.get(pluginHandler);
            map.put(plugin.name(), plugin);
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] initialize finished");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] initialize failed");
            //ASMTestMod.LOGGER.error(e.toString());
        }
        initialized = true;
    }

    public static boolean isSameMethod(String owner, MethodInsnNode methodInsn, String superClass, String obfName, String name, String desc, boolean isInterface) {
        if ((!obfName.equals(methodInsn.name) && !name.equals(methodInsn.name)) || !desc.equals(methodInsn.desc)) {
            return false;
        }

        return isSubclass(owner, superClass, isInterface);
    }

    public static boolean isSameMethod(String owner, MethodNode method, String superClass, String obfName, String name, String desc, boolean isInterface) {
        if ((!obfName.equals(method.name) && !name.equals(method.name)) || !desc.equals(method.desc)) {
            return false;
        }

        return isSubclass(owner, superClass, isInterface);
    }
    public static boolean isSubclass(String className, String superClass, boolean isInterface) {
        if (className.equals(superClass) || superClass.equals("java/lang/Object")) {
            return true;
        }

        if (className.equals("java/lang/Object")) {
            return false;
        }

        String currentName = className;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        while (!currentName.equals("java/lang/Object")) {
            try (InputStream is = classLoader.getResourceAsStream(currentName.concat(".class"))) {
                ClassReader classReader = new ClassReader(Objects.requireNonNull(is));
                ClassNode classNode = new ClassNode(Opcodes.ASM9);
                classReader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
                if (classNode.visibleAnnotations != null) {
                    return false;
                }
                currentName = classReader.getSuperName();
                if (currentName.equals(superClass)) {
                    return true;
                }
                if (isInterface) {
                    for (String interfaceName : classReader.getInterfaces()) {
                        if (isSubclass(interfaceName, superClass, true)) {
                            return true;
                        }
                    }
                }
            } catch (Throwable e) {
                //ASMTestMod.LOGGER.error("Failed to find super Class", e);
                return false;
            }
        }

        return false;
    }
    //

}