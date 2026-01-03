package net.kenji.kenjis_stamina_system.event;

import net.kenji.kenjis_stamina_system.KenjisStaminaSystem;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(
        modid = KenjisStaminaSystem.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class StaminaConfigHandler {

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent event) {
        if (!event.getConfig().getModId().equals(KenjisStaminaSystem.MODID)) return;

        StaminaManager.rebuildStaminaCauseMap();
    }
}