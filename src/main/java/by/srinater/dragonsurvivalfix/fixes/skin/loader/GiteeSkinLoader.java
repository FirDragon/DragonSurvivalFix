package by.srinater.dragonsurvivalfix.fixes.skin.loader;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvivalMod;
import by.dragonsurvivalteam.dragonsurvival.client.skins.NetSkinLoader;
import by.dragonsurvivalteam.dragonsurvival.client.skins.SkinObject;
import by.dragonsurvivalteam.dragonsurvival.util.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

public class GiteeSkinLoader extends NetSkinLoader{
    // https://gitee.com/api/v5/repos/{owner}/{repo}/git/trees/{sha}
    private static final String SKINS_LIST_LINK = "https://gitee.com/api/v5/repos/srinater/DragonSurvival/git/trees/master?recursive=1";
    private static final String SKINS_PING = "https://gitee.com/api/v5/repos/srinater/DragonSurvival/git/trees/master";
    private static final String SKINS_LOCATION = "src/test/resources/";
    static class GiteeSkin{
        public String path;
        public String type;
        public String sha;
        public String url;// blob url
        public int size;
    }
    static class GiteeSkinListApiResponse {
        public GiteeSkin[] tree;
        public boolean truncated;
    }
    static class GiteeSkinResponse {
        public String sha;
        public String size;
        public String url;
        public String content;
        public String encoding;
    }
    public Collection<SkinObject> querySkinList()
    {
        BufferedReader reader;
        GiteeSkinListApiResponse skinListResponse;
        ArrayList<SkinObject> result = new ArrayList<>();
        try {
            Gson gson = GsonFactory.getDefault();
            URL url = new URL(SKINS_LIST_LINK);
            reader = new BufferedReader(new InputStreamReader(internetGetStream(url, 2000)));
            try {
                skinListResponse = gson.fromJson(reader, GiteeSkinListApiResponse.class);
                reader.close();
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
            if (skinListResponse.truncated)
            {
                DragonSurvivalMod.LOGGER.warn("The result returned by gitee skin library contains slices, please pay attention to it!!!");
            }
            if (skinListResponse.tree.length != 0) {
                for (GiteeSkin giteeSkin : skinListResponse.tree)
                {
                    if (!giteeSkin.type.equals("blob") && giteeSkin.path.startsWith(SKINS_LOCATION))
                        continue;
                    SkinObject skin = new SkinObject();
                    skin.name = giteeSkin.path.substring(SKINS_LOCATION.length());
                    skin.short_name = giteeSkin.path;
                    skin.id = giteeSkin.sha;
                    skin.size = giteeSkin.size;
                    skin.user_extra = giteeSkin;
                    result.add(skin);
                }
            } else {
                return result;
            }
        } catch (Throwable e) {
            DragonSurvivalMod.LOGGER.log(Level.WARN, "Failed to get skin information in Gitcode.");
            return null;
        }
        return result;
    }
    public InputStream querySkinImage(SkinObject skin) throws IOException {
        Gson gson = GsonFactory.getDefault();
        if (!(skin.user_extra instanceof GiteeSkin giteeSkin))
        {
            DragonSurvivalMod.LOGGER.error("This is an unexpected development error, please report it to the developer.");
            throw new IOException("Skin object extra is of unexpected type");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(internetGetStream(new URL(giteeSkin.url), 2000)));
        try {
            GiteeSkinResponse giteeSkinResponse = gson.fromJson(reader, GiteeSkinResponse.class);
            reader.close();
            if (!giteeSkinResponse.encoding.equals("base64"))
            {
                DragonSurvivalMod.LOGGER.error("This is an unexpected development error, please report it to the developer.");
                throw new IOException("Wrong skin data encoding.the encoding is:" + giteeSkinResponse.encoding);
            }
            return new ByteArrayInputStream(Base64.getDecoder().decode(giteeSkinResponse.content));
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
    public boolean ping() {
        try(InputStream ignore = internetGetStream(new URL(SKINS_PING), 3 * 1000))
        {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
