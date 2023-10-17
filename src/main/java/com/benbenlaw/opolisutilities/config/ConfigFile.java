package com.benbenlaw.opolisutilities.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ConfigFile {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> homeStoneCooldown;
    public static final ForgeConfigSpec.ConfigValue<Boolean> homeStoneTakesDamage;

    public static final ForgeConfigSpec.ConfigValue<String> discordURL;
    public static final ForgeConfigSpec.ConfigValue<String> modpackVersion;
    public static final ForgeConfigSpec.ConfigValue<String> modpackName;
    public static final ForgeConfigSpec.ConfigValue<Double> basicLootBoxDropChance;
    public static final ForgeConfigSpec.ConfigValue<Integer> crookBoost;

    static {
        BUILDER.push("Opolis Utilities Config File");

        homeStoneCooldown = BUILDER.comment("Cooldown in ticks of the home stone, default = 6000")
                .define("Home Stone Cooldown", 6000);

        homeStoneTakesDamage = BUILDER.comment("Does the home stone take damge when teleporting, default = true")
                .define("Home Stone Durability", true);

        discordURL = BUILDER.comment("URL for your discord")
                .define("Discord URL", "https://discord.gg/benbenlaw");

        modpackName = BUILDER.comment("Modpack name")
                .define("Modpack name", "TESTMODPACK");

        modpackVersion = BUILDER.comment("Modpack Version")
                .define("Modpack Version", "VERSION");

        basicLootBoxDropChance = BUILDER.comment("Mobs basic loot bag drop chance 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.75")
                .define("Mob Drop Chance", 0.95);

        crookBoost = BUILDER.comment("Additional rolls when using the crook on leaves")
                .define("Additional Crook Rolls", 3);


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
