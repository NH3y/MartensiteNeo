package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.ClaudePlayerDetect;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.chunkData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import org.jetbrains.annotations.NotNull;


@EventBusSubscriber
public class EventUnload {
    public static void CloseDetect(){
        boolean GUEnable = FeatureConfiguration.GroupUnload.get();
        if(GUEnable){
            NeoForge.EVENT_BUS.register(EventUnload.class);
        }else{
            NeoForge.EVENT_BUS.unregister(EventUnload.class);
        }
    }
    @SubscribeEvent
    private static void onEntityUnload(@NotNull EntityLeaveLevelEvent event){
        if (event.getEntity().getRemovalReason() == null) return;
        if(event.getEntity().getRemovalReason().name().equals("DISCARDED") && event.getLevel().getChunk(event.getEntity().blockPosition()) instanceof chunkData data && !data.martensiteNeo$getSafeChunk()){Entity main = event.getEntity();
            if(ClaudePlayerDetect.execute(main.level(),main) < 2*ProtectZoneConfiguration.RADIUS.get()) return;
            if(main instanceof Mob central){
                AABB aabb = AABB.ofSize(central.position(),2,2,2);
                event.getLevel().getEntities(central ,aabb).stream().limit(10).filter(EventUnload::checkDespawn).forEach(Entity::discard);
                }
            }
        }

    private static boolean checkDespawn(@NotNull Entity entity) {
        boolean exclude = entity.hasCustomName() || entity.hasControllingPassenger() || entity instanceof Animal || !entity.getType().canSummon();
        boolean include = entity instanceof LivingEntity living && living.isAlive();
        return !exclude && include;
    }
}
