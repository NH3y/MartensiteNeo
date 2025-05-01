package com.gmail.orangeandy2007.martensite.martensiteneo.network;

import com.gmail.orangeandy2007.martensite.martensiteneo.feature.EventUnload;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.levelData;
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
import java.util.*;
import org.slf4j.Logger;
import java.util.stream.Stream;

@EventBusSubscriber
public class MartensiteModVariables {
    private static final Logger logger = LogUtils.getLogger();

    @SubscribeEvent
    public static void onLevelLoad(Load event) throws IOException {
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
    public static void onLevelUnload(Unload event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        logger.info("Unloading");

        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData){
            save(data.getLevelName() + level.dimensionType().effectsLocation().getPath(),((levelData) level).martensiteNeo$getSafeChunks());
        }
    }
    @SubscribeEvent
    public static void onLevelSave(Save event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        logger.info("Saving");
        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData) {
            save(data.getLevelName() + level.dimensionType().effectsLocation().getPath(), ((levelData) level).martensiteNeo$getSafeChunks());
        }
    }
	public static Map<String,int[]> read(String name) throws IOException {
        Map<String,int[]> map = new HashMap<>();
        File file = new File("ChunkList.data");
        if(!file.exists() || !file.canRead() || file.length() == 0) return map;
        FileReader reader = new FileReader(file);

        List<String> qualify;
        try (BufferedReader BReader = new BufferedReader(reader)){
            Stream<String> lines = BReader.lines()
                    .filter(line -> line.contains("?"))
                    .filter(line -> line.contains(name));
            qualify = new ArrayList<>(lines.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(qualify.isEmpty()) return map;
        for(String data : qualify){
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
        File file = new File("ChunkList.data");
        List<String> lines = null;

        if(file.exists()&&file.canRead()&&file.length() != 0) {
            FileReader fileReader = new FileReader(file);
            try (BufferedReader BReader = new BufferedReader(fileReader)) {
                lines = new ArrayList<>(BReader.lines().filter(line -> line.contains("?")).toList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter BWriter = new BufferedWriter(fileWriter);
        boolean encounter = false;
        ArrayList<String> toPrint = new ArrayList<>();
        for(Map.Entry<String,int[]> entry :  data.entrySet()){
            toPrint.add(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
        }
        if (lines != null) {
            for(String line : lines){
                if(line.contains(name)){
                    if(!encounter) {
                        encounter = true;
                        sectionData(BWriter,name,toPrint);
                    }
                }else{
                    BWriter.write(line);
                }
                BWriter.newLine();
            }
            if(!encounter){
                sectionData(BWriter, name, toPrint);
            }
        }else{
            sectionData(BWriter,name,toPrint);
        }
        BWriter.flush();
    }
    public static void sectionData(BufferedWriter BWriter, String name, List<String> toPrint) throws IOException {
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
            }
        }
    }
    private static Map.Entry<String,int[]> dataFormat(String line){
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