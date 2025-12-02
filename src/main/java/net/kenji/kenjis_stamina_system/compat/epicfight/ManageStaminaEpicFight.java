package net.kenji.kenjis_stamina_system.compat.epicfight;


import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class ManageStaminaEpicFight {


    public static void OnInit(){
        MinecraftForge.EVENT_BUS.register(new EpicFightEventHandler());
    }

    public static class EpicFightEventHandler {

       private static double getEpicFightAttackStaminaConsumption(){
           return KenjisStaminaSystemCommon.EPIC_FIGHT_ATTACK_CONSUME_AMOUNT.get();
       }
        private static double getEpicFightDodgeStaminaConsumption(){
            return KenjisStaminaSystemCommon.EPIC_FIGHT_DODGE_CONSUME_AMOUNT.get();
        }

        boolean attacking = false;
        boolean dodging = false;

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event){
            Entity entity = event.player;

            if(entity instanceof Player player){
                StaminaManager staminaManager = StaminaManager.get(player);

                player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {

                    if (cap instanceof PlayerPatch<?> playerPatch){
                        SkillContainer dodgeSkill = playerPatch.getSkill(SkillSlots.DODGE);

                        attacking = playerPatch.getAnimator().getEntityState().attacking();
                        dodging = dodgeSkill.isActivated();

                        dodgeSkill.setDisabled(!staminaManager.hasValidStamina());

                        if(!staminaManager.hasValidStamina()) {
                            playerPatch.getEntityState().setState(EntityState.CAN_BASIC_ATTACK, false);
                        }



                        float currentStamina = staminaManager.currentStamina;
                        float currentMaxStamina = staminaManager.currentMaxStamina;

                        float epicFightMaxStamina = playerPatch.getMaxStamina();
                        float percent = currentStamina / currentMaxStamina;
                        float finalStamina = percent * epicFightMaxStamina;

                        playerPatch.setStamina(finalStamina);
                        if(dodging){
                            staminaManager.consumeStamina((float) getEpicFightDodgeStaminaConsumption());
                        }
                    }
                });

                if(attacking) {
                    staminaManager.consumeStamina((float) getEpicFightAttackStaminaConsumption());
                }
            }
        }
    }
}
