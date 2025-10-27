package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel implements Runnable {

    private final List<Balloon> balloons = new CopyOnWriteArrayList<>();
    private final List<Particle> particles = new CopyOnWriteArrayList<>();
    private int score = 0;
    private final Random random = new Random();
    private Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Balloon b : balloons) {
                    if (b.contains(e.getPoint())) {
                        createExplosion(b.x, b.y, b.color);
                        balloons.remove(b);
                        score += 10;
                        break; 
                    }
                }
            }
        });
    }

    private void createExplosion(int x, int y, Color color) {
        int particleCount = 30;
        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double speed = 2 + random.nextDouble() * 4;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            int lifespan = 30 + random.nextInt(30);
            particles.add(new Particle(x, y, vx, vy, color, lifespan));
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int balloonSpawnCounter = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            repaint();

            balloonSpawnCounter++;
            if (balloonSpawnCounter > 60) { 
                 spawnBalloon();
                 balloonSpawnCounter = 0;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void spawnBalloon() {
        int radius = 20 + random.nextInt(30);
        int x = random.nextInt(getWidth() - radius * 2) + radius;
        int y = getHeight() + radius;
        Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        int speed = 2 + random.nextInt(4);
        balloons.add(new Balloon(x, y, radius, color, speed));
    }

    private void update() {
        for (Balloon b : balloons) {
            b.update();
            if (b.y < -b.radius) { 
                balloons.remove(b);
            }
        }

        for (Particle p : particles) {
            p.update();
            if (!p.isAlive()) {
                particles.remove(p);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Modern Gradient Background
        GradientPaint gp = new GradientPaint(0, 0, new Color(26, 37, 76), 0, getHeight(), new Color(10, 10, 40));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (Balloon b : balloons) {
            b.draw(g2d);
        }

        for (Particle p : particles) {
            p.draw(g2d);
        }

        // Draw Score with a more visible color
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        g2d.drawString("Score: " + score, 10, 30);
    }
}
