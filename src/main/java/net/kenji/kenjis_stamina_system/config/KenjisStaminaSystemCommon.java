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
    public static ForgeConfigSpec.ConfigValue<Double> ICARUS_FLY_CONSUME_AMOUNT;


    public static ForgeConfigSpec.ConfigValue<Double> STAMINA_REGEN_DELAY;






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
                .comment("Epic Fight Attack Consume Amount")
                .defineInRange("How much stamina attacking With epic fight system consumes", 38f, 15, 180);
        EPIC_FIGHT_DODGE_CONSUME_AMOUNT = BUILDER
                .comment("Epic Fight DODGE Consume Amount")
                .defineInRange("How much stamina dodging With epic fight system consumes", 85f, 15, 180);


        ICARUS_FLY_CONSUME_AMOUNT = BUILDER
                .comment("Icarus Fly Consume Amount")
                .defineInRange("How much stamina flapping icarus wings when flying consumes", 1.5f, 0.5f, 8f);




        BUILDER.pop();


        BUILDER.push("Misc");
        STAMINA_REGEN_DELAY = BUILDER
                .comment("Stamina Regen Delay")
                .defineInRange("The small amount of time before stamina begins to regenerate again", 40f, 28.0f, 140.0f);


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
