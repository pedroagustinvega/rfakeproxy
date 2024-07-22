package dev.wrrulos.rFakeProxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.wrrulos.rFakeProxy.listeners.ChatListener;
import dev.wrrulos.rFakeProxy.listeners.CommandListener;
import dev.wrrulos.rFakeProxy.listeners.ConnectionListener;
import dev.wrrulos.rFakeProxy.listeners.ProxyPing;
import org.slf4j.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Plugin(
    id = "rfakeproxy",
    name = "RFakeProxy",
    version = BuildConstants.VERSION,
    description = "Plugin created for MCPTool fakeproxy command",
    url = "wrrulos.dev",
    authors = {"Pedro Agustin Vega"}
)
public class RFakeProxy {
    private final ProxyServer server;
    private static String adminKey;
    private static List<String> admins = new ArrayList<>();
    private static final String PREFIX = "#";

    @Inject
    private Logger logger;

    /**
     * Constructor for the RFakeProxy class
     * @param server The proxy server
     * @param logger The logger
     */
    @Inject
    public RFakeProxy(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        // Generate the admin key (random string of 10 characters)
        RFakeProxy.adminKey = generateRandomString(10);
        System.out.println("[RFakeProxy] " + "[ADMINKEY] " + adminKey);
    }

    /**
     * Register the ProxyPing listener when the proxy is initialized
     * @param event The ProxyInitializeEvent
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        EventManager eventManager = server.getEventManager();
        eventManager.register(this, new ProxyPing(server, logger));
        eventManager.register(this, new ConnectionListener());
        eventManager.register(this, new ChatListener(server, logger));
        eventManager.register(this, new CommandListener());
        logger.info("RFakeProxy has been initialized successfully.");

        if (server.getServer("lobby").isPresent()) {
            logger.info("Target server: {}", server.getServer("lobby").get().getServerInfo().getAddress().toString().split("/")[0]);
        } else {
            logger.warn("Target server not found.");
        }
    }

    public static List<String> getAdmins() {
        return RFakeProxy.admins;
    }

    public static void addAdmin(String admin) {
        RFakeProxy.admins.add(admin);
    }

    public static String getAdminKey() {
        return RFakeProxy.adminKey;
    }

    public static String getPrefix() {
        return RFakeProxy.PREFIX;
    }

    public static String generateRandomString(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }

        return stringBuilder.toString();
    }
}
