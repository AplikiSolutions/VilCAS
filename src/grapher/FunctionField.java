/*
 * Copyright (C) 2015 Perttu
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
 */
package grapher;

import java.awt.event.*;
import java.awt.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.border.*;

public class FunctionField extends JPanel{
    
    private static final ButtonGroup activeButtonGroup;
    private static ScriptEngine engine;
    
    private final JTextField field;
    private Color color;
    
    static{
        activeButtonGroup = new ButtonGroup();
    }
    
    public FunctionField(String s){
        this.color = Color.red;
        
        setBorder(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.gray));
        panel.setBackground(Color.white);
        
        JTextField infoText = new JTextField(" y = ");
        infoText.setEditable(false);
        infoText.setBorder(null);
        infoText.setFocusable(false);
        infoText.setOpaque(false);
        panel.add(infoText);
        
        field = new JTextField(s, 30);
        field.setBorder(null);
        field.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                Window.repaint();
            }
        });
        panel.add(field);
        
        JRadioButton activeButton = new JRadioButton();
        activeButtonGroup.add(activeButton);
        activeButton.setFocusable(false);
        activeButton.setOpaque(false);
        activeButton.setBorder(null);
        activeButton.addActionListener((ActionEvent e) -> {
            if(activeButton.isSelected())
                Graph.setActive(this);
        });
        activeButton.setSelected(true);
        Graph.setActive(this);
        panel.add(activeButton);
        
        JButton colorChooser = new JButton("");
        colorChooser.setBorder(new EmptyBorder(5, 5, 5, 5));
        colorChooser.setBackground(color);
        colorChooser.addActionListener((ActionEvent e) -> {
            Color c = JColorChooser.showDialog(this, "Choose color", color);
            
            if(c != null)
                this.color = c;
            
            colorChooser.setBackground(c);
            Window.repaint();
        });
        panel.add(colorChooser);
        
        JButton removeButton = new JButton(" X ");
        removeButton.setBorder(new LineBorder(Color.gray));
        removeButton.setFocusable(false);
        removeButton.addActionListener((ActionEvent e) -> {
            Window.remove(this);
        });
        panel.add(removeButton);
        
        add(panel);
    }//FunctionField
    
    
    public void draw(Graph graph, Graphics g){
        
        g.setColor(color);
        
        double lasty = 0;

        for(int x = -1; x < graph.getWidth(); x ++){

            double y = graph.yToPanel(getValue(graph.xToGraph(x)));
            
            if(Double.isNaN(y))
                continue;
            
            //don't draw if outside of panel
            if(graph.contains(x - 1, (int)Math.round(lasty)) || 
                    graph.contains(x, (int)Math.round(y)))
                g.drawLine(x - 1, (int)Math.round(lasty), x, (int)Math.round(y));

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
    
    public Color getColor(){
        return color;
    }//getColor
    
    public void setColor(Color c){
        color = c;
    }
    
    public static void init(){
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
    }//init
}
