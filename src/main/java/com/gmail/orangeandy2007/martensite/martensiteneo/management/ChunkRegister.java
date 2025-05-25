package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import com.gmail.orangeandy2007.martensite.martensiteneo.feature.MessageTick;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.chunkData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.levelData;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;

public class ChunkRegister {
    private static final Set<Player> pendingPlayer = new HashSet<>();
    private static final Logger logger = LogUtils.getLogger();

    public static void addChunk(Level world, @NotNull LevelChunk chunk, String name, Entity entity) {
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        addChunk(world, value, name, entity);
    }

    public static void addChunk(Level world, @NotNull Entity entity, String name) {
        int[] value = new int[]{entity.chunkPosition().x, entity.chunkPosition().z};
        addChunk(world, value, name, entity);
    }

    public static void addChunk(Level world, int[] value, String name, Entity entity) {

        int[] check = ((levelData) world).martensiteNeo$addSafeChunk(name, value);
        if (check != null && !Arrays.toString(check).equals(Arrays.toString(value))) {
            logger.warn("Chunk Register Fail (repeat_chunk)");
            messageMaker(entity, "Chunk Nane Already Exist At", true);
            messageMaker(entity, name, false);
            return;
        }
        logger.info("Chunk Register Success");
        messageMaker(entity, "Add Chunk!", true);
        messageMaker(entity, name, false);
        ((chunkData) entity.level().getChunk(value[0], value[1])).martensiteNeo$setSafeChunk(true);
    }

    public static void remove(Level world, @NotNull LevelChunk chunk, Entity entity) {
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : ((levelData) world).martensiteNeo$getSafeChunks().entrySet()) {
            if (Arrays.equals(entry.getValue(), value)) {
                toRemove.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if (toRemove.isEmpty()) {
            messageMaker(entity, "Not Found", true);
            return;
        }
        for (String key : toRemove) {
            ((levelData) world).martensiteNeo$removeSafeChunk(key);
            messageMaker(entity, "Remove: " + key, true);
        }
        ((chunkData) chunk).martensiteNeo$setSafeChunk(false);
    }

    public static void rename(Level world, @NotNull LevelChunk chunk, String name, Entity entity) {
        int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
        ArrayList<String> toRename = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : ((levelData) world).martensiteNeo$getSafeChunks().entrySet()) {
            if (Arrays.equals(entry.getValue(), value)) {
                toRename.add(entry.getKey());
                messageMaker(entity, "Found: " + entry.getKey(), true);
            }
        }
        if (toRename.isEmpty()) {
            messageMaker(entity, "Not Found!", true);
            return;
        }
        if (toRename.size() != 1) {
            messageMaker(entity, "Multiple Chunks were Found", true);
            return;
        }
        for (String key : toRename) {
            ((levelData) world).martensiteNeo$removeSafeChunk(key);
            ((levelData) world).martensiteNeo$addSafeChunk(name, value);
            messageMaker(entity, key, false);
        }
    }

    public static void clear(Level world, Entity entity) {
        if (entity instanceof Player player && player.hasPermissions(4)) {
            logger.warn("{} Clear The ChunkList!!!", player.getName());
            logger.warn("UUID: {}", player.getUUID());
            for (int[] value : ((levelData) world).martensiteNeo$getSafeChunks().values()) {
                ((chunkData) entity.level().getChunk(value[0], value[1])).martensiteNeo$setSafeChunk(false);
            }
            messageMaker(entity, "Clear!", true);
            ((levelData) world).martensiteNeo$setSafeChunks(new HashMap<>());
        }
    }

    public static void search(Level world, Entity entity, @NotNull String include) {
        if (include.equals("_") && !pendingPlayer.contains((Player) entity)) {
            messageMaker(entity, "Searching \"_\" would contain all the enumerated chunk", true);
            messageMaker(entity, "Try again to confirmed the search", true);
            pendingPlayer.add((Player) entity);
            return;
        }
        pendingPlayer.remove(((Player) entity));

        ArrayList<String> toSearch = new ArrayList<>();
        for (String key : ((levelData) world).martensiteNeo$getSafeChunks().keySet()) {
            if (key.contains(include)) {
                toSearch.add(key + " = " + Arrays.toString(((levelData) world).martensiteNeo$getSafeChunks().get(key)));
            }
        }
        if (entity instanceof Player player) {
            toSearch.add("Finish! " + toSearch.size() + " Chunks in total!");
            if (MessageTick.running) {
                messageMaker(player, "Searching System is Busy", true);
                messageMaker(player, "Please Wait", true);
            }
            MessageTick.sendMessage(toSearch, player);
        }
    }

    public static void get(LevelChunk chunk, Entity entity) {
        if (chunk instanceof chunkData AdChunk && AdChunk.martensiteNeo$getSafeChunk()) {
            int[] value = new int[]{chunk.getPos().x, chunk.getPos().z};
            messageMaker(entity, "Found Register At : " + Arrays.toString(value), true);
        } else {
            messageMaker(entity, "Not Found or Not Loaded", true);
        }
    }

    public static void messageMaker(Entity entity, String key, boolean literal) {
        if (entity instanceof Player player) {
            if (!literal) {
                player.displayClientMessage(Component.literal(key + " : " + Arrays.toString(((levelData) entity.level()).martensiteNeo$getSafeChunks().get(key))), false);
            } else {
                player.displayClientMessage(Component.literal(key), false);
            }
        }
    }
}
