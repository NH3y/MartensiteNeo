package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.nmEntityCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration.DEATHCANCEL;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTickMixin extends Entity implements nmEntityCache {

    @Shadow public int deathTime;

    public LivingEntityTickMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tickDeath", at = @At("HEAD"))
    public void tickDeath(CallbackInfo ci) {
        if(DEATHCANCEL.get())this.deathTime = 19;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci){
        if(this.martensiteNew$getNowCancel() && !this.getType().toString().equals("minecraft:player")) ci.cancel();
    }
    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    public void baseTick(CallbackInfo ci){
        if(this.martensiteNew$getNowCancel() && !this.getType().toString().equals("minecraft:player")) ci.cancel();
    }

    @Inject(method="knockback", at = @At("HEAD"), cancellable = true)
    public void knockback(CallbackInfo ci)   {
        double Radius = ProtectZoneConfiguration.RADIUS.get();
        if (this.martensiteNew$getCacheNearestPlayerDisSq() >= Radius && Math.random() < 0.5*(this.martensiteNew$getCacheNearestPlayerDisSq() - Radius)/(128 - Radius)) {
            ci.cancel();
        }
    }
    @Inject(method="playBlockFallSound", at = @At("HEAD"), cancellable = true)
    public void playBlockFallSound(CallbackInfo ci){
        if(this.martensiteNew$getCacheNearestPlayerDisSq()>= 2*ProtectZoneConfiguration.RADIUS.get()) ci.cancel();
    }
    @Inject(method="handleEntityEvent", at = @At("HEAD"), cancellable = true)
    public void handleEntityEvent(byte p_20975_, CallbackInfo ci){
        if(p_20975_ == 46 && this.martensiteNew$getCacheNearestPlayerDisSq() >= ProtectZoneConfiguration.RADIUS.get()*2) ci.cancel();
    }
    @Inject(method="spawnItemParticles", at = @At("HEAD"), cancellable = true)
    public void spawnItemParticles(CallbackInfo ci){
        if(this.martensiteNew$getCacheNearestPlayerDisSq()>= 2*ProtectZoneConfiguration.RADIUS.get()) ci.cancel();
    }
    @Inject(method = "push", at = @At("HEAD"), cancellable = true)
    public void push(CallbackInfo ci){
        if(this.martensiteNew$getNowCancel()) ci.cancel();
    }
}
