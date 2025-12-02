package net.kenji.kenjis_stamina_system.mixins;

import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerOverride {

    public Player player;

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void onAddExhaustion(float amount, CallbackInfo ci) {
        if (player == null) return; // safety check
        StaminaManager staminaManager = StaminaManager.get(player);
        if(staminaManager.canUseHunger) {
            if (amount != StaminaManager.foodExhaustAmount && amount != StaminaManager.puffedFoodExhaustAmount) {
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(Player pPlayer, CallbackInfo ci) {
       player = pPlayer;
    }
}
