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
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class Server {

    static class Logs {

        private final String id;
        private final StringBuilder logs;

        public Logs(String id) {
            this.id = id;
            logs = new StringBuilder();
        }

        public String getId() {
            return id;
        }

        public StringBuilder getLogs() {
            return logs;
        }
    }
    
    private final String host;
    private final int port;

    private Thread serverThread;
    private ServerSocketChannel serverChannel;

    private final Map<SocketChannel, Logs> channelLogMap = new HashMap<>();
    private final StringBuilder serverLog = new StringBuilder();

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String getUserFromRequest(String req) {
        return req.substring(req.indexOf(' ') + 1);
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socket = socketChannel.accept();
        socket.configureBlocking(false);
        socket.register(key.selector(), OP_READ);
    }

    private void passed(StringBuilder logs, StringBuilder res, String req, String id) {
        String[] dates = req.split(" ");
        logs.append("Request: ").append(req).append("\n").append("Result:\n").append(Time.passed(dates[0], dates[1])).append("\n");
        serverLog.append(id).append(" request at ").append(LocalTime.now()).append(": \"").append(req).append("\"").append("\n");
        res.append(Time.passed(dates[0], dates[1]));
    }

    private StringBuilder requestHandler(SocketChannel client, String req) {
        StringBuilder res = new StringBuilder();

        if (!channelLogMap.containsKey(client)) {
            channelLogMap.put(client, new Logs(getUserFromRequest(req)));
        }

        Logs log = channelLogMap.get(client);
        String id = log.getId();
        StringBuilder logs = log.getLogs();

        if (req.contains("login")) {
            logs.append("=== ").append(id).append(" log start ===\n").append("logged in\n");
            serverLog.append(id).append(" logged in at ").append(LocalTime.now()).append("\n");
            res.append("logged in");
        } else if (Time.isDate(req.split(" ")[0]) && Time.isDate(req.split(" ")[1])) {
            passed(logs, res, req, id);
        } else if (Time.isDateTime(req.split(" ")[0]) && Time.isDateTime(req.split(" ")[1])) {
            passed(logs, res, req, id);
        } else {
            logs.append("logged out\n").append("=== ").append(id).append(" log end ===\n");
            serverLog.append(id).append(" logged out at ").append(LocalTime.now()).append("\n");
            res.append(req.equals("bye and log transfer") ? logs : "logged out");
            channelLogMap.remove(client);
        }
        return res;
    }

    public void startServer() {
        serverThread = new Thread(() -> {
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.socket().bind(new InetSocketAddress(host, port));
                serverChannel.configureBlocking(false);

                Selector selector = Selector.open();
                serverChannel.register(selector, OP_ACCEPT);

                while (!serverThread.isInterrupted()) {

                    selector.select();

                    Set<SelectionKey> keys = selector.selectedKeys();

                    for (Iterator<SelectionKey>it = keys.iterator(); it.hasNext(); ) {

                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isValid()) {

                            if (key.isAcceptable()) {
                                accept(key);

                            } else if (key.isReadable()) {

                                SocketChannel client = (SocketChannel) key.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(2048);
                                client.read(buffer);
                                String req = new String(buffer.array()).trim();
                                String res = requestHandler(client, req).toString();
                                ByteBuffer byteBuffer = Charset.forName("ISO-8859-2").encode(res);
                                client.write(byteBuffer);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    public void stopServer() {
        serverThread.interrupt();
    }

    public String getServerLog() {
        return serverLog.toString();
    }
}
