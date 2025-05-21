package com.gmail.orangeandy2007.martensite.martensiteneo.init;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.bus.api.SubscribeEvent;

//import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.StatueSettingConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.Martensiteneo;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Martensiteneo.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MartensiteModConfigs {
	@SubscribeEvent
	public static void register(@NotNull FMLConstructModEvent event) {
		event.enqueueWork(() -> ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ProtectZoneConfiguration.SPEC, "martensite-basic.toml"));
		event.enqueueWork(() -> ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, FeatureConfiguration.SPEC, "martensite-feature.toml"));

	}
}
