package org.example;

public class Main {
    public static void main(String[] args) {
        VectorRenderer vectorRenderer = new VectorRenderer();
        RasterRenderer rasterRenderer = new RasterRenderer();

        Shape[] shapes = {new Circle(vectorRenderer, 4.2),
                new Rectangle(vectorRenderer, 10.0, 20.0),
                new Circle(rasterRenderer, 5.0),
                new Rectangle(rasterRenderer, 4.0, 7.0)
        };

        for (Shape shape : shapes) {
            shape.draw();
        }
    }
}