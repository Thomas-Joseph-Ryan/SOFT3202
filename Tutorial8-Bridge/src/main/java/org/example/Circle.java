package org.example;

public class Circle extends Shape {

    private Double radius;

    public Circle(Renderer renderer, Double radius) {
        super(renderer);
        this.radius = radius;
    }

    public void draw() {
        this.renderer.render_Circle(this.radius);
    }
}
