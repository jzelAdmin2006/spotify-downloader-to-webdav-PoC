package tech.bison.trainee;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

@Service
public class SpotifyService {
    private final ExecutorService downloadExecutor;
    private final ExecutorService uploadExecutor;
    private final Terminal terminal = new Terminal();

    public SpotifyService(ExecutorService downloadExecutor, ExecutorService uploadExecutor) {
        this.downloadExecutor = downloadExecutor;
        this.uploadExecutor = uploadExecutor;
    }

    public void downloadTrack(String trackId) {
        downloadExecutor.submit(() -> {
            downloadTrackWithDeemix(trackId);
            uploadExecutor.submit(this::uploadToWebDav);
        });
    }

    private void downloadTrackWithDeemix(String trackId) {
        terminal.execute(String.format("deemix --portable -b FLAC -p ./tmp https://open.spotify.com/track/%s", trackId));
    }

    private void uploadToWebDav() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("./tmp"))) {
            stream.forEach(filePath -> {
                new Terminal().execute(String.format("rclone copy \"%s\" MYWEBDAV:/", filePath));
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

