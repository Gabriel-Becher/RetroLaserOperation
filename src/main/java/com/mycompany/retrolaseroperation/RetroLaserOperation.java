/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.retrolaseroperation;

import javax.swing.JFrame;

/**
 *
 * @author gabri
 */
public class RetroLaserOperation {

    public static void main(String[] args) {
        JFrame window = new JFrame("Retro Laser Operation");
        
        GamePanel game = new GamePanel();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setResizable(false);
        
        window.add(game);
        
        window.pack();
        
        window.setLocationRelativeTo(null);
        
        window.setVisible(true);
        
        game.requestFocusInWindow();        
    }
}
