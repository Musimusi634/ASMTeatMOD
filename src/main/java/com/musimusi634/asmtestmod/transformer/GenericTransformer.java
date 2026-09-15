package com.musimusi634.asmtestmod.transformer;

import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.ITransformerActivity;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

public class GenericTransformer {

    static boolean initialized = false;

    public static void initialize() {
        final Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("starting initialize...");
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
            LOGGER.info("initialize finished");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.info("initialize failed");
            LOGGER.error(e.toString());
        }
        initialized = true;
    }

    public static int transform(ClassNode classNode) {
        final Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("LivingEntity found!");
        for (MethodNode method : classNode.methods) {
            if (!"m_21223_".equals(method.name)) continue;
            InsnList instructions = new InsnList();
            LOGGER.info("getHealth found!");
            for (AbstractInsnNode Isin : method.instructions) {
                if (!(Isin.getOpcode() == Opcodes.FRETURN)) continue;
                LOGGER.info("return found!");
                instructions.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "com/musimusi634/asmtestmod/transformer/Methods",
                        "onGetHealth",
                        "(net.minecraft.world.entity.LivingEntity;F)F",
                        false
                ));
                method.instructions.insertBefore(Isin,instructions);
                LOGGER.info("inject completed!");
                return ILaunchPluginService.ComputeFlags.SIMPLE_REWRITE;
            }
        }
        return ILaunchPluginService.ComputeFlags.NO_REWRITE;
    }
}