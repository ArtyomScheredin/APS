package ru.scheredin.SMO;

import one.nio.http.HttpServer;
import one.nio.http.HttpServerConfig;
import one.nio.server.AcceptorConfig;
import ru.scheredin.SMO.api.AutoModeController;
import ru.scheredin.SMO.api.MainController;
import ru.scheredin.SMO.api.StepModeController;
import ru.scheredin.SMO.stats.AutoModeStatsService;
import ru.scheredin.SMO.stats.BuyerStats;
import ru.scheredin.SMO.stats.Snapshot;
import ru.scheredin.SMO.stats.StepModeStatsService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public final class Application {

    public static void main(String[] args) throws IOException {
        int port = 19666;
        String url = "http://localhost:" + port;
        Round round = new Round(5, 5, 0.1, 10, 12, 3);
        Orchestrator.INSTANCE().runRound(round);
        ArrayList<Snapshot> snapshots = StepModeStatsService.INSTANCE().getSnapshots();
        HttpServer server = new HttpServer(configFromPort(port));
        server.addRequestHandlers(new StepModeController());
        server.addRequestHandlers(new AutoModeController());
        server.addRequestHandlers(new MainController());
        server.start();
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
    private static void autoLocalMode() {
        List<BuyerStats> buyersResults = AutoModeStatsService.INSTANCE().getBuyersResults();
        System.out.println(BuyerStats.getHeader());
        buyersResults.forEach(System.out::println);
    }

    private static void stepLocalMode() {
        Iterator<Snapshot> iterator = StepModeStatsService.INSTANCE().iterator();
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
