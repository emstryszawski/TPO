/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Client {

    private final String host;
    private final int port;
    private final String id;
    private SocketChannel channel;

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            System.out.println("Connecting...");
            while (!channel.finishConnect()) {
                Thread.sleep(500);
                System.out.println("Connecting...");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Unknown host [Client -> Server]");
        }
        System.out.println("Connected");
    }

    public String send(String req) {
        ByteBuffer inBuf = ByteBuffer.allocateDirect(512);
        ByteBuffer outBuf = ByteBuffer.allocateDirect(512);
        String message = "";
        try {
            outBuf.put(req.getBytes());
            outBuf.flip();
            channel.write(outBuf);

            while (true) {

                inBuf.clear();

                int readBytes = channel.read(inBuf);

                if (readBytes == 0) {
                    continue;
                }
                else if (readBytes == -1) {

                    break;
                }
                else {
                    inBuf.flip();
                    System.out.println("inBuf.get() = " + inBuf.get());
                    if (readBytes > 0) {
                        CharBuffer charBuffer = inBuf.asCharBuffer();
                        System.out.println("charBuffer.get() = " + charBuffer.get());
                    }
                }
            }
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String getId() {
        return id;
    }
}
