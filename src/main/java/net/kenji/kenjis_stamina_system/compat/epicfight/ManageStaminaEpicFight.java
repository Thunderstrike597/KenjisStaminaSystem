package net.kenji.kenjis_stamina_system.compat.epicfight;


import net.kenji.kenjis_stamina_system.KenjisStaminaSystem;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.events.EntityEvents;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.damagesource.DamageSourceElements;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        static Map<UUID, Boolean> changedStateFromStamina = new HashMap<>();
        static Map<UUID, Boolean> isSameBlockTick = new HashMap<>();

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event){
            Entity entity = event.player;

            if(entity instanceof Player player){
                StaminaManager staminaManager = StaminaManager.get(player);

                player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {

                    if (cap instanceof PlayerPatch<?> playerPatch){
                        SkillContainer dodgeSkill = playerPatch.getSkill(SkillSlots.DODGE);
                        if(playerPatch.getAnimator().getEntityState().attacking()) {
                            onStaminaAttack(playerPatch, staminaManager);
                        }


                        if(dodgeSkill != null){
                            boolean dodging = dodgeSkill.isActivated();
                            if(dodging){
                                staminaManager.consumeStamina((float) getEpicFightDodgeStaminaConsumption());
                                StaminaManager.setPlayerStaminaCause(player, StaminaManager.StaminaCause.StaminaCauses.EPIC_FIGHT_DODGE);
                            }
                        }


                        float currentStamina = staminaManager.currentStamina;
                        float currentMaxStamina = staminaManager.currentMaxStamina;

                        float epicFightMaxStamina = playerPatch.getMaxStamina();
                        float percent = currentStamina / currentMaxStamina;
                        float finalStamina = percent * epicFightMaxStamina;

                        playerPatch.setStamina(finalStamina);

                        if(!staminaManager.hasValidStamina()) {
                            playerPatch.getEntityState().setState(EntityState.CAN_BASIC_ATTACK, false);
                            if(playerPatch.getEntityState().getState(EntityState.CAN_SKILL_EXECUTION)) {
                                changedStateFromStamina.putIfAbsent(player.getUUID(), true);
                                playerPatch.getEntityState().setState(EntityState.CAN_SKILL_EXECUTION, false);
                            }
                        }
                        else if(changedStateFromStamina.getOrDefault(player.getUUID(), false)) {
                            playerPatch.getEntityState().setState(EntityState.CAN_SKILL_EXECUTION, true);
                            changedStateFromStamina.remove(player.getUUID());
                        }
                    }
                });
            }
        }
        private void onStaminaAttack(PlayerPatch<?> playerPatch, StaminaManager staminaManager) {
            CapabilityItem itemCap = playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            if (itemCap instanceof WeaponCapability weaponCap) {
                if(playerPatch instanceof ServerPlayerPatch serverPlayerPatch) {
                    List<AnimationProvider<?>> combos = weaponCap.getAutoAttckMotion(playerPatch);
                    AnimationPlayer animPlayer = serverPlayerPatch.getServerAnimator().animationPlayer;
                    DynamicAnimation currentAnim = animPlayer.getAnimation();
                    for(AnimationProvider<?> combo : combos){
                        if(combo.get() == currentAnim){
                           StaticAnimation staticAnim = (StaticAnimation) currentAnim.getRealAnimation();
                           if(staticAnim instanceof BasicAttackAnimation basicAttack){
                               for (AttackAnimation.Phase phase : basicAttack.phases) {
                                   float antic = phase.antic;
                                   float contact = phase.contact;
                                   if(animPlayer.getElapsedTime() > antic && animPlayer.getElapsedTime() < contact){
                                       staminaManager.consumeStamina((float) (double) KenjisStaminaSystemCommon.EPIC_FIGHT_ATTACK_CONSUME_AMOUNT.get());
                                       StaminaManager.setPlayerStaminaCause(playerPatch.getOriginal(), StaminaManager.StaminaCause.StaminaCauses.EPIC_FIGHT_ATTACK);
                                   }
                               }
                           }
                        }
                    }
                }
            }
        }
    }
}
