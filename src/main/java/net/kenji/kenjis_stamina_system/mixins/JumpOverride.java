package net.kenji.kenjis_stamina_system.mixins;

import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class JumpOverride {

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void onJumpFromGround(CallbackInfo ci) {
        Player player = (Player)(Object) this;
        StaminaManager stamina = StaminaManager.get(player);
        if (!stamina.hasValidStamina()) {
            ci.cancel();
        }
    }
}
