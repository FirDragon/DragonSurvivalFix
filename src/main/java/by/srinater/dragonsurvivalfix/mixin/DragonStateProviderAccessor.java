package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import com.ibm.icu.impl.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DragonStateProvider.class)
public interface DragonStateProviderAccessor {
    @Invoker(remap = false, value = "getFakePlayer")
    static Pair<Boolean, LazyOptional<DragonStateHandler>> getFakePlayer(Entity entity) {
        throw new AssertionError();
    }
}
