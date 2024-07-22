package dev.wrrulos.rFakeProxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
    id = "rfakeproxy",
    name = "RFakeProxy",
    version = BuildConstants.VERSION
    , description = "Plugin created for MCPTool fakeproxy command "
    , url = "wrrulos.dev"
    , authors = {"Pedro Agustin Vega"}
)
public class RFakeProxy {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
