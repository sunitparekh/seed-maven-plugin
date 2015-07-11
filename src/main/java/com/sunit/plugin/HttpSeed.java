package com.sunit.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Mojo(name = "http")
public class HttpSeed extends AbstractMojo {

    @Parameter( property = "seed.data.dir", defaultValue = "./seed" )
    private String seedDataDir;

    @Parameter( property = "config.file.name", defaultValue = "config.json" )
    private String configFileName;

    public void execute() throws MojoExecutionException {
        try {
            Files.walkFileTree(Paths.get(seedDataDir), new HttpPostFileVisitor(configFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
