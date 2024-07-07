package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvivalMod;
import by.dragonsurvivalteam.dragonsurvival.client.skins.GitcodeSkinLoader;
import by.dragonsurvivalteam.dragonsurvival.client.skins.NetSkinLoader;
import by.dragonsurvivalteam.dragonsurvival.client.skins.SkinObject;
import by.dragonsurvivalteam.dragonsurvival.util.GsonFactory;
import by.srinater.dragonsurvivalfix.utils.GitcodeSkinLoader_SkinListApiResponse;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@Mixin(GitcodeSkinLoader.class)
public class GitcodeSkinLoaderMixin extends NetSkinLoader {
    private static final String SKINS_LIST_LINK = "https://web-api.gitcode.com/api/v1/projects/mirrors%2FDragonSurvivalTeam%2FDragonSurvival/repository/tree?ref=master&path=src/test/resources&per_page=100&page=";
    private static final String SKINS_DOWNLOAD_LINK = "https://web-api.gitcode.com/api/v1/projects/mirrors%%2FDragonSurvivalTeam%%2FDragonSurvival/repository/blobs/%s/raw?ref=master&file_name=%s";
    private static final String SKINS_PING = "https://web-api.gitcode.com/api/v1/projects/mirrors%2FDragonSurvivalTeam%2FDragonSurvival/repository/tree?ref=master&path=src/test&per_page=100&page=1";
    private static final HashMap<String, String> GITCODE_HEADER = new HashMap<String, String>() { // from class: by.dragonsurvivalteam.dragonsurvival.client.skins.GitcodeSkinLoader.1
        {
            put("referer", "https://gitcode.com/");
        }
    };
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public Collection<SkinObject> querySkinList()
    {
        BufferedReader reader;
        GitcodeSkinLoader_SkinListApiResponse skinListResponse;
        ArrayList<SkinObject> result = new ArrayList<>();
        int page = 1;
        while (true) {
            try {
                Gson gson = GsonFactory.getDefault();
                URL url = new URL("https://web-api.gitcode.com/api/v1/projects/mirrors%2FDragonSurvivalTeam%2FDragonSurvival/repository/tree?ref=master&path=src/test/resources&per_page=100&page=" + page);
                reader = new BufferedReader(new InputStreamReader(internetGetStream(url, GITCODE_HEADER, 2000)));
                try {
                    skinListResponse = gson.fromJson(reader, GitcodeSkinLoader_SkinListApiResponse.class);
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
                if (skinListResponse.content.length != 0) {
                    result.addAll(Arrays.asList(skinListResponse.content));
                    page++;
                    reader.close();
                } else {
                    reader.close();
                    return result;
                }
            } catch (IOException e) {
                DragonSurvivalMod.LOGGER.log(Level.WARN, "Failed to get skin information in Gitcode.");
                return null;
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public InputStream querySkinImage(SkinObject skin) throws IOException {
        return internetGetStream(new URL(String.format(SKINS_DOWNLOAD_LINK, skin.id, skin.name)), GITCODE_HEADER, 15000);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean ping() {
        try(InputStream ignore = internetGetStream(new URL(SKINS_PING), GITCODE_HEADER, 3 * 1000))
        {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
