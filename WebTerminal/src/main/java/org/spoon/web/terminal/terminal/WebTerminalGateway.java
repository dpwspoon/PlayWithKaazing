/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package org.spoon.web.terminal.terminal;

import org.kaazing.gateway.server.test.Gateway;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;

import java.io.File;
import java.net.URI;

public class WebTerminalGateway {
    final File webDir;

    public WebTerminalGateway() throws Exception {
        // TODO: Yuck, wish we could include it in the jar :-(
        webDir = new File("src/main/resources/web");
        if(!webDir.exists()){
            throw new Exception("The web dir expects that you run the jar from, the top level directory of the " +
                    "project, look at org.kaazing.utilities.web.terminal.WebTerminalGateway");
        }
    }

    public void start(String url) throws Exception {

        // @formatter:off
        GatewayConfiguration gatewayConfiguration = new GatewayConfigurationBuilder()
            // TODO pull file from Jar

            .webRootDirectory(webDir)
            .service()
                .name("wsToTcpBackendProxy")
                .description("This is the proxy to a backend tcp service which is running a terminal")
                .accept(URI.create("ws://" + url))
                .connect(URI.create("tcp://localhost:7777"))
                .type("proxy")
                .crossOrigin()
                    .allowOrigin("*")
                .done()
            .done()
            .service()
                .name("directoryService")
                .description("A directory service to serve the js resources to the browser")
                .accept(URI.create("http://" + url))
                .type("directory")
                .property("directory", "/")
                .property("welcome-file", "index.html")
                // TODO Add Realm and Security if you actually want to deploy this!!
            .done()
        .done();
        // @formatter:on

        Gateway gateway = new Gateway();
        gateway.start(gatewayConfiguration);
    }

}
