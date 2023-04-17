package au.edu.sydney.soft3202.tutorials.week8.uow;

public class Document {
    private int id;
    private SlowDatabase backingDatabase;
    private String contents;

    private UowDriver uowDriver;

    public Document(int id, SlowDatabase backingDatabase, UowDriver uowDriver) {
        this.id = id;
        this.backingDatabase = backingDatabase;
        this.uowDriver = uowDriver;
    }

    public int getID() {
        return this.id;
    }

    public String getContents() {
        System.out.println("Getting contents");
        return backingDatabase.getDocumentContents(this.id);
    }

    public void setContents(String contents) {
        System.out.println("Setting contents");
        this.contents = contents;
        this.uowDriver.addTransaction(this.id, this.contents);
    }

    public void appendToContents(String append) {
        System.out.println("Appending to contents");
        this.contents = contents + append;
        this.uowDriver.addTransaction(this.id, this.contents);
    }
}
