package lockservice;

public interface Service {
    void run(Object o);

    public static void main(String[] args) {
        Service s = null;
        s.run(1);
        s.run(2);
        s.run("fsdfs");

        s.run(2);
        s.run(2);
    }
}
