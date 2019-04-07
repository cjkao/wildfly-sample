package sample;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.opentracing.Traced;
@Traced
@Stateless
@Slf4j
public class UploadListener {
    private static final String PATH = "../uploads/";


    @Asynchronous
    public void uploadFile(@Observes Part file) {
        log.info( "Start upload file: " + System.currentTimeMillis());
        try (InputStream input = file.getInputStream()) {
            TimeUnit.MILLISECONDS.sleep(500);
            checkDirectory();
            String fileName = PATH + file.getSubmittedFileName();
            Files.copy(input, new File(fileName).toPath());
            log.info( "End upload file: " + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            log.error( e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the directory if not exists
     *
     * @throws IOException
     */
    private static void checkDirectory() throws IOException {
        Path path = Paths.get(PATH);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }
}
