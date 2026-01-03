package net.kenji.kenjis_stamina_system.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class KenjisStaminaSystemCommon {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_UI;
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_STAMINA_LOGIC;

    public static ForgeConfigSpec.ConfigValue<Double> STAMINA_MAX_LIMIT;
    public static ForgeConfigSpec.ConfigValue<Double> STAMINA_MIN_LIMIT;

    public static ForgeConfigSpec.ConfigValue<Double> MAX_STAMINA_GAIN_RATE;
    public static ForgeConfigSpec.ConfigValue<Double> MIN_STAMINA_GAIN_RATE;

    public static ForgeConfigSpec.ConfigValue<Double> SPRINT_CONSUME_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> BLOCK_BREAK_CONSUME_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> JUMP_CONSUME_AMOUNT;

    public static ForgeConfigSpec.ConfigValue<Double> EPIC_FIGHT_ATTACK_CONSUME_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> EPIC_FIGHT_DODGE_CONSUME_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> EPIC_FIGHT_GUARD_CONSUME_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> ICARUS_FLY_CONSUME_AMOUNT;

    public static ForgeConfigSpec.ConfigValue<Double> SPRINT_STAMINA_REGEN_DELAY;
    public static ForgeConfigSpec.ConfigValue<Double> JUMP_STAMINA_REGEN_DELAY;
    public static ForgeConfigSpec.ConfigValue<Double> BLOCK_BREAK_STAMINA_REGEN_DELAY;


    public static ForgeConfigSpec.ConfigValue<Double> ATTACK_STAMINA_REGEN_DELAY;
    public static ForgeConfigSpec.ConfigValue<Double> DODGE_STAMINA_REGEN_DELAY;
    public static ForgeConfigSpec.ConfigValue<Double> GUARD_STAMINA_REGEN_DELAY;


    public static ForgeConfigSpec.ConfigValue<Double> HUNGER_DEPLETE_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<Double> HUNGER_DEPLETE_AMOUNT_PUFFED;







    static {


        BUILDER.push("Stamina Values");

        STAMINA_MAX_LIMIT = BUILDER
                .comment("Full Stamina Limit")
                .defineInRange("The max stamina value for when the hunger bar is full", 220.0f, 50.0f, 500.0f);
        STAMINA_MIN_LIMIT = BUILDER
                .comment("Hungry Stamina Limit")
                .defineInRange("The max stamina value for when the hunger bar is empty", 220.0f, 50.0f, 500.0f);
        MAX_STAMINA_GAIN_RATE = BUILDER
                .comment("Full Stamina Gain Rate")
                .defineInRange("The max stamina gain rate for when the hunger bar is full", 25, 10, 60.0f);
        MIN_STAMINA_GAIN_RATE = BUILDER
                .comment("Hungry Stamina Gain Rate")
                .defineInRange("The max stamina gain rate for when the hunger bar is empty", 18, 5, 50.0f);
        SPRINT_CONSUME_AMOUNT = BUILDER
                .comment("Sprint Consume Amount")
                .defineInRange("How much stamina sprinting consumes", 2.5f, 0.5f, 12f);

        JUMP_CONSUME_AMOUNT = BUILDER
                .comment("Jump Consume Amount")
                .defineInRange("How much stamina jumping consumes", 28f, 8f, 60.0f);


        BLOCK_BREAK_CONSUME_AMOUNT = BUILDER
                .comment("Block Break Consume Amount")
                .defineInRange("How much stamina breaking blocks consume", 15f, 2, 50.0f);
        BUILDER.pop();

        BUILDER.push("Compat Stamina Values");

        EPIC_FIGHT_ATTACK_CONSUME_AMOUNT = BUILDER
                .comment("Epic Fight 'ATTACK' Consume Amount")
                .defineInRange("How much stamina attacking With epic fight's system consumes", 55f, 0.1, 180);
        EPIC_FIGHT_DODGE_CONSUME_AMOUNT = BUILDER
                .comment("Epic Fight 'DODGE' Consume Amount")
                .defineInRange("How much stamina dodging With epic fight's system consumes", 85f, 0.1, 180);
        EPIC_FIGHT_GUARD_CONSUME_AMOUNT = BUILDER
                .comment("Epic Fight 'GUARD' Consume Amount")
                .defineInRange("How much stamina blocking an attack With epic fight's system consumes", 120f, 0.1, 180);

        ICARUS_FLY_CONSUME_AMOUNT = BUILDER
                .comment("Icarus Fly Consume Amount")
                .defineInRange("How much stamina flapping icarus wings when flying consumes", 1.5f, 0.5f, 8f);




        BUILDER.pop();
        BUILDER.push("Exhaustion Values");

        HUNGER_DEPLETE_AMOUNT = BUILDER
                .comment("Hunger Exhaustion Amount")
                .defineInRange("How much hunger is depleted when stamina is regenerating", 0.075, 0.0, 1);
        HUNGER_DEPLETE_AMOUNT_PUFFED = BUILDER
                .comment("Puffed Hunger Exhaustion Amount")
                .defineInRange("How much hunger is depleted when stamina is regenerating when puffed", 0.12, 0.0, 1.5);


        BUILDER.pop();
        BUILDER.push("Misc");
        SPRINT_STAMINA_REGEN_DELAY = BUILDER
                .comment("Sprint Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after sprinting", 40d, 0.1d, 140d);
        ATTACK_STAMINA_REGEN_DELAY = BUILDER
                .comment("Attack Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after attacking with epic fight", 80d, 0.1d, 140d);
        DODGE_STAMINA_REGEN_DELAY = BUILDER
                .comment("Dodge Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after dodging with epic fight", 70d, 0.1d, 140d);
        GUARD_STAMINA_REGEN_DELAY = BUILDER
                .comment("Guard Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after blocking an attack with epic fight", 100d, 0.1d, 140d);
        JUMP_STAMINA_REGEN_DELAY = BUILDER
                .comment("Jump Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after jumping", 50d, 0.1d, 140d);
        BLOCK_BREAK_STAMINA_REGEN_DELAY = BUILDER
                .comment("Block Break Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again after breaking a block", 58d, 0.1d, 140d);


        BUILDER.pop();

        BUILDER.push("Debug Settings");

        ENABLE_UI = BUILDER
                .comment("Enable UI")
                .define("Is UI Enabled", true);
        ENABLE_STAMINA_LOGIC = BUILDER
                .comment("Enable Stamina Logic")
                .define("Whether Stamina Functions", true);



        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
