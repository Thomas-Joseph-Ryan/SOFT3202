package au.edu.sydney.soft3202.task1;

import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class SampleBenchmark {

    ShoppingBasket sut;

    @Setup(Level.Iteration)
    public void setUp() {
        ShoppingBasket.resetInstance();
        sut = ShoppingBasket.getInstance("A");
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void addItemBenchmark() {
        sut.addItem("apple", 1);
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void addItemNameBenchmark() {
        sut.insertNewItem("coconut", 3.2);
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void removeItemNameBenchmark() {
        sut.deleteExistingItem("apple");
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void removeItemBenchmark(RemoveItemState state) {
        state.sut.removeItem("pear", 1);
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void createAddRemoveBenchmark() {
        sut.insertNewItem("coconut", 3.2);
        for (int i = 0; i < 10; i++) {
            sut.addItem("coconut", 1);
        }
        sut.deleteExistingItem("coconut");
    }




}
