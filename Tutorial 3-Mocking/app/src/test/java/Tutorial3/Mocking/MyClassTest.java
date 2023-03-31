package Tutorial3.Mocking;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MyClassTest {

    @Test
    void myTest() {
        MyDependencyImpl mockDependencyImpl = mock(MyDependencyImpl.class);
        MyClass myClass = new MyClass(mockDependencyImpl);
        myClass.doSomething();
        verify(mockDependencyImpl, times(1)).getString();
        verify(mockDependencyImpl).getTruth();
        verify(mockDependencyImpl).hello();

        when(mockDependencyImpl.getString()).thenReturn(null);
        assertEquals(myClass.doSomething(), "hello");
        when(mockDependencyImpl.getString()).thenReturn("string");
        assertEquals(myClass.doSomething(), "goodbye");
    }

    @Test
    void testingDifferentPathsTrue() {
        MyDependencyImpl mockDependencyImpl = mock(MyDependencyImpl.class);
        MyClass myClass = new MyClass(mockDependencyImpl);
        when(mockDependencyImpl.getString()).thenReturn(null);
        assertEquals(myClass.doSomething(), "hello");
    }

    @Test
    void testingDifferentPathsWithBooleanTrue() {
        MyDependencyImpl mockDependencyImpl = mock(MyDependencyImpl.class);
        MyClass myClass = new MyClass(mockDependencyImpl);
        when(mockDependencyImpl.getStringParam(true)).thenReturn("true");
        myClass.doSomethingElse();
        verify(mockDependencyImpl).getStringParam(true);
    }
}
