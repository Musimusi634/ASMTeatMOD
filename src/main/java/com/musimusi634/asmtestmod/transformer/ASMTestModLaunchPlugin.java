package com.musimusi634.asmtestmod.transformer;

import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.api.ITransformerActivity;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.EnumSet;

public class ASMTestModLaunchPlugin implements ILaunchPluginService {

    static {
        LogUtils.getLogger().info("ASMTestModLaunchPlugin LOADED");
    }

    @Override
    public String name() {
        return "asmtestmod_launch_plugin";
    }

    @Override
    public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
        if (!reason.equals(ITransformerActivity.CLASSLOADING_REASON)) return ComputeFlags.NO_REWRITE;
        return GenericTransformer.transform(classNode);
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.BEFORE,Phase.AFTER);
    }
}
