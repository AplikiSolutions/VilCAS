/*
* Copyright (C) 2015 Apliki Solutions Nyman & Yli-Opas
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
* 
* 
* Full GNU GPL can be found in LICENSE.txt
* 
* If this code is reused, this header must tell if the code
* is modified or not.
* 
* All changes to the code must be distinguishable from
* the original code.
*/

package grapher;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Graph extends JPanel{
    
    private double scaleX, scaleY;
    //offset is in panel's coordinate system
    private int offsetX, offsetY, mouseX = 0, mouseY = 0;
    private boolean resetView = true;
    private static ScriptEngine engine;
    
    public Graph(){
        setBackground(Color.white);
        
        Dragger dragger = new Dragger();
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
        addMouseWheelListener(dragger);
    }//graph
    
    public void resetView(){
        resetView = true;
        repaint();
    }//resetView
    
    @Override
    public void paint(Graphics g){
        
        if(resetView){
            resetView = false;
            offsetX = getWidth() / 2;
            offsetY = getHeight() / 2;
            scaleX = 0.01;
            scaleY = -0.01;
        }
        
        //clear background
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        try{
            engine.put("x", 0);
            engine.eval(Window.getEquation());
            
        }catch(Exception e){
            drawAxes(g);
            
            return;
        }
        
        g.setColor(Color.red);
        
        double lasty = 0;

        for(int i = -1; i < getWidth(); i += 2){
            engine.put("x", xToGraph(i));

            double y = 0;
            try{
                y = yToPanel(((Number)(engine.eval(Window.getEquation()))).doubleValue());
            }catch(Exception e){
                break;
            }
            
            if(contains(i - 1, (int)Math.round(lasty)) || contains(i, (int)Math.round(y)))
                g.drawLine(i - 1, (int)Math.round(lasty), i, (int)Math.round(y));

            lasty = y;
        }
        
        try{
            engine.put("x", xToGraph(mouseX));
            double yExact = ((Number)(engine.eval(Window.getEquation()))).doubleValue();
            int y = (int)yToPanel(yExact);
            
            g.setColor(Color.gray);
            g.drawLine(mouseX, mouseY, mouseX, y);
            g.drawLine(mouseX, y, (int)xToPanel(0), y);
            
            g.drawString("(" + round(xToGraph(mouseX)) + ", " + round(yExact) + ")",
                    mouseX + 5, mouseY);
        }catch(Exception ex){}
        
        
        drawAxes(g);
        
    }//drawGraph
    
    public void drawAxes(Graphics g){
        //draw axes
        g.setColor(Color.black);
        g.drawLine((int)xToPanel(0), 0, (int)xToPanel(0), getHeight());
        g.drawLine(0, (int)yToPanel(0), getWidth(), (int)yToPanel(0));
        
        g.setColor(Color.gray);
        
        //draw y-values
        double logY = Math.log10(-scaleY) + 2; // + 2 sets frequency
        int exponent = (int)Math.floor(logY); //rounds down
        
        int multiplier = 1;
        if(Math.abs(logY - exponent) > 0.7)
            multiplier = 5;
        else if(Math.abs(logY - exponent) > 0.3)
            multiplier = 2;
        
        
        double gap = multiplier * Math.pow(10, exponent); //rounds scale to closest 1, 2 or 5
        
        double y = (int)(yToGraph(0) / gap) * gap; //gives first gap-rounded value on panel
        
        while(yToPanel(y) < getHeight()){
            
            if(Math.abs(y) < gap / 2){ //dont draw zero
            }else if(xToPanel(0) < 0){
                //draws value to left wall
                g.drawLine(0, (int)yToPanel(y), 5, (int)yToPanel(y));
                g.drawString(round(y), 8, (int)yToPanel(y) + 5);
                
            }else if(xToPanel(0) > getWidth()){
                //draws value to rigth wall
                g.drawLine(getWidth(), (int)yToPanel(y), getWidth() - 5, (int)yToPanel(y));
                g.drawString(round(y), 
                        (int)(getWidth() - getFontMetrics(getFont()).getStringBounds(round(y), g).getWidth() - 8), 
                        (int)yToPanel(y) + 5);
                
            }else{
                //draws value next to axis
                g.drawLine((int)xToPanel(0) - 2, (int)yToPanel(y), (int)xToPanel(0) + 2, (int)yToPanel(y));
                g.drawString(round(y), (int)xToPanel(0) + 5, (int)yToPanel(y) + 5);
            }
            
            y -= gap; //go to next y-value
        }
        
        
        //draw x-values
        double logX = Math.log10(scaleX) + 2; // + 2 sets frequency
        exponent = (int)Math.floor(logX); //rounds down
        
        
        multiplier = 1;
        if(Math.abs(logX - exponent) > 0.7)
            multiplier = 5;
        else if(Math.abs(logX - exponent) > 0.3)
            multiplier = 2;
        
        gap = multiplier * Math.pow(10, exponent); //rounds scale to closest 1, 2 or 5
        
        double x = (int)(xToGraph(0) / gap) * gap; //gives first gap-rounded value on panel
        
        while(xToPanel(x) < getWidth()){
            
            if(Math.abs(x) < gap / 2){ //dont draw zero
            }else if(yToPanel(0) < 0){
                //draws value to left wall
                g.drawLine((int)xToPanel(x), 0, (int)xToPanel(x), 5);
                g.drawString(round(x), 20, (int)xToPanel(x) - 5);
                
            }else if(yToPanel(0) > getHeight()){
                //draws value to rigth wall
                g.drawLine((int)xToPanel(x), getHeight(), (int)xToPanel(x), getHeight() - 5);
                g.drawString(round(x), (int)(getHeight() - 8), (int)xToPanel(x) - 5);
                
            }else{
                //draws value next to axis
                g.drawLine((int)xToPanel(x), (int)yToPanel(0) - 2, (int)xToPanel(x), (int)yToPanel(0) + 2);
                g.drawString(round(x), (int)xToPanel(x) - 5, (int)yToPanel(0) - 5);
            }
            
            x += gap; //go to next y-value
        }
        
    }
    
    private String round(double d){
//        final int accuracy = 3;
//        
//        double e = accuracy - 1 - (int)Math.log10(Math.abs(d));
//        
//        return Math.round(d * Math.pow(10, e)) / Math.pow(10, e);
        
        
        DecimalFormat f;
        if(Math.abs(d) < 1000 && Math.abs(d) > 0.01)
            f = new DecimalFormat("##0.###");
        else
            f = new DecimalFormat("0.##E0");
        
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(dfs);
        
        return f.format(d);
    }
    
    private double xToGraph(double x){
        return (x - offsetX) * scaleX;
    }
    
    private double yToGraph(double y){
        return (y - offsetY) * scaleY;
    }
    
    private double xToPanel(double x){
        return x / scaleX + offsetX;
    }
    
    private double yToPanel(double y){
        return y / scaleY + offsetY;
    }
    
    private class Dragger extends MouseAdapter{
        
        int lastX = 0, lastY = 0;
        
        @Override
        public void mouseDragged(MouseEvent e){
            offsetX += e.getX() - lastX;
            offsetY += e.getY() - lastY;
            lastX = e.getX();
            lastY = e.getY();
            mouseX = e.getX();
            mouseY = e.getY();
            
            repaint();
        }
        
        @Override
        public void mouseMoved(MouseEvent e){
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            lastX = e.getX();
            lastY = e.getY();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            double k = Math.pow(1.2, e.getUnitsToScroll() / 3);
            
            //zoom in respect to middle point of mouse and origin
            offsetX += (1 - 1 / k) * (e.getX() - offsetX);
            offsetY += (1 - 1 / k) * (e.getY() - offsetY);
            
            scaleX *= k;
            scaleY *= k;
            
            repaint();
        }
    }
    
    public static void init(){
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
    }//init
    
}
