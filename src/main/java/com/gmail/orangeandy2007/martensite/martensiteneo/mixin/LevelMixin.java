package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.chunkData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.levelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(Level.class)
public abstract class LevelMixin implements levelData {
    @Shadow public abstract LevelChunk getChunk(int chunkX, int chunkZ);

    @Shadow @Nullable public abstract ChunkAccess getChunk(int p_46502_, int p_46503_, ChunkStatus p_331611_, boolean p_46505_);

    @Unique
    public Set<LevelChunk> martensiteNeo$safeChunks = new HashSet<>();

    @Unique
    public Map<String,int[]> martensiteNeo$ChunksList = new HashMap<>();

    @Override
    public void martensiteNeo$setSafeChunks(Map<String, int[]> input) {
        this.martensiteNeo$safeChunks.clear();
        this.martensiteNeo$ChunksList.clear();

        for(Map.Entry<String,int[]> entry : input.entrySet()){
            LevelChunk chunk = this.getChunk(entry.getValue()[0], entry.getValue()[1]);

            ((chunkData) chunk).martensiteNeo$setSafeChunk(true);
            this.martensiteNeo$safeChunks.add(chunk);
        }
        martensiteNeo$ChunksList.putAll(input);
    }

    @Override
    public int[] martensiteNeo$addSafeChunk(String name,int[] value){
        int[] v = this.martensiteNeo$ChunksList.putIfAbsent(name,value);
        this.martensiteNeo$safeChunks.add(this.getChunk(value[0],value[1]));
        ((chunkData)this.getChunk(value[0],value[1])).martensiteNeo$setSafeChunk(true);
        return v;
    }

    @Override
    public Map<String,int[]> martensiteNeo$getSafeChunks(){
        return martensiteNeo$ChunksList;
    }

    @Override
    public Set<LevelChunk> martensiteNeo$getChunks() {
        return this.martensiteNeo$safeChunks;
    }

    @Override
    public void martensiteNeo$removeSafeChunk(String name) {
        int[] value;
        if((value = this.martensiteNeo$ChunksList.get(name))!=null) {
            this.martensiteNeo$ChunksList.remove(name);
            this.martensiteNeo$safeChunks.remove(this.getChunk(value[0], value[1]));
        }
    }

    @Override
    public void martensiteNeo$refreshChunks() {
        for(LevelChunk chunk : this.martensiteNeo$safeChunks){
            ((chunkData) chunk).martensiteNeo$setSafeChunk(true);
        }
    }
}
