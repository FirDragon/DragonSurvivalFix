package by.srinater.dragonsurvivalfix.fixes;

import by.dragonsurvivalteam.dragonsurvival.common.capability.Capabilities;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.srinater.dragonsurvivalfix.mixin.DragonStateProviderAccessor;
import com.ibm.icu.impl.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public class DragonStateProviderProxy {
    public static LazyOptional<DragonStateHandler> getCap(Entity entity) {
        if (entity == null) {
            return LazyOptional.empty();
        } else {
            if (entity.level().isClientSide) {
                Pair<Boolean, LazyOptional<DragonStateHandler>> fakeState = DragonStateProviderAccessor.getFakePlayer(entity);
                if (fakeState.first) {
                    return fakeState.second;
                }
            }

            if (!(entity instanceof Player)) {
                return LazyOptional.empty();
            }
            return entity.getCapability(Capabilities.DRAGON_CAPABILITY);
        }
    }
}
