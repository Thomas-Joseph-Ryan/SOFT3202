
import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestSuite {

    private Main main;

    @BeforeAll
    public void createMain() {
        main = new Main();
    }

    @Test
    public void testNoOptionsNoCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = null;
        Double c2Y = null;
        Double c2R = null;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertFalse(value);
    }

    @Test
    public void testNoOptionsCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 6.0;
        Double c2X = null;
        Double c2Y = null;
        Double c2R = null;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertTrue(value);
    }

    @Test
    public void testAllOptionsNoCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 12.0;
        Double c2Y = 12.0;
        Double c2R = 3.0;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertFalse(value);
    }

    @Test
    public void testAllOptionsCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 6.0;
        Double c2Y = 6.0;
        Double c2R = 4.0;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertTrue(value);
    }

    @Test
    public void testNoRadiusOptionNoCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 10.0;
        Double c2Y = 10.0;
        Double c2R = null;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertFalse(value);
    }

    @Test
    public void testNoRadiusOptionCollision(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 7.0;
        Double c2Y = 7.0;
        Double c2R = null;
        boolean value = main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        Assertions.assertFalse(value);
    }

    @Test
    public void testIllegalOptions1(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = null;
        Double c2Y = 4.0;
        Double c2R = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        }) ;
    }

    @Test
    public void testIllegalOptions2(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 4.0;
        Double c2Y = null;
        Double c2R = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        }) ;
    }

    @Test
    public void testIllegalOptions3(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = null;
        Double c2Y = 4.0;
        Double c2R = 4.0;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        }) ;
    }

    @Test
    public void testIllegalOptions4(){
        /*
        Test the basic case for one ball against bounds
         */

        double bX = 20.0;
        double bY = 20.0;
        double c1X = 5.0;
        double c1Y = 5.0;
        double c1R = 3.0;
        Double c2X = 4.0;
        Double c2Y = null;
        Double c2R = 4.0;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.checkCollision(bX, bY, c1X, c1Y, c1R, c2X, c2Y, c2R);
        }) ;
    }

    @ParameterizedTest(name = "{index} => boundsX={0}, boundsY={1}, circleOneX={2}, circleOneY={3}, circleOneRadius={4}, circleTwoX={5}, circleTwoY={6}, circleTwoRadius={7}")
    @MethodSource("provideInvalidInputValues")
    @DisplayName("Should throw IllegalArgumentException for negative input values")
    void testIllegalArgumentExceptionNegativeValues(double boundsX, double boundsY,
                                             double circleOneX, double circleOneY, double circleOneRadius,
                                             Double circleTwoX, Double circleTwoY, Double circleTwoRadius) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.checkCollision(boundsX, boundsY, circleOneX, circleOneY, circleOneRadius, circleTwoX, circleTwoY, circleTwoRadius);
        });
    }

    private static List<Object[]> provideInvalidInputValues() {
        return Arrays.asList(
                new Object[]{-1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0},
                new Object[]{1.0, -2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0},
                new Object[]{1.0, 2.0, -3.0, 4.0, 5.0, 6.0, 7.0, 8.0},
                new Object[]{1.0, 2.0, 3.0, -4.0, 5.0, 6.0, 7.0, 8.0},
                new Object[]{1.0, 2.0, 3.0, 4.0, -5.0, 6.0, 7.0, 8.0},
                new Object[]{1.0, 2.0, 3.0, 4.0, 5.0, -6.0, 7.0, 8.0},
                new Object[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, -7.0, 8.0},
                new Object[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, -8.0}
        );
    }

    /*
    Final test would be :
    If either circleOne or circleTwo overlap the bounds when a
    circle-to-circle check is being performed an IllegalStateException
    is thrown.
     */

}
