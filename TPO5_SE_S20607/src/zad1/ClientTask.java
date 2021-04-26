/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientTask implements Runnable {

    public static ClientTask create(Client c, List<String> reqList, boolean showRes) {
        return new ClientTask() {
            @Override
            public void run() {
                c.connect();
                c.send("login " + c.getId());
                for(String req : reqList) {
                    String res = c.send(req);
                    if (showRes) System.out.println(res);
                }
                String clog = c.send("bye and log transfer");
                System.out.println(clog);
            }
        };
    }

    public String get() throws InterruptedException, ExecutionException {
        return "";
    }

    @Override
    public void run() {

    }
}
