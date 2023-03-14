package Tutorial3.Mocking;

public class MyDependencyImpl implements MyDependency{

    private final String someString;

    public MyDependencyImpl(String someString) {
        this.someString = someString;
    }

    public String getString() {
        return this.someString;
    }

    public String getStringParam(boolean b) {
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public void hello() {
        System.out.println("hello");
    }

    @Override
    public Boolean getTruth() {
        return true;
    }
}
