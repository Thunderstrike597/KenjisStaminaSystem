package net.kenji.kenjis_stamina_system.mixins.compat;

import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = LivingEntityPatch.class, remap = false)
public abstract class EpicFightAttackMixin {

    @Inject(method = "onAttackBlocked", at = @At("HEAD"))
    private void onAttackBlocked(DamageSource damageSource, LivingEntityPatch<?> opponent, CallbackInfo ci) {

        if(opponent.getOriginal() instanceof Player player) {
            StaminaManager staminaManager = StaminaManager.get(player);
            staminaManager.consumeStamina((float)(double)KenjisStaminaSystemCommon.EPIC_FIGHT_GUARD_CONSUME_AMOUNT.get());
            StaminaManager.setPlayerStaminaCause(player, StaminaManager.StaminaCause.StaminaCauses.EPIC_FIGHT_GUARD);
        }
    }

}
