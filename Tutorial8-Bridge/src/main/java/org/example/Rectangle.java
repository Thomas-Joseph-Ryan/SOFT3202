package org.example;

public class Rectangle extends Shape {

    private final Double height;
    private final Double width;

    public Rectangle(Renderer renderer, Double width, Double height) {
        super(renderer);
        this.width = width;
        this.height = height;
    }

    public void draw() {
        this.renderer.render_Rectangle(this.width, this.height);
    }
}
