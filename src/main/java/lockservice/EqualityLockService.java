package lockservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class EqualityLockService implements Service {
    private final Service service;
    private final Map<Object, ReentrantLock> map = new ConcurrentHashMap<>();

    public EqualityLockService(Service service) {
        this.service = service;
    }

    @Override
    public void run(Object o) {
        ReentrantLock lock = getLock(o);
        try {
            service.run(o);
        } finally {
            if (!lock.hasQueuedThreads()) map.remove(o);
            lock.unlock();
        }
    }

    private ReentrantLock getLock(Object o) {
        while (true) {
            ReentrantLock lock = map.computeIfAbsent(o, obj -> new ReentrantLock());
            try {
                lock.lock();
            } finally {
                if (!map.containsValue(lock)) lock.unlock();
                else return lock;
            }
        }
    }
}
