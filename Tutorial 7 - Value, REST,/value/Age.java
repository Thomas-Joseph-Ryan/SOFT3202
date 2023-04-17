package au.edu.sydney.soft3202.tutorials.week8.value;

import java.util.Objects;

public final class Age {
    private final int value;

    public Age(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Age birthday() {
        return new Age(this.value + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Age)) {
            return false;
        }

        Age other = (Age) obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
