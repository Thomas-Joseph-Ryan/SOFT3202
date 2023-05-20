package org.example;

public class VectorRenderer extends Renderer{

    @Override
    public void render_Circle(Double radius) {
        System.out.printf("Drawing a vector circle with radius %s", radius);
    }

    @Override
    public void render_Rectangle(Double width, Double height) {
        System.out.printf("Drawing a vector rectangle with width %s and height %s", width, height);
    }
}
