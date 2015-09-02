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

import java.awt.event.*;
import java.awt.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.border.*;

public class FunctionField extends JPanel{
    
    public static final int NONE = 0, DERIVE = 1, INTEGRATE = 2;
    
    private static final Color[] colors = {Color.red, Color.green, Color.blue, Color.orange, Color.magenta};
    private static int colorIndex = 0;
    private static boolean initialized = false;
    private static final ButtonGroup activeButtonGroup;
    private static ScriptEngine engine;
    private static Graph graph;
    
    private final JTextField field;
    private final JRadioButton activeButton;
    private Color color;
    private int diff;
    private double[] values;
    
    static{
        activeButtonGroup = new ButtonGroup();
    }
    
    public static void setGraph(Graph g){
        graph = g;
    }
    
    public FunctionField(String s){
        this.color = colors[colorIndex];
        colorIndex++;
        if(colorIndex >= colors.length)
            colorIndex = 0;
        
        diff = NONE;
        
        setBorder(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.gray));
        panel.setBackground(Color.white);
        
        //active category chooser
        activeButton = new JRadioButton();
        activeButtonGroup.add(activeButton);
        activeButton.setFocusable(false);
        activeButton.setOpaque(false);
        activeButton.setBorder(null);
        activeButton.addActionListener((ActionEvent e) -> {
            if(activeButton.isSelected())
                FunctionOptions.setActive(this);
        });
        activeButton.setSelected(true);
        FunctionOptions.setActive(this);
        panel.add(activeButton);
        
        JTextField infoText = new JTextField(" f(x) =");
        infoText.setEditable(false);
        infoText.setBorder(null);
        infoText.setFocusable(false);
        infoText.setOpaque(false);
        panel.add(infoText);
        
        //the field for the function
        field = new JTextField(s, 30);
        field.setBorder(null);
        field.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                Window.repaint();
            }
        });
        field.addFocusListener(new FocusListener(){
        @Override
            public void focusGained(FocusEvent e){
                activeButton.setSelected(true);
                FunctionOptions.setActive(FunctionField.this);
            }
            @Override
            public void focusLost(FocusEvent e){}
        });
        
        panel.add(field);
        
        add(panel);
    }//FunctionField
    
    
    
    
    public void draw(Graphics g){
        
        values = new double[graph.getWidth()];
        
        g.setColor(color);
        
        
        if(diff != INTEGRATE){

            for(int x = 0; x < graph.getWidth(); x++){
                
                if(diff == NONE)
                    values[x] =  getValue(graph.xToGraph(x));
                else//diff == DERIVE
                    values[x] =  (getValue(graph.xToGraph(x)) - getValue(graph.xToGraph(x-1))) / (-graph.scaleY);
            }
            
        }else{//diff == INTEGRATE
            
            double y = 0;//graph's coordinate system
            double increment;
            
            //positive direction
            for(int x = (int)graph.xToPanel(0); x < graph.getWidth(); x++){
                increment = getValue(graph.xToGraph(x)) * (-graph.scaleY);
                if(!Double.isFinite(increment))
                    increment = 0;
                
                y += increment;
                
                if(x >= 0)
                    values[x] = y;
            }
            
            y = 0;
            //negative direction
            for(int x = (int)graph.xToPanel(0); x >= 0; x--){
                increment = getValue(graph.xToGraph(x)) * (-graph.scaleY);
                if(!Double.isFinite(increment))
                    increment = 0;
                
                y -= increment;
                
                if(x < graph.getWidth())
                    values[x] = y;
            }
            
        }
        
        int lasty = (int)Math.round(graph.yToPanel(values[0]));
        for(int x = 1; x < values.length; x++){
            
            int y = (int)Math.round(graph.yToPanel(values[x]));
            
            //don't draw if outside of panel
            if(graph.contains(x - 2, lasty) || graph.contains(x - 1, y))
                if(!Double.isNaN(y) && !Double.isNaN(lasty))
                    g.drawLine(x - 1, lasty, x, y);
            
            lasty = y;
        }
        
    }//draw
    
    
    public double getValue(double x){
        try{
            engine.put("x", x);
            return ((Number)(engine.eval(field.getText()))).doubleValue();
        }catch(Exception e){
            return Double.NaN;
        }
    }//getValue
    
    public double getSavedValue(int x){
        return values[x];
    }//getSavedValue
    public String solve(){
        
        int sign = 1;
        
        if(values[0] < 0)
            sign = -1;
            
        for(int x = 1; x < values.length; x++){
            if(sign * values[x] < 0){
                double d = graph.xToGraph(x - Math.abs(values[x]  / (values[x-1] + values[x])));
                
                double m = Math.pow(10, (int)-Math.log10(graph.scaleX) + 2);
                
                return Double.toString(((int)d * m) / m);
            }
        }
        
        return "NaN";
    }//solve
    
    public Color getColor(){
        return color;
    }//getColor
    public void setColor(Color c){
        color = c;
    }//setColor
    
    public void setDiff(int i){
        diff = i;
    }//setDiff
    public int getDiff(){
        return diff;
    }//getDiff
    
    public void setActive(){
        activeButton.setSelected(true);
    }//setActive
    
    public static boolean isInitialized(){
        return initialized;
    }//isInitialized
    
    public static void init(){
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        
        initialized = true;
        
        if(Window.graphOpen())
            Window.repaint();
    }//init
}
