package com.gmail.orangeandy2007.martensite.martensiteneo.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FeatureConfiguration {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> DEATHCANCEL;
    public static final ModConfigSpec.ConfigValue<Boolean> CrampExp;
    public static final ModConfigSpec.ConfigValue<Boolean> GrabExp;
    static {
        DEATHCANCEL = BUILDER.comment("Cancel the death animation which causes lag when lots of entities die in the same time").define("Death Animation Cancel", true);
        CrampExp = BUILDER.comment("Basically trying to make them merge into only a few").define("Try Cramp XP Faster", true);
        GrabExp = BUILDER.comment("False as default since it cause some randomness on the xp you take").define("Try Take All the XP in Second", false);

        SPEC = BUILDER.build();
    }
}
