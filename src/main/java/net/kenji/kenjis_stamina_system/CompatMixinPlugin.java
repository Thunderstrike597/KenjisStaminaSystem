package net.kenji.kenjis_stamina_system;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CompatMixinPlugin implements IMixinConfigPlugin {

    private boolean isLoaded(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        // Example: only load this mixin if "othermod" is installed
        if (mixinClassName.equals("net.kenji.kenjis_stamina_system.mixins.compat.IcarusHungerOverride")) {
            return isLoaded("icarus");
        }
        if (mixinClassName.equals("net.kenji.kenjis_stamina_system.mixins.compat.EpicFightAttackMixin")) {
            return isLoaded("epicfight");
        }
        if (mixinClassName.equals("net.kenji.kenjis_stamina_system.mixins.compat.EpicFightPlayerMixin")) {
            return isLoaded("epicfight");
        }

        return true;
    }

    // unused methods:
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> a, Set<String> b) {}
    @Override public List<String> getMixins() { return null; }
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
