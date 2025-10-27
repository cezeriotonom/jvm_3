package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    private final List<Balloon> balloons = new ArrayList<>();
    private int score = 0;
    private final Random random = new Random();
    private Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Iterate backwards to safely remove items while iterating
                for (int i = balloons.size() - 1; i >= 0; i--) {
                    Balloon b = balloons.get(i);
                    if (b.contains(e.getPoint())) {
                        balloons.remove(i);
                        score += 10;
                        break; // Pop one balloon at a time
                    }
                }
            }
        });
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
        long timer = System.currentTimeMillis();
        int frames = 0;
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
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
            
            // Control spawn rate
            balloonSpawnCounter++;
            if (balloonSpawnCounter > 60) { // Spawn roughly every second
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
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            b.update();
            if (b.y < -b.radius) { // Remove if off-screen
                balloons.remove(i);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Balloon b : balloons) {
            b.draw(g2d);
        }

        // Draw Score
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + score, 10, 30);
    }
}
