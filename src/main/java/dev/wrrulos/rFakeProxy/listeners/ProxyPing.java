package dev.wrrulos.rFakeProxy.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProxyPing {
    private final ProxyServer server;
    private final Logger logger;

    /**
     * Constructor for the ProxyPing class
     * @param server The proxy server
     * @param logger The logger
     */
    public ProxyPing(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    /**
     * Modify the ping response to match the target server
     * @param event The ProxyPingEvent
     */
    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing ping = event.getPing();
        ServerPing.Builder builder = ping.asBuilder();

        server.getServer("lobby").ifPresent(targetServer -> {
            CompletableFuture<ServerPing> futurePing = targetServer.ping();

            try {
                ServerPing serverPing = futurePing.get();  // Stop the thread until the ping is done

                if (serverPing.getPlayers().isPresent()) {
                    builder.onlinePlayers(serverPing.getPlayers().get().getOnline());
                    builder.maximumPlayers(serverPing.getPlayers().get().getMax());
                    List<ServerPing.SamplePlayer> samplePlayers = new ArrayList<>(serverPing.getPlayers().get().getSample());
                    builder.samplePlayers(samplePlayers.toArray(new ServerPing.SamplePlayer[0]));
                }

                //builder.version(serverPing.getVersion());  TODO: Implement version later
                builder.description(serverPing.getDescriptionComponent());
                serverPing.getFavicon().ifPresent(builder::favicon);
                ServerPing newPing = builder.build();
                event.setPing(newPing);

            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error pinging target server", e);
            }
        });
    }
}
