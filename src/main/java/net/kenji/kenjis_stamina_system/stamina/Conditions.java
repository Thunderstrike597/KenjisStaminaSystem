package net.kenji.kenjis_stamina_system.stamina;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

public class Conditions {

    public static ConditionHandler.RegenCondition fallFlying = new ConditionHandler.RegenCondition() {
        @Override
        public boolean canRegen(Player player) {
            return !player.isFallFlying();
        }
    };
    public static ConditionHandler.RegenCondition inWater = new ConditionHandler.RegenCondition() {
        @Override
        public boolean canRegen(Player player) {
            BlockPos blockPos = player.getOnPos();
            boolean isOnFluid = player.level().isFluidAtPosition(blockPos, FluidState::isSource);
            if(player.isInWater() && isOnFluid) {
                return false;
            }
            return true;
        }
    };





}
