package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.nmEntityCache;

@Mixin(RandomStrollGoal.class)
public abstract class StrollGoalMixin extends Goal {

    @Shadow @Final protected PathfinderMob mob;

    @Inject(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;getNoActionTime()I"), cancellable = true)
    protected void onCheck(CallbackInfoReturnable<Boolean> cir) {
        if(((nmEntityCache)this.mob).martensiteNew$getCacheNearestPlayerDisSq() >= ProtectZoneConfiguration.RADIUS.get()){
            cir.setReturnValue(false);
        }
        cir.setReturnValue(false);
    }
}
