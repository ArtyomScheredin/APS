package ru.scheredin.SMO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.scheredin.SMO.stats.AutoModeStats;
import ru.scheredin.SMO.stats.Snapshot;
import ru.scheredin.SMO.stats.StepModeStats;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Round round = new Round(5, 5, 1, 10, 3, 50);
        Orchestrator.INSTANCE().runRound(round);
        Iterator<Snapshot> iterator = StepModeStats.INSTANCE().iterator();
        try (Scanner scanner = new Scanner(new InputStreamReader(System.in))) {

            while (iterator.hasNext()) {

                Snapshot snapshot = iterator.next();
                System.out.println(snapshot.toString());
            }
        }
        //AutoModeStats.INSTANCE().getResults();
    }
}
