package com.gmail.orangeandy2007.martensite.martensiteneo.mixin.manager;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Unique
    private static final int ADDITIONAL_HISTORY = 924;

    @ModifyExpressionValue(method = {"addMessageToDisplayQueue","addMessageToQueue","addRecentChat"},at = @At(value = "CONSTANT", args = "intValue=100"))
    public int useAdditionalHistory(int original){
        return original + ADDITIONAL_HISTORY;
    }
}
