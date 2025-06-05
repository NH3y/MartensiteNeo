package com.gmail.orangeandy2007.martensite.martensiteneo.mixin.manager;

import com.gmail.orangeandy2007.martensite.martensiteneo.classes.PlayerDistanceManager;
import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.chunkData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.levelData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.NotNull;
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
    private Set<LevelChunk> martensiteNeo$safeChunks = new HashSet<>();

    @Unique
    private Map<String,int[]> martensiteNeo$ChunksList = new HashMap<>();

    @Unique
    private Map<Player, BlockPos> martensiteNeo$PositionMap = new HashMap<>();

    @Unique
    public PlayerDistanceManager martensiteNeo$distanceManager = new PlayerDistanceManager(ProtectZoneConfiguration.RANDOM_LOOP.get(), (LevelAccessor) this);
    @Override
    public void martensiteNeo$setSafeChunks(@NotNull Map<String, int[]> input) {
        if (this.martensiteNeo$safeChunks == null || this.martensiteNeo$safeChunks.isEmpty()) {
            return;
        }
        this.martensiteNeo$safeChunks.clear();
        this.martensiteNeo$ChunksList.clear();

        for(Map.Entry<String,int[]> entry : input.entrySet()){
            LevelChunk chunk = this.getChunk(entry.getValue()[0], entry.getValue()[1]);

            ((chunkData) chunk).martensiteNeo$setSafeChunk(true);
            this.martensiteNeo$safeChunks.add(chunk);
        }
        this.martensiteNeo$ChunksList.putAll(input);
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
        return this.martensiteNeo$ChunksList == null ? new HashMap<>() : this.martensiteNeo$ChunksList;
    }

    @Override
    public Set<LevelChunk> martensiteNeo$getChunks() {
        return this.martensiteNeo$safeChunks == null ? new HashSet<>() : this.martensiteNeo$safeChunks;
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
        for(LevelChunk chunk : this.martensiteNeo$getChunks()){
            ((chunkData) chunk).martensiteNeo$setSafeChunk(true);
        }
    }

    @Override
    public void martensiteNeo$refreshPosMap() {
        if (this.martensiteNeo$PositionMap == null || this.martensiteNeo$PositionMap.isEmpty()) {
            return;
        }
        this.martensiteNeo$PositionMap.clear();
        if((Object)this instanceof LevelAccessor level){
            for(Player player : level.players().stream().filter(p -> (!p.isSpectator())).toList()){
                this.martensiteNeo$PositionMap.put(player, player.blockPosition());
            }
        }
    }

    @Override
    public Set<Map.Entry<Player, BlockPos>> martensiteNeo$getPosMap() {
        return this.martensiteNeo$PositionMap == null ? new HashSet<>() : this.martensiteNeo$PositionMap.entrySet();
    }

    @Override
    public PlayerDistanceManager martensiteNeo$getDistanceManager() {
        return this.martensiteNeo$distanceManager;
    }
}
