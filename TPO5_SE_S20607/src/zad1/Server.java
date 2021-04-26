/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.*;

public class Server {
    
    private final String host;
    private final int port;
    
    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                serverChannel.socket().bind(new InetSocketAddress(host, port));
                serverChannel.configureBlocking(false);

                Selector selector = Selector.open();
                serverChannel.register(selector, OP_ACCEPT);

                while (true) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            SocketChannel client = serverChannel.accept();
                            System.out.println("Connection established");
                            client.configureBlocking(false);
                            client.register(selector, OP_READ);
                        }

                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            client.read(buffer);
                            String messageFromClient = new String(buffer.array());
                            System.out.println("messageFromClient = " + messageFromClient);
                            if (messageFromClient.equals("bye and log transfer"))
                                client.socket().close();
                            client.register(selector, OP_WRITE);
                        }

                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            String response = "hi - from non-blocking server";
                            byte[] bs = response.getBytes();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            buffer.put(bs);

                            int write = client.write(buffer);
//                            System.out.println("write = " + write);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopServer() {

    }

    public boolean getServerLog() {
        return false;
    }
}
