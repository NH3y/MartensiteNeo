
package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.nmEntityCache;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;
import java.util.stream.Collectors;

// 高效率的尋找最近玩家方法
public class ClaudePlayerDetect {
    public static double execute(LevelAccessor world, Entity entity) {
        if (entity instanceof nmEntityCache) {
            BlockPos entityPos = entity.blockPosition();
            Player player = ((nmEntityCache) entity).martensiteNew$getCacheNearestPlayer();

            // 2. 利用緩存
            // 只每5刻更新一次最近玩家，或首次計算時
            if ((world.getLevelData().getGameTime() + entity.getId()) % 5 == 0 || player == null) {
                double closestDistSq = 16384;
                Player closestPlayer = null;
                int index = -1;

                // 4. 快速過濾 - 如果玩家在不同維度則跳過
                List<Player> qualify = world.players().stream().filter(eachPlayer -> (eachPlayer.level() == entity.level() && !eachPlayer.isSpectator())).collect(Collectors.toUnmodifiableList());
                List<BlockPos> blockDistance = qualify.stream().map(q -> (q.blockPosition().offset(entityPos.multiply(-1)))).toList();
                for (BlockPos pos : blockDistance) {
                    index++;
                    // 5. 使用曼哈頓距離進行初步篩選
                    // 曼哈頓距離計算比歐幾里得距離更快

                    int manhattanDist =
                            Math.abs(pos.getX()) +
                            Math.abs(pos.getY()) +
                            Math.abs(pos.getZ());

                    // 6. 如果曼哈頓距離已經大於目前最近距離，則跳過
                    if (manhattanDist * manhattanDist > (closestDistSq * 3)) continue;

                    // 7. 對還剩下的可能性進行精確計算
                    double distSq = length(pos);
                    if (distSq < closestDistSq) {
                        closestDistSq = distSq;
                        closestPlayer = qualify.get(index);
                    }
                }
                // 儲存計算結果到緩存
                ((nmEntityCache) entity).martensiteNew$setCacheNearestPlayer(closestPlayer, world.getLevelData().getGameTime(), Math.sqrt(closestDistSq));
            }
        }
        return ((nmEntityCache) entity).martensiteNew$getCacheNearestPlayerDisSq();
    }

    private static double length(Vec3i vec3i) {
        return vec3i.getX() * vec3i.getX() + vec3i.getY() * vec3i.getY() + vec3i.getZ() * vec3i.getZ();
    }
}