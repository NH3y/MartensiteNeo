package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.chunkData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.levelData;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent.Load;

@EventBusSubscriber
public class LevelTick {
    @SubscribeEvent
    public static void onChunkLoad(Load event){
        boolean boo = event.getLevel() instanceof levelData AdLevel && AdLevel.martensiteNeo$getChunks().contains(event.getChunk());
        ((chunkData)event.getChunk()).martensiteNeo$setSafeChunk(boo);
    }

    @Override
    public int hashCode() {
        return ((chunkData)this).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LevelChunk) {
            return ((chunkData) this).equals(obj);
        }
        return false;
    }
}
