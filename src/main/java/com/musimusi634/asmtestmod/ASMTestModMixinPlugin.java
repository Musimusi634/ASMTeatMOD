package com.musimusi634.asmtestmod;

import com.musimusi634.asmtestmod.agent.AgentLoader;
import com.musimusi634.asmtestmod.transformer.GenericTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ASMTestModMixinPlugin implements IMixinConfigPlugin {

    static {
        GenericTransformer.initialize();
        try {
            //AgentLoader.loadAgent();
        } catch (Exception e) {
            LOGGER.error("Agent Load Failed",e);
        }
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        GenericTransformer.transform(targetClass);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
