package by.srinater.dragonsurvivalfix.config.attributes;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.config.ModConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigItem {
    Dist[] dist();
    String name();
    String region();
}
