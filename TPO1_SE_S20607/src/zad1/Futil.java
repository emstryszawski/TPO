package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardOpenOption.*;

public class Futil extends SimpleFileVisitor<Path> {

    private static final Charset CP_1250 = Charset.forName("Cp1250");
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final Futil instance = new Futil();
    private static FileChannel fileChannelWriter;

    private Futil() {

    }

    public static void processDir(String dirName, String resultFileName) {
        Path readPath = Paths.get(dirName),
                writePath = Paths.get(resultFileName);
        try {
            fileChannelWriter = FileChannel.open(writePath, CREATE, TRUNCATE_EXISTING, WRITE);
            Files.walkFileTree(readPath, instance);
        } catch (IOException e) {
            System.err.println("I/O Exception");
        }
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {

        Files.walk(path)
                .filter(Files::isRegularFile)
                .forEach(p -> {
                    try (FileChannel fileChannelReader = FileChannel.open(path)) {

                        ByteBuffer buffer = ByteBuffer.allocate((int) fileChannelReader.size());
                        fileChannelReader.read(buffer);
                        buffer.flip();
                        CharBuffer decode = CP_1250.decode(buffer);
                        fileChannelWriter.write(UTF_8.encode(decode));

                    } catch (IOException e) {
                        System.err.println("I/O Exception");
                    }
                });
        return CONTINUE;
    }
}
