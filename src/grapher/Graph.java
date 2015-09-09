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
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class Graph extends JPanel{
    
    
    public double scaleX, scaleY;
    //offset is in panel's coordinate system
    private int offsetX, offsetY, mouseX = 0, mouseY = 0;
    private boolean resetView = true;
    private final ArrayList<FunctionField> functions = new ArrayList();
    
    private final JFrame frame;
    
    public Graph(){
        setBackground(Color.white);
        
        Dragger dragger = new Dragger();
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
        addMouseWheelListener(dragger);
        
        frame = new JFrame("Graph");
        setOpaque(false);
        frame.add(this);
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                Window.close();
            }
        });
        
        frame.setSize(500, 500);
        frame.setMinimumSize(new Dimension(300, 300));
        
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth()), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
    }//graph
    
    public void resetView(){
        resetView = true;
        repaint();
    }//resetView
    
    @Override
    public void paint(Graphics g){
        //clear background
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if(resetView){
            resetView = false;
            offsetX = getWidth() / 2;
            offsetY = getHeight() / 2;
            scaleX = 0.01;
            scaleY = -0.01;
        }
        
        //draw functions
        functions.stream().forEach((f) -> {
            f.draw(g);
        });
        
        //draw value of active function from mouse
        if(FunctionField.isInitialized())
            try{
                double yExact = FunctionOptions.getActive().getSavedValue(mouseX);
                int y = (int)yToPanel(yExact);

                g.setColor(Color.gray);
                g.drawLine(mouseX, mouseY, mouseX, y);
                g.drawLine(mouseX, mouseY, mouseX, (int)yToPanel(0));
                g.drawLine(mouseX, y, (int)xToPanel(0), y);

                g.drawString("(" + format(xToGraph(mouseX)) + ", " + format(yExact) + ")",
                        mouseX + 5, mouseY);
            }catch(Exception ex){}
        
        drawAxes(g);
        
        super.paint(g);
    }//drawGraph
    
    
    public void drawAxes(Graphics g){
        
        
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
            
            int lineX, textX;
            if(xToPanel(0) < 5){//draws value to top wall
                lineX = 0;
                textX = 8;
                
            }else if(xToPanel(0) > getWidth() - 5){//draws value to bottom wall
                lineX = getWidth() - 5;
                textX = (int)(getWidth() - stringWidth(format(y)) - 8);
                
            }else{//draws value next to axis
                lineX = (int)xToPanel(0) - 3;
                textX = (int)xToPanel(0) + 5;
            }
            
            if(Math.abs(y) > gap / 2){ //dont draw zero
                //draw value
                g.drawLine(lineX, (int)yToPanel(y), lineX + 5, (int)yToPanel(y));
                g.drawString(format(y), textX, (int)yToPanel(y) + stringHeight(format(y)) / 2 - 1);
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
            
            int lineY, textY;
            if(yToPanel(0) < 5){//draws value to top wall
                lineY = 0;
                textY = 8 + stringHeight(format(x));
                
            }else if(yToPanel(0) > getHeight() - 5){//draws value to bottom wall
                lineY = getHeight() - 5;
                textY = (int)(getHeight() - 8);
                
            }else{//draws value next to axis
                lineY = (int)yToPanel(0) - 3;
                textY = (int)yToPanel(0) - 5;
            }
            
            if(Math.abs(x) > gap / 2){ //dont draw zero
                //draw value
                g.drawLine((int)xToPanel(x), lineY, (int)xToPanel(x), lineY + 5);
                g.drawString(format(x), (int)(xToPanel(x) - stringWidth(format(x)) / 2), textY);
            }
            
            x += gap; //go to next x-value
        }
        
        
        //draw axes
        g.setColor(Color.black);
        g.drawLine((int)xToPanel(0), 0, (int)xToPanel(0), getHeight());
        g.drawLine(0, (int)yToPanel(0), getWidth(), (int)yToPanel(0));
    }//drawAxes
    
    
    private int stringWidth(String s){
        return (int)getFontMetrics(getFont()).getStringBounds(s, getGraphics()).getWidth();
    }//stringWidth
    
    private int stringHeight(String s){
        return (int)getFontMetrics(getFont()).getStringBounds(s, getGraphics()).getHeight();
    }//stringHeight
    
    public String format(double d){
        DecimalFormat f;
        if(Math.abs(d) < 1000 && Math.abs(d) > 0.01)
            f = new DecimalFormat("0.####");
        else
            f = new DecimalFormat("0.####E0");
        
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(dfs);
        
        return f.format(d);
    }//format
    
    
    public double xToGraph(double x){
        return (x - offsetX) * scaleX;
    }//xToGraph
    public double yToGraph(double y){
        return (y - offsetY) * scaleY;
    }//yToGraph
    public double xToPanel(double x){
        return x / scaleX + offsetX;
    }//xToPanel
    public double yToPanel(double y){
        return y / scaleY + offsetY;
    }//yToPanel
    
    
    public void addFunction(FunctionField f){
        functions.add(f);
        repaint();
    }//addFunction
    
    public void remove(FunctionField f){
        functions.remove(f);
        if(!functions.isEmpty()){
            functions.get(functions.size() - 1).setActive();
            FunctionOptions.setActive(functions.get(functions.size() - 1));
        }
    }//addFunction
    
    public void close(){
        frame.dispose();
    }//close
    
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
        }//mouseDragged
        
        @Override
        public void mouseMoved(MouseEvent e){
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }//mouseMoved
        
        @Override
        public void mousePressed(MouseEvent e){
            lastX = e.getX();
            lastY = e.getY();
        }//mousePressed
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            double k = Math.pow(1.2, e.getUnitsToScroll() / 3);
            
            //zoom in respect to middle point of mouse and origin
            offsetX += (1 - 1 / k) * (e.getX() - offsetX);
            offsetY += (1 - 1 / k) * (e.getY() - offsetY);
            
            scaleX *= k;
            scaleY *= k;
            
            repaint();
        }//mouseWheelMoved
    }//Dragger
    
}
