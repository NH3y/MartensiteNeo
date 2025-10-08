package com.gmail.orangeandy2007.martensite.martensiteneo.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ProtectZoneConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.ConfigValue<Boolean> RANDOM_SEARCH;
	public static final ModConfigSpec.ConfigValue<Integer> RANDOM_LOOP;
	public static final ModConfigSpec.ConfigValue<Double> RADIUS;
	public static final ModConfigSpec.ConfigValue<Double> MCHANCE;
    public static final ModConfigSpec.BooleanValue ALWAYS_APPEND;
	static {

        BUILDER.push("Basic");
		RADIUS = BUILDER.comment("the distance from player that martensite start working").define("Radius", (double) 24);
		MCHANCE = BUILDER.comment("the lowest chance that entity tick").define("Minimal Chance", 0.0);
        BUILDER.pop();

        BUILDER.push("Random Search");
		RANDOM_SEARCH = BUILDER.comment("calculate distance without going through all the player but with lower accuracy").define("Random Search", false);
		RANDOM_LOOP = BUILDER.comment("the loop count of the random search. The higher the number, the better in accuracy").define("Loop Count", 20);
		BUILDER.pop();

        ALWAYS_APPEND = BUILDER.comment("always append the chunk index after chunk name").define("Always Append", false);

        SPEC = BUILDER.build();
	}

}
