/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Tools {

    public static Options createOptionsFromYaml(String fileName) throws Exception {
        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(Paths.get(fileName));
        Map<String, Object> data = yaml.load(inputStream);

        String host = (String) data.get("host");
        int port = (int) data.get("port");
        boolean concurMode = (boolean) data.get("concurMode");
        boolean showSendRes = (boolean) data.get("showSendRes");
        Map<String, List<String>> clientsMap = (Map<String, List<String>>) data.get("clientsMap");

        return new Options(host, port, concurMode, showSendRes, clientsMap);
    }
}
