package Tutorial3.Mocking;

public class MyClass {
    private MyDependency dependency;

    public MyClass(MyDependency dependency) {
        this.dependency = dependency;
    }

    public String doSomething() {
        this.dependency.hello();
        this.dependency.getTruth();


        if (this.dependency.getString() == null) {
            return "hello";
        } else {
            return "goodbye";
        }

    }

    public void doSomethingElse() {
        this.dependency.getStringParam(true);

    }
}
