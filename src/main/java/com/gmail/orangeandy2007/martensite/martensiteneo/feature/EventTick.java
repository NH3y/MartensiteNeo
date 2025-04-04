package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.chunkData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.nmEntityCache;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.ClaudePlayerDetect;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EventTick{
	@SubscribeEvent
	public static void onEntityTick(Pre event) {
		if (!event.getEntity().level().isClientSide()) {
			boolean boo = execute(event, event.getEntity().level(), event.getEntity());
			event.setCanceled(boo);
			((nmEntityCache)event.getEntity()).martensiteNew$setNowCancel(boo);
		}
	}
	private static boolean execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null || entity instanceof Player)
			return false;
		if (entity instanceof Entity) {
			LevelChunk nowChunk = entity.level().getChunkAt(entity.blockPosition());
			double Distance = ClaudePlayerDetect.execute(world, entity);
			double Config = ProtectZoneConfiguration.RADIUS.get();
			if(Distance <= Config) return false;
			if (!((chunkData) nowChunk).martensiteNeo$getSafeChunk()) {
				double Chance = (1 - ProtectZoneConfiguration.MCHANCE.get())*((Distance - Config)/(128 - Config));
				boolean Cancel = Math.random() < Chance;
				if (event != null) {
					return entity instanceof nmEntityCache && Cancel;
                }
			}
		}
		return false;
	}
}
