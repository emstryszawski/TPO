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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.*;

public class Server {
    
    private final String host;
    private final int port;
    ServerSocketChannel serverChannel;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }
    private final Charset charset = Charset.forName("ISO-8859-2");

    public void startServer() {
        new Thread(() -> {
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.socket().bind(new InetSocketAddress(host, port));
                serverChannel.configureBlocking(false);

                Selector selector = Selector.open();
                serverChannel.register(selector, OP_ACCEPT);

                while (true) {

                    int select = selector.select();

                    if (select == 0)
                        continue;

                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();

                    while (iterator.hasNext()) {

                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            SocketChannel client = serverChannel.accept();
                            System.out.println("client.isConnected() = " + client.isConnected());
                            client.configureBlocking(false);
                            client.register(selector, OP_READ);
                            continue;
                        }

                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            read(client);
                        }

                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            client.write(ByteBuffer.wrap("hi".getBytes()));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    StringBuffer requestBuffer = new StringBuffer(); // żądanie
    StringBuffer responseBuffer = new StringBuffer(); // odpowiedź

    private void read(SocketChannel client) {
        // reading from server

        if (!client.isOpen()) return;

        try {
            requestBuffer.setLength(0);
            buffer.clear();
            readLoop:
            while (true) {
                int n = client.read(buffer);
                if (n > 0) {
                    buffer.flip();
                    CharBuffer cbuf = charset.decode(buffer);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '$') break readLoop;
                        requestBuffer.append(c);
                    }
                }
            }

            String request = requestBuffer.toString();
            System.out.println("request = " + request);

            if (request.contains("login")) {
                write(client, "logged in");
            }

            else if (Time.isDate(request)) {
                System.out.println("date");
                write(client, "date");
            }

            else if (Time.isDateTime(request)) {
                System.out.println("datetime");
                write(client, "dateTime");
            }

            else if (request.equals("bye and log transfer$")) {
                try {
                    client.close();
                    client.socket().close();
                } catch (IOException e1) {
                    System.out.println(e1);
                }
            }

            else {
                System.out.println("bad request");
                throw new IOException();
            }

        } catch (IOException e) {
            try {
                client.close();
                client.socket().close();
            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
    }

    private void write(SocketChannel client, String message) {
        if (!client.isOpen()) return;
        try {
            responseBuffer.setLength(0);
            responseBuffer.append(message);
            responseBuffer.append("$");
            buffer.clear();
            buffer.flip();
            buffer = charset.encode(CharBuffer.wrap(responseBuffer));
            client.write(buffer);
        } catch (IOException e) {
            try {
                client.close();
                client.socket().close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            if (serverChannel.isOpen()) {
                serverChannel.close();
                serverChannel.socket().close();
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();;
            System.exit(1);
        }

    }

    public boolean getServerLog() {
        return false;
    }
}
