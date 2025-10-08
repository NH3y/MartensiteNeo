package com.gmail.orangeandy2007.martensite.martensiteneo.network;

import com.gmail.orangeandy2007.martensite.martensiteneo.feature.EventUnload;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.levelData;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent.Load;
import net.neoforged.neoforge.event.level.LevelEvent.Unload;
import net.neoforged.neoforge.event.level.LevelEvent.Save;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@EventBusSubscriber
public class MartensiteModVariables {
    private static final Logger logger = LogUtils.getLogger();
    private static final ArrayList<String> lines = new ArrayList<>();
    public static long lastReadTime = 0;

    @SubscribeEvent
    public static void onLevelLoad(@NotNull Load event) {
        if(event.getLevel().isClientSide()){return;}

        logger.info("Loading");
        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData levelData) {
            levelData.martensiteNeo$setSafeChunks(read(data.getLevelName() + level.dimensionType().effectsLocation().getPath()));
            levelData.martensiteNeo$refreshChunks();
        }
        EventUnload.CloseDetect();
    }
    @SubscribeEvent
    public static void onLevelUnload(@NotNull Unload event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        logger.info("Unloading");
        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData levelData){
            save(data.getLevelName() + level.dimensionType().effectsLocation().getPath(),levelData.martensiteNeo$getSafeChunks());
        }
    }
    @SubscribeEvent
    public static void onLevelSave(@NotNull Save event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        logger.info("Saving");
        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData) {
            save(data.getLevelName() + level.dimensionType().effectsLocation().getPath(), ((levelData) level).martensiteNeo$getSafeChunks());
        }

        lastReadTime = 0;
    }

    private static void readAndStore() throws FileNotFoundException {
        File file = new File("ChunkList.data");
        if(!file.exists() || !file.canRead() || file.length() == 0) return;
        FileReader reader = new FileReader(file);

        List<String> allLine;
        try (BufferedReader BReader = new BufferedReader(reader)){
            Stream<String> lines = BReader.lines()
                    .filter(line -> line.contains("?"));
            allLine = new ArrayList<>(lines.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(allLine.isEmpty()) return;
        lines.clear();
        lines.addAll(allLine);
        lastReadTime = System.currentTimeMillis();
    }

	public static Map<String,int[]> read(String name) {
        Map<String,int[]> map = new HashMap<>();
        long time = System.currentTimeMillis();
        if (time - lastReadTime > 300000 || lastReadTime == 0) {
            try {
                readAndStore();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (lines.isEmpty()) return map;
        Set<String> qualified = lines.stream().filter(line -> line.contains(name)).collect(Collectors.toSet());
        for(String data : qualified){
            int gap = data.length() - data.lastIndexOf("?") - 4;
            if (gap <= 0) {
                continue;
            }
            String[] dataSet = data.substring(data.lastIndexOf("?")+2,data.length()-2).split(">");
            if(dataSet.length == 0){
                return map;
            }
            for(String each : dataSet){
                Map.Entry<String,int[]> entry = dataFormat(each);
                if(entry == null) continue;
                map.put(entry.getKey(),entry.getValue());
            }
        }
        return map;
    }
    public static void save(String name, Map<String,int[]> data) throws IOException {
        if (data == null) {
            return;
        }
        File file = new File("ChunkList.data");
        List<String> lines = null;

        if(file.exists() && file.canRead() && file.length() != 0) {
            FileReader fileReader = new FileReader(file);
            try (BufferedReader BReader = new BufferedReader(fileReader)) {
                lines = new ArrayList<>(BReader.lines().filter(line -> line.contains("?")).toList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter BWriter = new BufferedWriter(fileWriter);
        ArrayList<String> toPrint = new ArrayList<>();
        for(Map.Entry<String,int[]> entry :  data.entrySet()){
            toPrint.add(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
        }
        if (lines == null) {
            sectionData(BWriter, name, toPrint);
            BWriter.flush();
            return;
        }
        if(lines.stream().noneMatch(line -> line.contains(name))){
            for (String line : lines) {
                BWriter.write(line);
                BWriter.newLine();
            }
            sectionData(BWriter, name, toPrint);
            BWriter.flush();
            return;
        }
        boolean encounter = false;
        for (String line : lines) {
            if (line.contains(name)) {
                if (!encounter) {
                    encounter = true;
                    sectionData(BWriter, name, toPrint);
                }
            } else {
                BWriter.write(line);
            }
            BWriter.newLine();
        }
        BWriter.flush();
    }
    public static void sectionData(@NotNull BufferedWriter BWriter, String name, @NotNull List<String> toPrint) throws IOException {
        int times = 0;
        BWriter.write(name + "? [");
        if(toPrint.isEmpty()){
            BWriter.write("]");
        }
        for (String set : toPrint) {
            times++;
            BWriter.write(set+">");
            if (times >= 100) {
                times = 0;
                BWriter.write("]");
                BWriter.newLine();
                BWriter.write(name + "? [");
                if (toPrint.size() % 100 == 0) {
                    BWriter.write("]");
                }
            }
        }
    }
    private static Map.@Nullable @Unmodifiable Entry<String,int[]> dataFormat(@NotNull String line){
        if(!line.contains("=")) return null;
        @NotNull String[] Pos = line.split("=")[1].split(",");
        if(Pos.length == 0) return null;
        int[] value = new int[]{
                Integer.parseInt(Pos[0].replace("[","").trim()),
                Integer.parseInt(Pos[1].replace("]","").trim())
        };
        String name = line.split("=")[0].replace("[","");
        return Map.entry(name,value);
    }
}