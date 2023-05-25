package org.dmx;

import ch.bildspur.artnet.ArtNetClient;
import com.jaysonh.dmx4artists.DMXControl;
import org.apache.commons.cli.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static int subnet = 0;
    private static int universe = 0;
    private static boolean debug = false;
    private static String networkInterface;

    public static void main(String[] args) throws SocketException {
        System.out.println("Hello world!");
        commandLineParameters(args);

        DMXControl dmx;
        dmx = new DMXControl(0, 511);

        if (networkInterface == null){
            System.out.println("No network interface detected");
            System.exit(200);
        }

        NetworkInterface ni = NetworkInterface.getByName(networkInterface);
        InetAddress address = ni.getInetAddresses().nextElement();

        ArtNetClient artnet = new ArtNetClient();
        artnet.start(address);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 511; i++) {
                    byte[] artnetData = artnet.readDmxData(subnet, universe);
                    dmx.sendValue(i, artnetData[i]);
                    if (debug) System.out.println(Arrays.toString(artnetData));
                }
            }
        }, 0, 25, TimeUnit.MILLISECONDS);
    }

    private static void commandLineParameters(String[] args) {
        var options = new Options()
                .addOption("h", "help", false, "Help Message")
                .addOption("d", "debug", false, "debug")
                .addOption("l", "list-interfaces", false, "lists the network interfaces")
                .addOption(Option.builder("s")
                        .longOpt("subnet")
                        .hasArg(true)
                        .desc("ArtNet Subnet")
                        .argName("0")
                        .build())
                .addOption(Option.builder("u")
                        .longOpt("universe")
                        .hasArg(true)
                        .desc("ArtNet Universe")
                        .argName("0")
                        .build())
                .addOption(Option.builder("i")
                        .longOpt("interface")
                        .hasArg(true)
                        .desc("Network interface")
                        .argName("ethx")
                        .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args);
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar dmx.jar ", options);
                System.exit(0);
            }
            if (line.hasOption("u")) {
                universe = Integer.parseInt(line.getOptionValue("u"));
            }
            if (line.hasOption("s")) {
                subnet = Integer.parseInt(line.getOptionValue("s"));
            }
            if (line.hasOption("d")) {
                debug = true;
            }
            if (line.hasOption("i")) {
                networkInterface = line.getOptionValue("i");
            }
            if (line.hasOption("l")) {
                System.out.println("List Network Interfaces:");
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    System.out.println(networkInterface);
                }
                System.exit(201);
            }
        } catch (ParseException | SocketException e) {
            throw new RuntimeException(e);
        }
    }
}