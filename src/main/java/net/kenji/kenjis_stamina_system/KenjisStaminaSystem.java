package net.kenji.kenjis_stamina_system;

import com.mojang.logging.LogUtils;
import net.kenji.kenjis_stamina_system.client.gui.StaminaBarManager;
import net.kenji.kenjis_stamina_system.compat.epicfight.ManageStaminaEpicFight;
import net.kenji.kenjis_stamina_system.compat.icarus.StaminaManagerIcarus;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.Conditions;
import net.kenji.kenjis_stamina_system.stamina.ConditionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KenjisStaminaSystem.MODID)
public class KenjisStaminaSystem {

    public static final String MODID = "kenjis_stamina_system";
    private static final Logger LOGGER = LogUtils.getLogger();

    // OPTIONAL – only keep these if you're registering items/blocks later
    public static final DeferredRegister<?> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<?> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static boolean isEpicFightLoaded = false;
    public static boolean isIcarusLoaded = false;

    public KenjisStaminaSystem() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register deferred registers if you need them
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);


        // (Optional) register lifecycle listeners
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KenjisStaminaSystemCommon.SPEC, "KenjisStaminaSystem-Common.toml");

    }

    public static boolean isEpicFightLoaded() {
        return isEpicFightLoaded;
    }
    public static boolean isIcarusLoaded() {
        return isIcarusLoaded;
    }

    private void commonSetup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        isEpicFightLoaded = ModList.get().isLoaded("epicfight");
        isIcarusLoaded = ModList.get().isLoaded("icarus");

        if(isEpicFightLoaded()) {
            ManageStaminaEpicFight.OnInit();
        }
        if(isIcarusLoaded()){
            StaminaManagerIcarus.OnInit();
        }
        ConditionHandler.registerCondition(Conditions.fallFlying);
        ConditionHandler.registerCondition(Conditions.inWater);

        LOGGER.info("[Kenji's Stamina System] Common setup initialized.");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @net.minecraftforge.eventbus.api.SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[Kenji's Stamina Rework] Client setup initialized.");
        }
        @SubscribeEvent
        public static void registerTextures(RegisterClientReloadListenersEvent event) {

            TextureManager tm = Minecraft.getInstance().getTextureManager();

            tm.register(StaminaBarManager.BG, new SimpleTexture(StaminaBarManager.BG));
            tm.register(StaminaBarManager.BAR_GREEN, new SimpleTexture(StaminaBarManager.BAR_GREEN));
        }
    }
}
