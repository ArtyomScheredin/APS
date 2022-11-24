package ru.scheredin.SMO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import one.nio.http.HttpServer;
import one.nio.http.HttpServerConfig;
import one.nio.http.HttpSession;
import one.nio.http.Request;
import one.nio.http.Response;
import one.nio.server.AcceptorConfig;
import ru.scheredin.SMO.api.AutoModeController;
import ru.scheredin.SMO.api.MainController;
import ru.scheredin.SMO.api.StepModeController;
import ru.scheredin.SMO.dto.Round;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.dto.BuyerStats;
import ru.scheredin.SMO.dto.Snapshot;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.services.SnapshotService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public final class Application {

    public static void main(String[] args) throws IOException {
        int port = 19666;
        String url = "http://localhost:" + port;

        Injector injector = Guice.createInjector(new Config());
        MainController mainController = injector.getInstance(MainController.class);
        StepModeController stepModeController = injector.getInstance(StepModeController.class);
        AutoModeController autoModeController = injector.getInstance(AutoModeController.class);
        HttpServer server = new HttpServer(configFromPort(port)){
            @Override
            public void handleDefault(Request request, HttpSession session) throws IOException {
                //For CORS issues
                 if (request.getMethod() == Request.METHOD_OPTIONS) {
                    Response response = new Response(Response.ACCEPTED, Response.EMPTY);
                    response.addHeader("Access-Control-Allow-Origin: *");
                    response.addHeader("Access-Control-Allow-Headers: *");
                    response.addHeader("Access-Control-Allow-Methods: GET, OPTIONS, HEAD, PUT, POST");
                    session.sendResponse(response);
                    return;
                }
                super.handleDefault(request, session);
            }

        };
        server.addRequestHandlers(mainController);
        server.addRequestHandlers(stepModeController);
        server.addRequestHandlers(autoModeController);
        server.start();
        System.out.println("APS_SMO-server started on port 19666");
    }

    private static HttpServerConfig configFromPort(int port) {
        HttpServerConfig httpConfig = new HttpServerConfig();
        AcceptorConfig acceptor = new AcceptorConfig();
        acceptor.port = port;
        acceptor.reusePort = true;
        httpConfig.acceptors = new AcceptorConfig[]{acceptor};
        return httpConfig;
    }


    //Для локального дебага
    private static void autoLocalMode(AutoModeStatsService autoModeStatsService) {
        List<BuyerStats> buyersResults = autoModeStatsService.getBuyersResults();
        System.out.println(BuyerStats.getHeader());
        buyersResults.forEach(System.out::println);
    }

    private static void stepLocalMode(SnapshotService snapshotService) {
        Iterator<Snapshot> iterator = snapshotService.iterator();
        try (Scanner scanner = new Scanner(new InputStreamReader(System.in))) {
            //for (int i = 0; i < 50; i++) {
            //   iterator.next();
            //}
            while (iterator.hasNext()) {
                //scanner.next();
                Snapshot snapshot = iterator.next();
                System.out.println(snapshot.toString());
            }
        }
    }
}
