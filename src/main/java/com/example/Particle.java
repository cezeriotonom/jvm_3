package com.example;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    private double x, y;
    private double vx, vy;
    private Color color;
    private int lifespan; // in frames

    public Particle(double x, double y, double vx, double vy, Color color, int lifespan) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.color = color;
        this.lifespan = lifespan;
    }

    public void update() {
        x += vx;
        y += vy;
        vy += 0.1; // A little gravity effect
        lifespan--;
    }

    public void draw(Graphics2D g2d) {
        // Fade out effect
        int alpha = (int) (255 * (lifespan / 60.0));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;
        
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g2d.fillRect((int) x, (int) y, 3, 3);
    }

    public boolean isAlive() {
        return lifespan > 0;
    }
}
