package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemClusterRenderState.class)
public abstract class ItemRenderStateMixin extends EntityRenderState {
    @Inject(method = "getRenderedAmount", at = @At("RETURN"), cancellable = true)
    private static void getRenderedAmount(int count, CallbackInfoReturnable<Integer> cir){
        if(FeatureConfiguration.ItemRenderCap.get() != 0){
            cir.setReturnValue(Math.min(cir.getReturnValue(),FeatureConfiguration.ItemRenderCap.get()));
        }
    }
}

