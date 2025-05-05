package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import com.gmail.orangeandy2007.martensite.martensiteneo.classes.MessageCache;
import com.gmail.orangeandy2007.martensite.martensiteneo.feature.MessageTick;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChunkRegister {
    public static void addChunk(Level world, LevelChunk chunk, String name, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        addChunk(world, value, name, entity);
    }
    public static void addChunk(Level world, Entity entity, String name){
        int[] value = new int[]{entity.chunkPosition().x,entity.chunkPosition().z};
        addChunk(world, value, name, entity);
    }
    public static void addChunk(Level world,int[] value, String name, Entity entity){

        int[] check = ((levelData)world).martensiteNeo$addSafeChunk(name, value);
        if (check != null && !Arrays.toString(check).equals(Arrays.toString(value))) {
            System.out.println("Fail");
            messageMaker(entity, "Chunk Nane Already Exist At", true);
            messageMaker(entity,name,false);
            return;
        }
        System.out.println("Success");
        messageMaker(entity, "Add Chunk!", true);
        messageMaker(entity,name,false);
        ((chunkData) entity.level().getChunk(value[0], value[1])).martensiteNeo$setSafeChunk(true);
    }
    public static void remove(Level world, LevelChunk chunk, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRemove = new ArrayList<>();
        for(Map.Entry<String, int[]> entry : ((levelData)world).martensiteNeo$getSafeChunks().entrySet()){
            if(Arrays.equals(entry.getValue(), value)) {
                toRemove.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if(toRemove.isEmpty()) {
            messageMaker(entity, "Not Found", true);
            return;
        }
        for(String key : toRemove){
            ((levelData) world).martensiteNeo$removeSafeChunk(key);
            messageMaker(entity, "Remove: " + key , true);
        }
        ((chunkData) chunk).martensiteNeo$setSafeChunk(false);
    }
    public static void rename(Level world, LevelChunk chunk, String name, Entity entity){
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRename = new ArrayList<>();
        for(Map.Entry<String, int[]> entry : ((levelData)world).martensiteNeo$getSafeChunks().entrySet()){
            if(Arrays.equals(entry.getValue(), value)){
                toRename.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if(toRename.isEmpty()) {
            messageMaker(entity,"Not Found!", true);
            return;
        }
        if(toRename.size() != 1){
            messageMaker(entity, "Multiple Chunks were Found", true);
            return;
        }
        for(String key : toRename){
            ((levelData) world).martensiteNeo$removeSafeChunk(key);
            ((levelData) world).martensiteNeo$addSafeChunk(name, value);
            messageMaker(entity,key,false);
        }
    }
    public static void clear(Level world,Entity entity){
        if(entity instanceof Player player && player.hasPermissions(4)){
            System.out.println(player.getName() + " Clear The ChunkList!!!");
            System.out.println("UUID: " + player.getUUID());
            for(int[] value : ((levelData)world).martensiteNeo$getSafeChunks().values()){
                ((chunkData)entity.level().getChunk(value[0],value[1])).martensiteNeo$setSafeChunk(false);
            }
            messageMaker(entity, "Clear!", true);
            ((levelData) world).martensiteNeo$setSafeChunks(new HashMap<>());
        }
    }
    public static void search(Level world, Entity entity, String include) {
        ArrayList<String> toSearch = new ArrayList<>();
        for(String key : ((levelData)world).martensiteNeo$getSafeChunks().keySet()){
            if(key.contains(include)){
                toSearch.add(key +" = "+ Arrays.toString(((levelData) world).martensiteNeo$getSafeChunks().get(key)));
            }
        }
        if(entity instanceof Player player) {
            toSearch.add("Finish! " + toSearch.size() + " Chunks in total!");
            if(MessageTick.running){
                messageMaker(player,"Searching System is Busy",true);
                messageMaker(player,"Please Wait",true);
            }
            MessageTick.sendMessage(toSearch, player);
        }
    }
    public static void get(LevelChunk chunk, Entity entity){
        if(chunk instanceof chunkData AdChunk && AdChunk.martensiteNeo$getSafeChunk()){
            int[] value = new int[]{chunk.getPos().x,chunk.getPos().z};
            messageMaker(entity, "Found Register At : " + Arrays.toString(value), true);
        }else{
            messageMaker(entity, "Not Found or Not Loaded", true);
        }
    }
    public static void messageMaker(Entity entity, String key, boolean literal){
        if(entity instanceof Player player) {
            if(!literal){
                player.displayClientMessage(Component.literal(key + " : " + Arrays.toString(((levelData)entity.level()).martensiteNeo$getSafeChunks().get(key))),false);
            }else{
                player.displayClientMessage(Component.literal(key),false);
            }
        }
    }
}
