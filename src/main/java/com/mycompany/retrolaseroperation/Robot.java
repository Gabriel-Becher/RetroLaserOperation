/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.retrolaseroperation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author gabri
 */
public class Robot {
    protected double x;
    
    private double cannonAngle;
    private double recoil = 0;
    
    public Robot(double x){
        this.x = x;
    }
    
    private Shape createRobot(){
        GeneralPath g = new GeneralPath();
        g.append(new Rectangle2D.Double(20,20,20,20), true);
        return g;
    
    }
    
    public void draw(Graphics2D g2d){
        AffineTransform original = g2d.getTransform();
        
        g2d.translate(x, 0);
        
        Shape robot = createRobot();
        
        g2d.setStroke(new BasicStroke(3f));
        g2d.setColor(Color.BLACK);
        g2d.draw(robot);
    
        g2d.setTransform(original);
        
    }
    
}
