package net.kenji.kenjis_stamina_system.compat.icarus;

import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.IcarusMixinPlugin;
import dev.cammiescorner.icarus.forge.compat.IcarusForgeCompatService;
import dev.cammiescorner.icarus.util.IcarusCompatService;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.kenji.kenjis_stamina_system.compat.epicfight.ManageStaminaEpicFight;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jline.utils.Log;

public class StaminaManagerIcarus {

    public static void OnInit(){
        MinecraftForge.EVENT_BUS.register(new IcarusEventHandler());

    }

    public static class IcarusEventHandler {

        private static double getIcarusFlightStaminaConsumption(){
            return KenjisStaminaSystemCommon.ICARUS_FLY_CONSUME_AMOUNT.get();
        }

        @SubscribeEvent
        public void PlayerTick(TickEvent.PlayerTickEvent event){
            Player player = event.player;

            boolean isKeyDown = false;
            boolean canFly = false;
            StaminaManager stamina = StaminaManager.get(player);
            if(player.isFallFlying() && IcarusHelper.hasWings(player)){

                stamina.setCanRegen(false);
                canFly = stamina.currentStamina > 0 && !stamina.isPuffed;

                if(player.level().isClientSide){
                    Minecraft mc = Minecraft.getInstance();
                   if(!canFly){
                       mc.options.keyUp.setDown(false);
                   }
                   isKeyDown = mc.options.keyUp.isDown();
                }


                if(isKeyDown){
                    stamina.consumeStamina((float)getIcarusFlightStaminaConsumption());
                }
            }
            else if(!player.isFallFlying()){
                stamina.setCanRegen(true);
            }
        }
    }
}
