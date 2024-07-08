package by.srinater.dragonsurvivalfix.config;

import by.srinater.dragonsurvivalfix.config.attributes.ConfigComment;
import by.srinater.dragonsurvivalfix.config.attributes.ConfigItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Config {
    @ConfigComment("Fixed the issue of being unable to enter the game and display player skins due to skin loader errors.")
    @ConfigItem(region = "common", name = "fix-skin-loader", dist = Dist.CLIENT)
    public static boolean EnableFixSkinLoader = true;

    @ConfigComment("Fixed dragons turning into humans when entering the game.")
    @ConfigItem(region = "common", name = "disable-dragon-state-cache", dist = {Dist.CLIENT,Dist.DEDICATED_SERVER})
    public static boolean DisableDragonStateCache = true;

    @ConfigComment("Fixed cheating behavior caused by skill downgrade logic issues.")
    @ConfigItem(region = "common", name = "enable-fix-skill-downgrade-bug", dist = Dist.DEDICATED_SERVER)
    public static boolean EnableFixSkillDowngradeBug  = true;

    @ConfigComment("Fixed the issue where dragon breath could hurt other dragons and humans in PVE.")
    @ConfigItem(region = "common", name = "disable-dragon-breath-hurt-player-in-PVE", dist = Dist.DEDICATED_SERVER)
    public static boolean DisableDragonBreathHurtPlayerInPVE  = true;
}
