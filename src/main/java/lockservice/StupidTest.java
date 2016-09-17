package lockservice;


import java.util.ArrayList;
import java.util.List;

/**
 * Тест дебила на то, что ничего не ложится с NPE, или еще какой гадостью
 * Created by asind on 17.09.2016.
 */
public class StupidTest {
    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objects.add(new Object());
        }
        Service service = o -> {
            int sum = 0;
            for (int i = 0; i < 100_000_000; i++) {
                sum += i;
            }
            System.err.println(Thread.currentThread().getName() + " " + sum);
        };
        EqualityLockService equalityLockService = new EqualityLockService(service);
        for (int i = 0; i < 50; i++) {
            final int ii = i;
            Object randObj = objects.get((int) (Math.random() * objects.size()));
            Thread thread = new Thread(() -> equalityLockService.run(randObj), "Thread num " + ii + " " + randObj.toString());
            thread.start();
        }
    }
}
