package org.dmx;

import ch.bildspur.artnet.ArtNetClient;
import com.jaysonh.dmx4artists.DMXControl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

public class Main {
    private static int subnet = 0;
    private static int universe = 0;

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
        while (true) { //Runable with sleep may be better here
            for (int i = 0; i < 511; i++) {
                byte[] artnetData = artnet.readDmxData(subnet, universe);
                dmx.sendValue(i, artnetData[i]);
                //System.out.println(Arrays.toString(artnetData));
            }
        }

    }
}