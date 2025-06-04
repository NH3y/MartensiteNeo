package com.gmail.orangeandy2007.martensite.martensiteneo.classes;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.levelData;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.nmEntityCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerDistanceManager {
    Set<Map.Entry<Player, BlockPos>> PosMap = null;
    int searchCount;
    LevelAccessor world;

    public PlayerDistanceManager(int count, LevelAccessor level){
        this.searchCount = count;
        this.world = level;
    }

    private int manhattanDistSqr(BlockPos pos){
        int base = Math.abs(pos.getX())
                + Math.abs(pos.getY())
                + Math.abs(pos.getZ());
        return base * base;
    }

    private int lengthSqr(BlockPos pos){
        return lengthSqr(pos.getX(), pos.getY(), pos.getZ());
    }

    private int lengthSqr(int x, int y, int z){
        return x*x + y*y + z*z;
    }

    public double find(Entity entity) {
        if (world instanceof levelData data) {
            if (data.martensiteNeo$getPosMap() == null) return 128;
            Vec3i vec3i = entity.blockPosition();
            this.PosMap = data.martensiteNeo$getPosMap().stream()
                    .map(entry -> (Map.entry(entry.getKey(), entry.getValue().offset(vec3i.multiply(-1))))).collect(Collectors.toSet());

            if (this.PosMap.isEmpty()) return 128;
            if (this.PosMap.size() == 1) {
                Optional<Map.Entry<Player, BlockPos>> result = this.PosMap.stream().findFirst();
                double distSqr = result.map(entry -> Math.sqrt(lengthSqr(entry.getValue()))).orElse(128.0);
                Player player = result.map(Map.Entry::getKey).orElse(null);
                ((nmEntityCache) entity).martensiteNew$setCacheNearestPlayer(player, 0, Math.min(Math.sqrt(distSqr), 128));
                return Math.min(distSqr, 128);
            }


            int distSqr = 16384;
            for (int i = 0; i < searchCount; i++) {
                distSqr = run(distSqr);
                if(this.PosMap.size() == 1) {
                    break;
                }
            }
            Optional<Map.Entry<Player, BlockPos>> result = this.PosMap.stream().findAny();
            if (result.isEmpty()) return 128;
            Player player = result.get().getKey();
            distSqr = lengthSqr(result.get().getValue());
            ((nmEntityCache) entity).martensiteNew$setCacheNearestPlayer(player, 0, Math.sqrt(distSqr));
            return Math.sqrt(distSqr);
        }
        return 128;
    }

    private int run(int dist){
        Set<Map.Entry<Player, BlockPos>> filtered = this.PosMap.stream().filter(entry -> manhattanDistSqr(entry.getValue()) / dist <= 3).collect(Collectors.toSet());
        this.PosMap.clear();
        this.PosMap.addAll(filtered);
        Optional<Map.Entry<Player, BlockPos>> random = filtered.stream().findAny();
        return random.map(playerBlockPosEntry -> lengthSqr(playerBlockPosEntry.getValue())).orElse(dist);
    }
}
