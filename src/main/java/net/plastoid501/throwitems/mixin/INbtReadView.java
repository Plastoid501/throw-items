package net.plastoid501.throwitems.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.NbtReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NbtReadView.class)
public interface INbtReadView {
    @Accessor
    NbtCompound getNbt();
}
