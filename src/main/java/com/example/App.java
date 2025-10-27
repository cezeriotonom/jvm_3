package com.example;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SymDev AI Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(800, 600));

            JLabel label = new JLabel("Hello SymDev AI", SwingConstants.CENTER);
            label.setFont(new Font("Serif", Font.BOLD, 32));

            frame.getContentPane().add(label, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null); // Center the window on the screen
            frame.setVisible(true);
        });
    }
}
