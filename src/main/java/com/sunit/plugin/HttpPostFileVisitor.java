package com.sunit.plugin;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

public class HttpPostFileVisitor extends SimpleFileVisitor<Path> {

    private String configFileName;
    Logger logger;

    public HttpPostFileVisitor(String configFileName){
        this.configFileName = configFileName;
        logger = Logger.getAnonymousLogger();
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        if (path.getFileName().toString().equals(configFileName)) return FileVisitResult.CONTINUE;

        logger.info(String.format("Seeding file: %s ", path));
        try {
            HttpResponse response = Request.Post(getPostUri(path))
                    .bodyFile(path.toFile(), ContentType.APPLICATION_JSON)
                    .execute().returnResponse();
            logger.info(response.getStatusLine().toString());
        } catch (Exception e) {
            logger.warning(String.format("Could not seed file: %s ", path));
        }
        return FileVisitResult.CONTINUE;
    }

    private String getPostUri(Path path) throws IOException {
        File file = Paths.get(path.getParent().toAbsolutePath().toString(), configFileName).toFile();
        return JsonPath.read(file,"$.postUrl");
    }
}
