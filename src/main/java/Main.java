import ru.scheredin.Request;

import java.util.concurrent.PriorityBlockingQueue;

public class Main {
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
