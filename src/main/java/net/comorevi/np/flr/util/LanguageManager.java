package net.comorevi.np.flr.util;

import cn.nukkit.Server;

import java.util.Arrays;

public class LanguageManager {
    private static String[] availableLanguages = {"eng", "jpn"};

    public static String getPluginLang() {
        return Arrays.asList(availableLanguages).contains(Server.getInstance().getLanguage().getLang()) ? Server.getInstance().getLanguage().getLang() : "eng";
    }
}
