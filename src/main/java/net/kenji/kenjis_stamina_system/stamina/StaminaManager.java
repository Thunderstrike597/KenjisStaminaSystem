package net.kenji.kenjis_stamina_system.stamina;

import net.kenji.kenjis_stamina_system.KenjisStaminaSystem;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = KenjisStaminaSystem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StaminaManager {

    public static Map<StaminaCause.StaminaCauses, Double> staminaCauseValues = new HashMap<>();

    private static final Map<UUID, StaminaManager> PLAYER_STAMINA = new HashMap<>();
    private static final Map<UUID, Integer> regenDelay = new HashMap<>();
    private static final Map<UUID, StaminaCause.StaminaCauses> currentStaminaCause = new HashMap<>();

    public static float staminaConsumptionMultiplier = 1;
    public static float staminaGainMultiplier = 1;
    public static float staminaPuffedDivideAmount = 2.85f;

    public static void rebuildStaminaCauseMap() {
        staminaCauseValues.clear();

        staminaCauseValues.put(
                StaminaCause.StaminaCauses.SPRINTING,
                KenjisStaminaSystemCommon.SPRINT_STAMINA_REGEN_DELAY.get()
        );
        staminaCauseValues.put(
                StaminaCause.StaminaCauses.JUMPING,
                KenjisStaminaSystemCommon.JUMP_STAMINA_REGEN_DELAY.get()
        );
        staminaCauseValues.put(
                StaminaCause.StaminaCauses.BLOCK_BREAK,
                KenjisStaminaSystemCommon.BLOCK_BREAK_STAMINA_REGEN_DELAY.get()
        );
        staminaCauseValues.put(
                StaminaCause.StaminaCauses.EPIC_FIGHT_ATTACK,
                KenjisStaminaSystemCommon.ATTACK_STAMINA_REGEN_DELAY.get()
        );
        staminaCauseValues.put(
                StaminaCause.StaminaCauses.EPIC_FIGHT_DODGE,
                KenjisStaminaSystemCommon.DODGE_STAMINA_REGEN_DELAY.get()
        );
        staminaCauseValues.put(
                StaminaCause.StaminaCauses.EPIC_FIGHT_GUARD,
                KenjisStaminaSystemCommon.GUARD_STAMINA_REGEN_DELAY.get()
        );
    }
    public static class StaminaCause {

        public enum StaminaCauses {
            SPRINTING,
            JUMPING,
            BLOCK_BREAK,
            EPIC_FIGHT_ATTACK,
            EPIC_FIGHT_DODGE,
            EPIC_FIGHT_GUARD
        }
    }

    public static void setPlayerStaminaCause(Player player, StaminaCause.StaminaCauses cause){
        currentStaminaCause.put(player.getUUID(), cause);
        double currentCause = staminaCauseValues.get(currentStaminaCause.getOrDefault(player.getUUID(), StaminaCause.StaminaCauses.SPRINTING));
        StaminaManager.get(player).setStaminaDelay((float) currentCause);
    }

    public static double getFoodExhaustionAmount() {
        return KenjisStaminaSystemCommon.HUNGER_DEPLETE_AMOUNT.get();
    }
    public static double getPuffedFoodExhaustionAmount() {
        return KenjisStaminaSystemCommon.HUNGER_DEPLETE_AMOUNT_PUFFED.get();
    }

    public static double getMinStaminaLimit() {
        return KenjisStaminaSystemCommon.STAMINA_MIN_LIMIT.get();
    }
    public static double getMaxStaminaLimit() {
        return KenjisStaminaSystemCommon.STAMINA_MAX_LIMIT.get();
    }
    public static double getMinStaminaGainRate() {
        return KenjisStaminaSystemCommon.MIN_STAMINA_GAIN_RATE.get();
    }
    public static double getMaxStaminaGainRate() {
        return KenjisStaminaSystemCommon.MAX_STAMINA_GAIN_RATE.get();
    }

    public static double getSprintConsumptionAmount() {
        return KenjisStaminaSystemCommon.SPRINT_CONSUME_AMOUNT.get();
    }
    public static double getBlockBreakConsumptionAmount() {
        return KenjisStaminaSystemCommon.BLOCK_BREAK_CONSUME_AMOUNT.get();
    }
    public static double getJumpConsumptionAmount() {
        return KenjisStaminaSystemCommon.JUMP_CONSUME_AMOUNT.get();
    }
    public static double getStaminaRegainDelay() {

        return KenjisStaminaSystemCommon.SPRINT_STAMINA_REGEN_DELAY.get();
    }


    public boolean isPuffed = false;
    private boolean wasConsumingStamina = false;

    public float currentMaxStamina = 50;
    public float staminaGainRate = 20f;
    public float currentStamina = currentMaxStamina;
    public float currentStaminaDelay = (float)getStaminaRegainDelay();

    public boolean canUseHunger = false;

    public static StaminaManager INSTANCE;
    private boolean startTimer = false;
    private float t = 0;

    public void setStaminaDelay(float delay){
        currentStaminaDelay = delay;
    }

    public static StaminaManager get(Player player) {
        return PLAYER_STAMINA.computeIfAbsent(player.getUUID(), id -> new StaminaManager());
    }

    public boolean hasValidStamina(){
        return currentStamina > 0 && !isPuffed;
    }

    public boolean canConsumeStamina(Player player) {
        if (player != null) {
            return player.isSprinting();
        } else return false;
    }

    private void setStamina(float amount) {
        currentStamina = Mth.clamp(amount, 0, currentMaxStamina);
    }

    public void consumeStamina(float amount) {
        if(hasValidStamina()) {
            currentStamina -= (amount / 10) * staminaConsumptionMultiplier;
            wasConsumingStamina = true;
        }
    }

    private void gainStamina(float amount, Player player) {
       if(ConditionHandler.shouldRegen(player)) {
           currentStamina += (amount / 10) * staminaGainMultiplier;
           float currentExhaustAmount = !isPuffed ? (float) getFoodExhaustionAmount() : (float) getPuffedFoodExhaustionAmount();
           player.getFoodData().addExhaustion(currentExhaustAmount);
       }
    }

    public void manageSprinting(Player player) {
        if (player != null) {
            if (currentStamina <= 0) {
                if (player.level().isClientSide) {
                    Minecraft mc = Minecraft.getInstance();
                    if (player.isSprinting() && mc.options.toggleSprint().get()) {
                        mc.options.keySprint.setDown(true);
                        setPlayerStaminaCause(player, StaminaCause.StaminaCauses.SPRINTING);
                        startTimer = true;
                    }
                    if (startTimer) {
                        t += 1;
                        if (t >= 5) {
                            startTimer = false;
                            mc.options.keySprint.setDown(false);
                        }
                    }
                }
                player.setSprinting(false);
            }
        }
    }

    private void manageStaminaConsumption(Player player) {
        float currentGainRate = isPuffed ? staminaGainRate / staminaPuffedDivideAmount : staminaGainRate;

        if (!player.isCreative()) {
            if (canConsumeStamina(player) && currentStamina > 0 && !player.isFallFlying()) {
                consumeStamina((float)getSprintConsumptionAmount());
            }
            else if (!wasConsumingStamina) {
                if (currentStamina < currentMaxStamina)
                    gainStamina(currentGainRate, player);
            }
            if(currentStamina <= 0 && !isPuffed){
                isPuffed = true;
            }else if(isPuffed && currentStamina >= currentMaxStamina){
                isPuffed = false;
            }
            if (!canConsumeStamina(player) && wasConsumingStamina && currentStaminaDelay > 0) {
               if(ConditionHandler.shouldRegen(player))
                currentStaminaDelay -= 1;
            }
            if (currentStaminaDelay <= 0) {
                wasConsumingStamina = false;
                currentStaminaDelay = (float)getStaminaRegainDelay();
            }
        } else {
            currentStamina = currentMaxStamina;
        }
    }

    public void ManageMaxStamina(Player player) {
        int currentHunger = player.getFoodData().getFoodLevel();
        int maxFoodLevel = 20;

        float hungerPercent = (float) currentHunger / maxFoodLevel;

        staminaGainRate = Mth.lerp(hungerPercent, (float)getMinStaminaGainRate() ,(float) getMaxStaminaGainRate());
        currentMaxStamina = Mth.lerp(hungerPercent, (float)getMinStaminaLimit(), (float)getMaxStaminaLimit());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if(KenjisStaminaSystemCommon.ENABLE_STAMINA_LOGIC.get()) {
          if (event.phase == TickEvent.Phase.END) {
              Player player = event.player;
              if (player != null) {
                  StaminaManager stamina = get(player);
                  stamina.manageSprinting(player);
                  stamina.manageStaminaConsumption(player);
                  stamina.ManageMaxStamina(player);

              }
          }
      }

    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(KenjisStaminaSystemCommon.ENABLE_STAMINA_LOGIC.get()) {
            Player player = event.getPlayer();
            StaminaManager stamina = get(player);
            stamina.consumeStamina((float)getBlockBreakConsumptionAmount());
            setPlayerStaminaCause(player, StaminaCause.StaminaCauses.BLOCK_BREAK);

        }
    }
    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if(KenjisStaminaSystemCommon.ENABLE_STAMINA_LOGIC.get()) {
            if (event.getEntity() instanceof Player player) {
                StaminaManager stamina = get(player);
                stamina.consumeStamina((float)getJumpConsumptionAmount());
                setPlayerStaminaCause(player, StaminaCause.StaminaCauses.JUMPING);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(KenjisStaminaSystemCommon.ENABLE_STAMINA_LOGIC.get()) {

            StaminaManager stamina = get(event.getEntity());
            stamina.setStamina(stamina.currentMaxStamina);
        }
    }
    @SubscribeEvent
    public static void onPlayerHeal(LivingHealEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player player){
            StaminaManager stamina = get(player);
            if(!player.hasEffect(MobEffects.REGENERATION)){
                if(player.getFoodData().getFoodLevel() >= 16){
                    stamina.canUseHunger = true;
                    regenDelay.put(player.getUUID(), 38);
                }
            }
        }
    }

    @SubscribeEvent
    public static void PlayerTickTimer(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if(regenDelay.containsKey(player.getUUID())) {
            int ticksLeft = regenDelay.get(player.getUUID()) - 1;
            if(ticksLeft <= 0) {
                StaminaManager stamina = get(player);
                System.out.println("Task triggered after delay!");
                regenDelay.remove(player.getUUID());
                stamina.canUseHunger = false;
            } else {
                regenDelay.put(player.getUUID(), ticksLeft);
            }
        }

    }
}
