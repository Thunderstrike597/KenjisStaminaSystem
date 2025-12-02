package net.kenji.kenjis_stamina_system.mixins.compat;


import dev.cammiescorner.icarus.util.IcarusHelper;
import dev.cammiescorner.icarus.util.ServerPlayerFallbackValues;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerFallbackValues.class, remap = false)
public class IcarusHungerOverride {

    @Inject(method = "exhaustionAmount", at = @At("HEAD"), cancellable = true)
    private void changeExhaustionAmount(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0f);
    }
    @Inject(method = "requiredFoodAmount", at = @At("HEAD"), cancellable = true)
    private void changeRequiredFoodAmount(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0f);
    }

}
