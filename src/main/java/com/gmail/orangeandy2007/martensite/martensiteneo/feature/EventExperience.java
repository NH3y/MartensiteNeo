package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.experienceData;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Comparator;

import static com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration.GrabExp;

@EventBusSubscriber
public class EventExperience {
	@SubscribeEvent
	public static void onPickupXP(PlayerXpEvent.PickupXp event) {
		if (event != null && GrabExp.get()) {
            Entity entity = event.getEntity();
            execute(entity.level(), entity.getX(), entity.getY(), event.getEntity().getZ());
        }
	}

	private static void execute(@NotNull LevelAccessor world, double x, double y, double z) {
		int XpStack = 1;
		if (1 == world.getLevelData().getGameTime() % 5) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> EntFound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.2 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(EntCnd -> EntCnd.distanceToSqr(_center))).toList();
				for (Entity entity : EntFound) {
					if (entity instanceof ExperienceOrb orb) {
						XpStack += orb.getValue()*((experienceData)orb).martensiteNeo$getCount();
						if (!entity.level().isClientSide())
							entity.discard();
					}
				}
			}
			System.out.println(XpStack);
			if (world instanceof ServerLevel _level)
				_level.addFreshEntity(new ExperienceOrb(_level, x, y, z, XpStack));
		}
	}
}
