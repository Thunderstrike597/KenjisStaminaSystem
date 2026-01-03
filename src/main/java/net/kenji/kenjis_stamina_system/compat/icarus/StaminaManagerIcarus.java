package net.kenji.kenjis_stamina_system.compat.icarus;

import dev.cammiescorner.icarus.util.IcarusHelper;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.ConditionHandler;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StaminaManagerIcarus {

    public static void OnInit(){
        MinecraftForge.EVENT_BUS.register(new IcarusEventHandler());
        ConditionHandler.registerCondition(isFlyingWithWings);
    }
    public static ConditionHandler.RegenCondition isFlyingWithWings = new ConditionHandler.RegenCondition() {
        @Override
        public boolean canRegen(Player player) {
            boolean hasWings = IcarusHelper.hasWings(player);
            if (player.isFallFlying() && IcarusHelper.hasWings(player)) {
                return false;
            } else if (!player.isFallFlying()) {
                return true;
            }
            return true;
        }
    };

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
        }
    }
}
