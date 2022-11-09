package ru.scheredin.SMO;

import ru.scheredin.SMO.stats.AutoModeStats;
import ru.scheredin.SMO.stats.Snapshot;
import ru.scheredin.SMO.stats.StepModeStats;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Round round = new Round(5, 5, 0.1, 10, 3, 3);
        Orchestrator.INSTANCE().runRound(round);
        AutoModeStats.INSTANCE().getCourierResults();
    stepLocalMode();
    }

    private static void stepLocalMode() {
        Iterator<Snapshot> iterator = StepModeStats.INSTANCE().iterator();
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
