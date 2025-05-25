package com.gmail.orangeandy2007.martensite.martensiteneo.mixin.manager;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.chunkData;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess implements chunkData {
    @Unique
    public boolean martensiteNeo$safeChunk = false;

    @Shadow
    private boolean loaded;

    public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable LevelChunkSection[] sections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, biomeRegistry, inhabitedTime, sections, blendingData);
    }

    @Override
    public boolean martensiteNeo$getSafeChunk() {
        return this.martensiteNeo$safeChunk;
    }

    @Override
    public void martensiteNeo$setSafeChunk(boolean boo) {
        this.martensiteNeo$safeChunk = boo;
    }

    @Override
    public int hashCode() {
        return this.getPos().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LevelChunk chunk){
            return chunk.getPos().toLong() == this.getPos().toLong();
        }
        return super.equals(obj);
    }
}
