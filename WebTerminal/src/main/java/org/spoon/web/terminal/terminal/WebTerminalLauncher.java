/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package org.spoon.web.terminal.terminal;

import org.apache.log4j.BasicConfigurator;

public class WebTerminalLauncher {

    public static void main(String... args) throws Exception {

        BasicConfigurator.configure();
        WebTerminalGateway gateway = new WebTerminalGateway();
        Thread backendThread = new Thread(new WebTerminalBackend(), "backend");

        String url = "localhost:8080";
        if (args.length > 0) {
            url = args[0];
        }

        System.out.println("Starting Gateway");
        gateway.start(url);
        System.out.println("Gateway Started");
        System.out.println("Starting Backend");
        backendThread.start();
        System.out.println("Backend Started");
        System.out.println("******************************************************************************");
        System.out.println("******************************************************************************");
        System.out.println();
        System.out.println();
        System.out.println("Navigate Browser to http://" + url + " to test the app");
        System.out.println();
        System.out.println();
        System.out.println("******************************************************************************");
        System.out.println("******************************************************************************");
        backendThread.join();
        System.out.println("Exiting...");
    }

}
