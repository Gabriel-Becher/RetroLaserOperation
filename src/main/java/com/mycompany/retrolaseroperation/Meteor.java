/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.retrolaseroperation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 *
 * @author gabri
 */
public class Meteor {

    private double x;
    private double y = 50;

    private double dx;
    private double dy;

    private double size;

    public Meteor(int level) {

        size = Math.random() * 30 + 20;

        x = Math.random() * (GamePanel.WIDTH - size);

        dx = Math.random() * 1.5;

        if (Math.random() <= 0.5) {
            dx *= -1;
        }

        dy = Math.min(1 + Math.random() * level,15);
}

    public void draw(Graphics2D g2d) {
        
    //fundo
    GeneralPath background = new GeneralPath();

    double centerX = x + size / 2;
    double centerY = y + size / 2;

    double tipX = centerX;
    double tipY = y - size * 1.3;

    background.moveTo(
            centerX,
            y + size * 1.2
    );

    background.quadTo(
            x - size,
            y + size,
            tipX,
            tipY
    );

    background.quadTo(
            x + 2*size,
            y + size,
            centerX,
            y + size * 1.2
    );

    background.closePath();

    double angle = -dx * 0.5;

    /*
     * Limita para não girar demais.
     */
    double maxAngle = Math.toRadians(180);

    angle = Math.max(
            -maxAngle,
            Math.min(maxAngle, angle)
    );

    AffineTransform rotation =
            AffineTransform.getRotateInstance(
                    angle,
                    centerX,
                    centerY
            );

    Shape rotatedBackground =
            rotation.createTransformedShape(
                    background
            );

    g2d.setColor(Color.YELLOW);
    g2d.fill(rotatedBackground);

    // corpo do meteoro
    Shape body = new Ellipse2D.Double(x, y,size, size );

    g2d.setColor(Color.decode("#b97a57"));
    g2d.fill(body);

    g2d.setColor(Color.BLACK);
    g2d.draw(body);

    // cratera pequena superior
    Shape hole1 = new Ellipse2D.Double(
            x + size * 0.55,
            y + size * 0.18,
            size * 0.20,
            size * 0.08
    );

    // cratera vertical esquerda
    Shape hole2 = new Ellipse2D.Double(
            x + size * 0.20,
            y + size * 0.35,
            size * 0.08,
            size * 0.18
    );

    // cratera grande inferior
    Shape hole3 = new Ellipse2D.Double(
            x + size * 0.42,
            y + size * 0.58,
            size * 0.35,
            size * 0.25
    );

    g2d.setColor(Color.decode("#3f271b"));

    g2d.fill(hole1);
    g2d.fill(hole2);
    g2d.fill(hole3);
}

    public void update() {

        x += dx;
        y += dy;
    }

    public boolean isOutside() {

        return (x > GamePanel.WIDTH)||(x<0);
    }
    
    public boolean isShot(double mouseX, double mouseY){
        double centerX = x+size/2;
        double centerY = y+size/2;
        
        double localdx = centerX - mouseX;
        double localdy = centerY - mouseY;
        
        double radius = size/2;
        
        return ((localdx*localdx)+(localdy*localdy)<=(radius*radius));
    }
    
    public boolean hit(){
        
        return y>(GamePanel.HEIGHT+size/2);

    }
}