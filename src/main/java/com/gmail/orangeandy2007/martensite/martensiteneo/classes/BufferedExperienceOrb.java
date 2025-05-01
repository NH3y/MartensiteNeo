package com.gmail.orangeandy2007.martensite.martensiteneo.classes;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BufferedExperienceOrb extends ExperienceOrb {
    private int count;
    private int buffer;
    public BufferedExperienceOrb(Level level, double x, double y, double z, int value, int count, int buffer) {
        super(level, x, y, z, value);
        this.count = count;
        this.buffer = buffer;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount % 20 == 1){
            this.fusion();
        }
    }

    @Override
    public void playerTouch(@NotNull Player entity) {
        if (entity instanceof ServerPlayer serverplayer) {
            if (NeoForge.EVENT_BUS.post(new PlayerXpEvent.PickupXp(entity, this)).isCanceled()) {
                return;
            }

            entity.take(this, 1);
            int i = this.repairPlayerItems(serverplayer, this.value);
            if (i > 0) {
                entity.giveExperiencePoints(i);
            }

            --this.count;
            if (this.count == 0) {
                System.out.println(buffer);
                entity.giveExperiencePoints(buffer);

                this.discard();
            }
        }
    }

    private void fusion(){
        if(this.buffer >= this.value){
            this.count++;
            this.buffer -= this.value;
        }
    }
    private int repairPlayerItems(ServerPlayer player, int value) {
        Optional<EnchantedItemInUse> optional = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, player, ItemStack::isDamaged);
        if (optional.isPresent()) {
            ItemStack itemstack = optional.get().itemStack();
            int i = EnchantmentHelper.modifyDurabilityToRepairFromXp(player.serverLevel(), itemstack, (int)((float)value * itemstack.getXpRepairRatio()));
            int j = Math.min(i, itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - j);
            if (j > 0) {
                int k = value - j * value / i;
                if (k > 0) {
                    return this.repairPlayerItems(player, k);
                }
            }

            return 0;
        } else {
            return value;
        }
    }
}
