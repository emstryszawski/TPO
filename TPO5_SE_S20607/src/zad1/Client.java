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
import java.nio.charset.Charset;

public class Client {

    private final String host;
    private final int port;
    private final String id;
    private final Charset charset = Charset.forName("ISO-8859-2");
    private SocketChannel channel;

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

        } catch (IOException e) {
            System.out.println("Unknown host [Client -> Server]");
        }
    }

    public String send(String req) {
        StringBuilder response = new StringBuilder();
        try {
            ByteBuffer buffer = ByteBuffer.wrap(req.getBytes());
            channel.write(buffer);
            buffer.clear();

            int read = channel.read(buffer);

            while (read == 0) {
                read = channel.read(buffer);
            }

            while (read != 0) {
                buffer.flip();
                CharBuffer charBuffer = charset.decode(buffer);
                response.append(charBuffer);
                buffer.clear();
                read = channel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String getId() {
        return id;
    }
}
