/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package org.spoon.web.terminal.terminal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebTerminalBackend implements Runnable {

    //    @Override
    //    public void run() {
    //        try {
    //            ServerSocket serverSocket = new ServerSocket(7777);
    //            Socket client = serverSocket.accept();
    //
    //            final OutputStream clientOutputStream = client.getOutputStream();
    //            PrintWriter clientPrintWriter = new PrintWriter(clientOutputStream, true);
    //            BufferedReader clientBufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    //
    //            File workingDir = new File("./");
    //
    //            // Everything comes in as a new line
    //            String cmd;
    //            while ((cmd = clientBufferedReader.readLine()) != null) {
    //
    //                // Hack to get directory to change, in reality this is why you need TelNet,
    //                //  That and vim is awesome!!
    //                if (cmd.startsWith("cd")) {
    //                    String[] args = cmd.split("\\s+");
    //                    if (args.length > 1) {
    //                        try {
    //                            final String fileName = args[1].trim();
    //                            File file = new File(workingDir, fileName);
    //                            if (file.exists()) {
    //                                System.out.print("Dir changed to: " + file.getAbsolutePath());
    //                                workingDir = file;
    //                            }
    //                        } catch (Exception e) {
    //                            // NOOP
    //                        }
    //                    }
    //                }
    //
    //                try {
    //                    System.out.println("Running cmd: " + cmd);
    //                    Process process = new ProcessBuilder("bash", "-c", cmd).directory(workingDir).start();
    //                    final InputStream inputStream = process.getInputStream();
    //                    byte[] buffer = new byte[1024];
    //                    int len = inputStream.read(buffer);
    //                    while (len != -1) {
    //                        clientOutputStream.write(buffer, 0, len);
    //                        len = inputStream.read(buffer);
    //                    }
    //                } catch (Exception e) {
    //                    System.out.println("exception " + e);
    //                    clientPrintWriter.println(e.getMessage());
    //                }
    //                // Always need to end with a new line
    //                clientPrintWriter.println();
    //
    //                // Looks like PrintWriter flush passes flush along
    //                clientPrintWriter.flush();
    //            }
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true) {
                Socket client = serverSocket.accept();
                Runnable connectionHandler = new ConnectionHandler(client);
                new Thread(connectionHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ConnectionHandler implements Runnable {

        final Socket client;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                final OutputStream clientOutputStream = client.getOutputStream();
                final PrintWriter clientPrintWriter = new PrintWriter(clientOutputStream, true);
                final BufferedReader clientBufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // Used for hacking cd
                File workingDir = new File("./");

                // Everything comes in as a new line
                String cmd;
                while ((cmd = clientBufferedReader.readLine()) != null) {

                    // Hack to get directory to change, in reality this is why you need TelNet,
                    //  That and vim is awesome!!
                    if (cmd.startsWith("cd")) {
                        String[] args = cmd.split("\\s+");
                        if (args.length > 1) {
                            try {
                                final String fileName = args[1].trim();
                                File file = new File(workingDir, fileName);
                                if (file.exists()) {
                                    System.out.print("Dir changed to: " + file.getAbsolutePath());
                                    workingDir = file;
                                }
                            } catch (Exception e) {
                                // NOOP
                            }
                        }
                    }

                    try {
                        System.out.println("Running cmd: " + cmd);
                        Process process = new ProcessBuilder("bash", "-c", cmd).directory(workingDir).start();
                        final InputStream inputStream = process.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = inputStream.read(buffer);
                        while (len != -1) {
                            clientOutputStream.write(buffer, 0, len);
                            len = inputStream.read(buffer);
                        }
                    } catch (Exception e) {
                        System.out.println("exception " + e);
                        clientPrintWriter.println(e.getMessage());
                    }
                    // Always need to end with a new line
                    clientPrintWriter.println();

                    // Looks like PrintWriter flush passes flush along
                    clientPrintWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
