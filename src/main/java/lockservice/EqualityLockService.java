package lockservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class EqualityLockService implements Service {
    private final Service service;
    private Map<Object, ReentrantLock> map = new ConcurrentHashMap<>();

    public EqualityLockService(Service service) {
        this.service = service;
    }

    @Override
    public void run(Object o) {
        ReentrantLock lock;
        while (true) {
            lock = map.computeIfAbsent(o, obj -> new ReentrantLock());
            try {
                lock.lock();
            } finally {
                if (!map.containsValue(lock)) lock.unlock();
                else break;
            }
        }
        try {
            service.run(o);
        } finally {
            if (lock.getQueueLength() == 0) map.remove(o);
            lock.unlock();
        }
    }
}
