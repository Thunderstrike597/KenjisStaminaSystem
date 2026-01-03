package net.kenji.kenjis_stamina_system.stamina;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionHandler {

    public interface RegenCondition {
        boolean canRegen(Player player);
    }

    private static final List<RegenCondition> conditions = new ArrayList<>();

    public static void registerCondition(RegenCondition c) {
        conditions.add(c);
    }

    public static boolean shouldRegen(Player player) {
        for (RegenCondition c : conditions) {
            if (!c.canRegen(player)) {
                return false; // Stop regen
            }
        }
        return true; // All passed
    }
}
