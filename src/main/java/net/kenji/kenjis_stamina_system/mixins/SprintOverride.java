package net.kenji.kenjis_stamina_system.mixins;

import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class SprintOverride {


    @Inject(method = "hasEnoughFoodToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void manageSprint(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer localPlayer = (LocalPlayer)(Object)this;
        StaminaManager staminaManager = StaminaManager.get(localPlayer);
        boolean val = staminaManager.hasValidStamina() || localPlayer.isPassenger() || localPlayer.getAbilities().mayfly;

        cir.setReturnValue(val);
    }
}
