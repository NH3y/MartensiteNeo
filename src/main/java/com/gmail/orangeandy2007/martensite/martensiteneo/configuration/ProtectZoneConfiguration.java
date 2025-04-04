package com.gmail.orangeandy2007.martensite.martensiteneo.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ProtectZoneConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.ConfigValue<Double> RADIUS;
	public static final ModConfigSpec.ConfigValue<Double> MCHANCE;
	public static final ModConfigSpec.ConfigValue<String> SPLITER;
	static {
		RADIUS = BUILDER.comment("the distance from player that martensite start working").define("Radius", (double) 24);
		MCHANCE = BUILDER.comment("the lowest chance that entity tick").define("Minimal Chance", 0.0);
		SPLITER = BUILDER.comment("Use the char you won't use in chunk name (Don't use speical char like backslash)").define("Chunk Name Spliter", "/");

		SPEC = BUILDER.build();
	}

}
