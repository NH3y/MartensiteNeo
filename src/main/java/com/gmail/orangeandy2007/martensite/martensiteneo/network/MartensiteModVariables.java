package com.gmail.orangeandy2007.martensite.martensiteneo.network;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.chunkData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@EventBusSubscriber
public class MartensiteModVariables {
	public static Map<String,int[]> ChunkList = new HashMap<>();
	public static int INDEX = 0;
	public static boolean SEARCH_LOGIC = false;
	public static int FILE_INDEX = 1;
    private static String ChunkDataCache = null;

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) throws IOException {
        if(event.getEntity().level().isClientSide()) return;
		String world = event.getEntity().level().getLevelData() instanceof ServerLevelData _leveldata ?_leveldata.getLevelName(): "";
		String map = event.getEntity().level().dimension().toString().replace("ResourceKey", " ").replace("minecraft","*");
		read(world + map, event.getEntity().level());
	}
	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) throws IOException {
        if(event.getEntity().level().isClientSide()) return;
        String world = event.getEntity().level().getLevelData() instanceof ServerLevelData _leveldata ?_leveldata.getLevelName(): "";
		String map = event.getFrom().toString().replace("ResourceKey", " ").replace("minecraft","*");
        String map2 = event.getTo().toString().replace("ResourceKey", " ").replace("minecraft","*");
        save(world + map);
        read(world + map2, event.getEntity().level());
	}
	@SubscribeEvent
	public static void onPlayerLogout(PlayerLoggedOutEvent event) throws IOException {
        if(event.getEntity().level().isClientSide()) return;
        String world = event.getEntity().level().getLevelData() instanceof ServerLevelData _leveldata ?_leveldata.getLevelName(): "";
		String map = event.getEntity().level().dimension().toString().replace("ResourceKey", " ").replace("minecraft","*");
		save(world + map);
	}

	public static void read(String name, Level level) throws IOException {
        File data = new File("ChunkList.data");
        if (!(data.exists() && data.canRead() && data.length() != 0)) return;
        FileReader ChunkReader = new FileReader(data);
        BufferedReader ChunkBuffer = new BufferedReader(ChunkReader);
        String line;
        FILE_INDEX = 0;
        Iterator<String> CheckLine = ChunkBuffer.lines().iterator();
        boolean encounter = false;
        while (CheckLine.hasNext()) {
            System.out.println("Read!");
            line = CheckLine.next();
            FILE_INDEX++;
            if (!line.contains("?")) continue;
            if (line.substring(0, line.lastIndexOf("?")).equals(name)) {
                encounter = true;
                System.out.println("This One!");
                ChunkList.clear();
                String[] mapData = line.substring(line.lastIndexOf("?") + 2, line.length() - 1).split("],");
                for (String entry : mapData) {
                    if (entry.isEmpty()) break;
                    int value1 = Integer.parseInt(entry.split("=")[1].replace("[", "").replace("]","").split(",")[0].trim());
                    int value2 = Integer.parseInt(entry.split("=")[1].replace("[", "").replace("]", "").split(",")[1].trim());
                    ChunkList.putIfAbsent(entry.split("=")[0].trim(), new int[]{value1, value2});
                    ((chunkData)level.getChunk(value1,value2)).martensiteNeo$setSafeChunk(true);
                }
                update();
                break;
            }
        }
        if (!encounter) FILE_INDEX++;
        ChunkBuffer.close();
        System.out.println("index: " + FILE_INDEX);
    }
	public static void save(String name) throws IOException {
        File data = new File("ChunkList.data");
        ArrayList<String> preString = null;
        if (!data.exists() || data.length()!=0) {
            FileReader subReader = new FileReader(data);
            BufferedReader subBuffer = new BufferedReader(subReader);
            preString = subBuffer.lines().distinct().collect(Collectors.toCollection(ArrayList::new));
            subBuffer.close();
        }

        FileWriter ChunkWriter = new FileWriter(data);
        BufferedWriter ChunkBuffer = new BufferedWriter(ChunkWriter);
        if(preString == null){
            ChunkBuffer.write(name + "?" + ChunkData());
        }else{
            String Current = name + "?" + ChunkData();
            INDEX = 1;
            for(String line : preString){
                if(INDEX == FILE_INDEX){
                    ChunkBuffer.write(Current);
                }else{
                    ChunkBuffer.write(line);
                }
                INDEX++;
                ChunkBuffer.newLine();
            }
            if(INDEX == FILE_INDEX){
                ChunkBuffer.write(Current);
            }
        }
        ChunkBuffer.close();
    }

    public static String ChunkData(){
        if(ChunkDataCache == null) {
            ArrayList<String> output = new ArrayList<>();
            for (Map.Entry<String, int[]> entry : ChunkList.entrySet()) {
                output.add(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
            }
            return output.toString();
        }
        return ChunkDataCache;
    }
    public static void update(){
        ChunkDataCache = null;
        ChunkDataCache = ChunkData();
    }
}