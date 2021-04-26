/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;

import jdk.internal.dynalink.support.NameCodec;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Client {

    private final String host;
    private final int port;
    private final String id;
    private SocketChannel channel;
    private Charset charset = Charset.forName("ISO-8859-2");
    ByteBuffer buffer = ByteBuffer.allocateDirect(512);
    StringBuffer requestBuffer = new StringBuffer(); // żądanie
    StringBuffer responseBuffer = new StringBuffer(); // odpowiedź

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect() {
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
            channel.configureBlocking(false);

            while (!channel.finishConnect()) {

            }
        } catch (IOException e) {
            System.out.println("Unknown host [Client -> Server]");
        }
    }

    public String send(String req) {
        req += '$';
        System.out.println("req = " + req);
        buffer.clear();
        String message = "";
        try {
            // writing to server
            buffer.put(req.getBytes());
            buffer.flip();
            channel.write(buffer);

//             reading from server
            responseBuffer.setLength(0);
            buffer.clear();
            readLoop:
            while (true) {
                int n = channel.read(buffer);
                if (n > 0) {
                    buffer.flip();
                    CharBuffer cbuf = charset.decode(buffer);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '$') break readLoop;
                        responseBuffer.append(c);
                    }
                }
            }
            message = responseBuffer.toString();
            System.out.println("message = " + message);
            if (req.equals("bye and log transfer$")) {
                channel.close();
                channel.socket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String getId() {
        return id;
    }
}
