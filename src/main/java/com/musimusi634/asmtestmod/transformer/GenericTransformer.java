package com.musimusi634.asmtestmod.transformer;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class GenericTransformer {

    static boolean initialized = false;
    private static boolean transformed;

    public static int transform(ClassNode classNode) {
        transformed = false;
        for (MethodNode method : classNode.methods) {
            transformMethodBody(classNode,method);
            transformMethodCalls(classNode,method);
        }

        if (transformed) {
            //ASMTestMod.LOGGER.info("[ASMTestModTransformer] inject completed!");
            return ILaunchPluginService.ComputeFlags.SIMPLE_REWRITE;
        }
        return ILaunchPluginService.ComputeFlags.NO_REWRITE;
    }

    private static void transformMethodBody(ClassNode classNode, MethodNode method) {
        if (isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21223_", "getHealth", "()F", false)) {
            injectBody(method,
                    "onGetHealth",
                    "(FLnet/minecraft/world/entity/LivingEntity;)F",
                    "getHealth",
                    Opcodes.FRETURN
            );
        }else if (isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21233_", "getMaxHealth", "()F", false)) {
            injectBody(method,
                    "onGetMaxHealth",
                    "(FLnet/minecraft/world/entity/LivingEntity;)F",
                    "getMaxHealth",
                    Opcodes.FRETURN
            );
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_21224_", "isDeadOrDying", "()Z", false)){
            injectBody(method,
                    "onIsDeadOrDying",
                    "(ZLnet/minecraft/world/entity/LivingEntity;)Z",
                    "isDeadOrDying",
                    Opcodes.IRETURN
            );
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/LivingEntity", "m_6084_", "isAlive", "()Z", false)){
            injectBody(method,
                    "onIsAlive",
                    "(ZLnet/minecraft/world/entity/Entity;)Z",
                    "isAlive",
                    Opcodes.IRETURN
            );
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/Entity", "m_146910_", "isRemoved", "()Z", false)){
            injectBody(method,
                    "onIsRemoved",
                    "(ZLnet/minecraft/world/entity/Entity;)Z",
                    "isRemoved",
                    Opcodes.IRETURN
            );
        }else if(isSameMethod(classNode.name, method, "net/minecraft/world/entity/Entity", "m_146911_", "getRemovalReason", "()Lnet/minecraft/world/entity/Entity$RemovalReason;", false)) {
            injectBody(method,
                    "onGetRemovalReason",
                    "(Lnet/minecraft/world/entity/Entity$RemovalReason;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity$RemovalReason;",
                    "getRemovalReason",
                    Opcodes.ARETURN
            );
        }
    }


    private static void transformMethodCalls(ClassNode classNode, MethodNode method) {
        for (AbstractInsnNode Insn : method.instructions) {
            if (!(Insn.getOpcode() == Opcodes.INVOKESTATIC)) continue;
            MethodInsnNode methodInsn = (MethodInsnNode) Insn;
            if (!(methodInsn.owner.equals("com/musimusi634/asmtestmod/transformer/HookMethods"))) continue;
            if (!(methodInsn.getPrevious().getPrevious().getOpcode() == Opcodes.DUP)) continue;

            method.instructions.remove(Insn.getPrevious().getPrevious());
            method.instructions.remove(Insn);
            method.maxStack--;
        }

        for (AbstractInsnNode Insn : method.instructions) {
            if (!(Insn.getOpcode() == Opcodes.INVOKEVIRTUAL || Insn.getOpcode() == Opcodes.INVOKEINTERFACE)) continue;

            MethodInsnNode methodInsn = (MethodInsnNode) Insn;
            if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21223_", "getHealth", "()F", false)) {
                injectCalls(method, Insn,
                        "hookGetHealth",
                        "(Lnet/minecraft/world/entity/LivingEntity;F)F",
                        "getHealth"
                );
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21233_", "getMaxHealth", "()F", false)) {
                injectCalls(method, Insn,
                        "hookGetMaxHealth",
                        "(Lnet/minecraft/world/entity/LivingEntity;F)F",
                        "getMaxHealth"
                );
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_21224_", "isDeadOrDying", "()Z", false)) {
                injectCalls(method, Insn,
                        "hookIsDeadOrDying",
                        "(Lnet/minecraft/world/entity/LivingEntity;Z)Z",
                        "isDeadOrDying"
                );
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/LivingEntity", "m_6084_", "isAlive", "()Z", false)) {
                injectCalls(method, Insn,
                        "hookIsAlive",
                        "(Lnet/minecraft/world/entity/Entity;Z)Z",
                        "isAlive"
                );
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/Entity", "m_146910_", "isRemoved", "()Z", false)) {
                injectCalls(method, Insn,
                        "hookIsRemoved",
                        "(Lnet/minecraft/world/entity/Entity;Z)Z",
                        "isRemoved"
                );
            } else if (isSameMethod(methodInsn.owner, methodInsn, "net/minecraft/world/entity/Entity", "m_146911_", "getRemovalReason", "()Lnet/minecraft/world/entity/Entity$RemovalReason;", false)) {
                injectCalls(method, Insn,
                        "hookGetRemovalReason",
                        "(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$RemovalReason;)Lnet/minecraft/world/entity/Entity$RemovalReason;",
                        "getRemovalReason"
                );
            }
        }
    }

    private static void injectBody(MethodNode method, String name, String desc, String target, int returnType) {
        //ASMTestMod.LOGGER.info("[ASMTestModTransformer] " + target + " found!");
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
            transformed = true;
        }
    }

    private static void injectCalls(MethodNode method, AbstractInsnNode Insn, String name, String desc, String target){
        //ASMTestMod.LOGGER.info("[ASMTestModTransformer] " + target + " call found!");
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
        method.instructions.insert(Insn, instructions);
        transformed = true;
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