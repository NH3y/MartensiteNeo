package com.gmail.orangeandy2007.martensite.martensiteneo.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ProtectZoneConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.ConfigValue<Boolean> RANDOM_SEARCH;
	public static final ModConfigSpec.ConfigValue<Integer> RANDOM_LOOP;
	public static final ModConfigSpec.ConfigValue<Double> RADIUS;
	public static final ModConfigSpec.ConfigValue<Double> MCHANCE;
	static {
		RADIUS = BUILDER.comment("the distance from player that martensite start working").define("Radius", (double) 24);
		MCHANCE = BUILDER.comment("the lowest chance that entity tick").define("Minimal Chance", 0.0);
		RANDOM_SEARCH = BUILDER.comment("calculate distance without going through all the player but with lower accuracy").define("Random Search", false);
		RANDOM_LOOP = BUILDER.comment("the loop count of the random search. The higher the number, the better in accuracy").define("loop count", 20);
		SPEC = BUILDER.build();
	}

}
