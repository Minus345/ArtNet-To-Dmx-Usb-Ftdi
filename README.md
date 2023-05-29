# Fidi-Dmx-Usb-To-ArtNet

Java Software that sends the ArtNet data to the dmx ftid usb dongel  
Build for https://www.amazon.de/DSD-TECH-RS485-Adapter-Lichtsteuerungskabel-Schwarz/dp/B07WV6P5W6

Tested on:
* windows 10
* ~~raspberry pi~~

### Requirements
* java 19
* ftdi driver

### Parameters

`java -jar FtdiDmx.jar`

| Parameter              |                   Usage                   |
|------------------------|:-----------------------------------------:|
| -d, --debug            |      shows the debug artnet packetes      |
| -h, --help             |          shows the help message           |
| -i, --interface <ethx> | network interface you recieve your artnet |
| -l, --list-interfaces  |       lists the network interfaces        |
| -s, --subnet <0>       |               ArtNet Subnet               |
| -u, --universe <0>     |              ArtNet Universe              |

### Libs Used:

- Dmx4Artists https://github.com/jaysonh/Dmx4Artists
- ArtNet4J https://github.com/cansik/artnet4j
