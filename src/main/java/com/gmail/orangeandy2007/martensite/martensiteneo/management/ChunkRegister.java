package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.gmail.orangeandy2007.martensite.martensiteneo.network.MartensiteModVariables.ChunkList;
import static com.gmail.orangeandy2007.martensite.martensiteneo.network.MartensiteModVariables.update;

public class ChunkRegister {
    public static void addChunk(LevelChunk chunk, String name, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        addChunk(value, name, entity);
    }
    public static void addChunk(Entity entity, String name){
        int[] value = new int[]{entity.chunkPosition().x,entity.chunkPosition().z};
        addChunk(value, name, entity);
    }
    public static void addChunk(int[] value, String name, Entity entity){
        if(ChunkList.containsValue(value) || ChunkList.containsKey(name)) return;
        ChunkList.putIfAbsent(name, value);
        messageMaker(entity, "Add Chunk!", true);
        messageMaker(entity,name,false);
        ((chunkData) entity.level().getChunk(value[0], value[1])).martensiteNeo$setSafeChunk(true);
        update();
    }
    public static void remove(LevelChunk chunk, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRemove = new ArrayList<>();
        System.out.println(ChunkList.containsValue(value));
        for(Map.Entry<String, int[]> entry : ChunkList.entrySet()){
            if(Arrays.equals(entry.getValue(), value)) {
                toRemove.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if(toRemove.isEmpty()) return;
        for(String key : toRemove){
            ChunkList.remove(key);
            messageMaker(entity, "Remove: " + key , true);
        }
        ((chunkData) chunk).martensiteNeo$setSafeChunk(false);
        update();
    }
    public static void rename(LevelChunk chunk, String name, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRename = new ArrayList<>();
        for(Map.Entry<String, int[]> entry : ChunkList.entrySet()){
            if(Arrays.equals(entry.getValue(), value)){
                toRename.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if(toRename.isEmpty()) return;
        for(String key : toRename){
            ChunkList.remove(key);
            ChunkList.putIfAbsent(name, value);
        }
        update();
    }
    public static void clear(Entity entity){
        if(entity instanceof Player player && player.hasPermissions(4)){
            System.out.println(player.getName() + "Clear The ChunkList!!!");
            System.out.println("UUID: " + player.getUUID());
            for(int[] value : ChunkList.values()){
                ((chunkData)entity.level().getChunk(value[0],value[1])).martensiteNeo$setSafeChunk(false);
            }
            messageMaker(entity, "Clear!", true);
            ChunkList.clear();
        }

        update();
    }
    public static void search(Entity entity, String key){
        boolean encounter = false;
        for(Map.Entry<String,int[]> entry : ChunkList.entrySet()){
            if(entry.getKey().contains(key)){
                messageMaker(entity, entry.getKey(), false);
                encounter = true;
            }
        }
        if(!encounter) messageMaker(entity,"Not Found!", true);
    }
    public static void get(LevelChunk chunk, Entity entity){
        if(chunk instanceof chunkData AdChunk && AdChunk.martensiteNeo$getSafeChunk()){
            int[] value = new int[]{chunk.getPos().x,chunk.getPos().z};
            messageMaker(entity, "Found Register At : " + Arrays.toString(value), true);
        }else{
            messageMaker(entity, "Not Found", true);
        }
    }
    public static void messageMaker(Entity entity, String key, boolean literal){
        if(entity instanceof Player player) {
            if(!literal){
                player.displayClientMessage(Component.literal(key + " : " + Arrays.toString(ChunkList.get(key))),false);
            }else{
                player.displayClientMessage(Component.literal(key),false);
            }
        }
    }
}
