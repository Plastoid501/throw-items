package net.plastoid501.throwitems.mixin;

import net.minecraft.client.MinecraftClient;
import net.plastoid501.throwitems.event.KeyBindHandler;
import net.plastoid501.throwitems.util.ClientUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void preTick(CallbackInfo ci) {
        ClientUtil.throwItems((MinecraftClient) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void postTick(CallbackInfo ci) {
        KeyBindHandler handler = KeyBindHandler.getInstance();
        handler.update();
    }
}
