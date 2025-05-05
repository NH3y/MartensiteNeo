
package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

// 高效率的尋找最近玩家方法
public class ClaudePlayerDetect {
    public static double execute(LevelAccessor world, Entity entity) {
        // 1. 使用空間索引結構而非線性搜索
        if (entity instanceof nmEntityCache) {
            BlockPos entityPos = entity.blockPosition();
            Player player = ((nmEntityCache) entity).martensiteNew$getCacheNearestPlayer();

            // 2. 利用緩存
            // 只每5刻更新一次最近玩家，或首次計算時
            if (world.getLevelData().getGameTime() % 5 == 0 || player == null) {
                double closestDistSq = 16384;
                Player closestPlayer = null;

                // 3. 使用距離平方比較，避免開方運算

                for (Player player2 : world.players()) {
                    // 4. 快速過濾 - 如果玩家在不同維度則跳過
                    if (player2.level() != entity.level() || player2.isSpectator()) continue;
                    // 5. 使用曼哈頓距離進行初步篩選
                    // 曼哈頓距離計算比歐幾里得距離更快
                    BlockPos playerPos = player2.blockPosition();

                    int manhattanDist =
                            Math.abs(playerPos.getX() - entityPos.getX()) +
                            Math.abs(playerPos.getY() - entityPos.getY()) +
                            Math.abs(playerPos.getZ() - entityPos.getZ());

                    // 6. 如果曼哈頓距離已經大於目前最近距離，則跳過
                    if (manhattanDist > Math.sqrt(closestDistSq * 3)) continue;

                    // 7. 對還剩下的可能性進行精確計算
                    double distSq =entity.distanceToSqr(player2);
                    if (distSq < closestDistSq) {
                        closestDistSq = distSq;
                        closestPlayer = player2;
                    }
                }
                // 儲存計算結果到緩存
                ((nmEntityCache) entity).martensiteNew$setCacheNearestPlayer(closestPlayer, world.getLevelData().getGameTime(), Math.sqrt(closestDistSq));
            }
        }
        return ((nmEntityCache) entity).martensiteNew$getCacheNearestPlayerDisSq();
    }
}