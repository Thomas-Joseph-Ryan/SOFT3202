package au.edu.sydney.soft3202.tutorials.week8.uow;

public class MainDriver {
    public static void main(String[] args) {
        SlowDatabase database = new SlowDatabase();
        UowDriver uowDriver = new UowDriver(database);

        Document first = new Document(0, database, uowDriver);
        first.setContents("First document");
        Document second = new Document(1, database, uowDriver);
        second.setContents("Second document");
        Document third = new Document(2, database, uowDriver);
        third.setContents("Third document");
        Document fourth = new Document(3, database, uowDriver);
        fourth.setContents("Fourth document");

        second.appendToContents(" I am the best document");

        third.setContents("I seriously am the third document");

        second.appendToContents(". Any other documents are not as good as me");

        fourth.appendToContents(" second document is so up itself.");

        System.out.println("First document contents:\n" + first.getContents());
        System.out.println("Second document contents:\n" + second.getContents());
        System.out.println("Third document contents:\n" + third.getContents());
        System.out.println("Fourth document contents:\n" + fourth.getContents());

        // Use this here
        uowDriver.commit();

        System.out.println("First document contents:\n" + first.getContents());
        System.out.println("Second document contents:\n" + second.getContents());
        System.out.println("Third document contents:\n" + third.getContents());
        System.out.println("Fourth document contents:\n" + fourth.getContents());
    }
}
