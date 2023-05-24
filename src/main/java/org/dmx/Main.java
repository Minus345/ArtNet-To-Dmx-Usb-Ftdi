package org.dmx;

import ch.bildspur.artnet.ArtNetClient;
import com.jaysonh.dmx4artists.DMXControl;

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

    public static void main(String[] args) throws SocketException {
        System.out.println("Hello world!");
        DMXControl dmx;
        dmx = new DMXControl(0, 511);

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            System.out.println(networkInterface);
        }

        NetworkInterface ni = NetworkInterface.getByName("eth6");
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
}