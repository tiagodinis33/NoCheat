package me.sxstore.nocheat;

public class Config {

    public static boolean ENABLE_ALERTS_ON_JOIN;

    public static boolean LOG_TO_CONSOLE, ENABLE_LOGGING;

    public static void updateConfig(){

        ENABLE_ALERTS_ON_JOIN = NoCheatMain.getInstance().getConfig().getBoolean("enable-alerts-on-join");

        LOG_TO_CONSOLE = NoCheatMain.getInstance().getConfig().getBoolean("logging.log-to-console");
        ENABLE_LOGGING = NoCheatMain.getInstance().getConfig().getBoolean("logging.enabled");
    }
}
