import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.scheredin.SMO.Request;

import java.util.concurrent.PriorityBlockingQueue;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        PriorityBlockingQueue<Request> requestsIndex = new PriorityBlockingQueue<>();
        requestsIndex.add(new Request(1,1,123));
        requestsIndex.add(new Request(1,1,231));
        requestsIndex.add(new Request(1,1,1));
        while (!requestsIndex.isEmpty()) {
            System.out.println(requestsIndex.poll().getBufferInsertedTime());
        }
    }
}
