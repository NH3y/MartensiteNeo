package com.gmail.orangeandy2007.martensite.martensiteneo.network;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.levelData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent.Load;
import net.neoforged.neoforge.event.level.LevelEvent.Unload;
import net.neoforged.neoforge.event.level.LevelEvent.Save;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

@EventBusSubscriber
public class MartensiteModVariables {
    @SubscribeEvent
    public static void onLevelLoad(Load event) throws IOException {
        if(event.getLevel().isClientSide()){return;}
        System.out.println("Load");

        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData) {
            ((levelData) level).martensiteNeo$setSafeChunks(read(data.getLevelName() + level.dimensionType().effectsLocation().getPath()));
            ((levelData) level).martensiteNeo$refreshChunks();
        }
    }
    @SubscribeEvent
    public static void onLevelUnload(Unload event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        System.out.println("Unload");

        LevelAccessor level = event.getLevel();
        if(level.getLevelData() instanceof ServerLevelData data && level instanceof levelData){
            save(data.getLevelName() + level.dimensionType().effectsLocation().getPath(),((levelData) level).martensiteNeo$getSafeChunks());
        }
    }
    @SubscribeEvent
    public static void onLevelSave(Save event) throws IOException {
        if(event.getLevel().isClientSide()){return;}

        System.out.println("Save");
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
            String[] Pos;
            String set;
            for(String each : dataSet){
                if(!each.contains("=")) continue;
                Pos = each.split("=")[1].split(",");
                if(Pos.length == 0) continue;
                int[] Value = new int[]{
                        Integer.parseInt(Pos[0].replace("[","").trim()),
                        Integer.parseInt(Pos[1].replace("]","").trim())
                };
                set = each.split("=")[0].replace("[","");
                map.put(set,Value);
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
        }else{
            sectionData(BWriter,name,toPrint);
        }
        if(!encounter){
            sectionData(BWriter, name, toPrint);
        }
        BWriter.flush();
    }
    public static void sectionData(BufferedWriter BWriter, String name, ArrayList<String> toPrint) throws IOException {
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
}