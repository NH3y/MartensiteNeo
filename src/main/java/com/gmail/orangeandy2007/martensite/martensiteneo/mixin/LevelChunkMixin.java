package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.chunkData;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements chunkData {
    @Unique
    public boolean martensiteNeo$safeChunk = false;

    @Override
    public boolean martensiteNeo$getSafeChunk() {
        return this.martensiteNeo$safeChunk;
    }

    @Override
    public void martensiteNeo$setSafeChunk(boolean boo) {
        this.martensiteNeo$safeChunk = boo;
    }
}
