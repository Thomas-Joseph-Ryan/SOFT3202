import java.lang.reflect.Executable;


public class PerformanceTesting {

    public static void main(String[] args) {
        StringTester stringTester = new StringTester();

        Worker[] workers = new Worker[] {
            new Worker() {
              @Override
              public void work() {stringTester.strConcat1("hello", "world");}},
            new Worker() {
                @Override
                public void work() {stringTester.strConcat2("hello", "world");}},

            new Worker() {
                @Override
                public void work() {
                    stringTester.strConcat3("hello", "world");
                }
            }
        };

        for (Worker worker : workers) {
            long start = System.nanoTime();
            worker.work();
            long end = System.nanoTime();
            System.out.println(end - start);
        }
    }
}

interface Worker {
    void work();
}

class StringTester {
    public void strConcat1(String first, String second) {
        System.out.println(first + second);
    }

    public void strConcat2(String first, String second) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(first);
        stringBuilder.append(second);
        System.out.println(stringBuilder);

    }

    public void strConcat3(String first, String second) {
        System.out.printf("%s %s", first, second);
    }
}
