package com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces;

import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface levelData {
    void martensiteNeo$setSafeChunks(Map<String,int[]> input);

    int[] martensiteNeo$addSafeChunk(String name, int[] value);

    void martensiteNeo$removeSafeChunk(String name);

    void martensiteNeo$refreshChunks();

    default Map<String,int[]> martensiteNeo$getSafeChunks(){return new HashMap<>();}

    default Set<LevelChunk> martensiteNeo$getChunks(){return new HashSet<>();}
}
