package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class ApiLauncher {
    private static final URI BASE_URI = URI.create("http://0.0.0.0:8081/api/v1/");

    public static HttpServer startServer() {
        ResourceConfig config = ResourceConfig.forApplication(new RestConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, config);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Jarvis Systems Online.");
        System.out.println("Smart Campus API is running at http://localhost:8081/api/v1");
        System.out.println("Press Enter to stop the server...");
        System.in.read();
        server.shutdownNow();
    }
}