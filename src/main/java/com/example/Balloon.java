package com.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Balloon {
    public int x, y, radius;
    public Color color;
    private int speed;

    public Balloon(int x, int y, int radius, Color color, int speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.speed = speed;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean contains(Point p) {
        return p.distance(x, y) <= radius;
    }
}
