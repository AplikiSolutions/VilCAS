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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Window {
    
    private static final JPanel fieldPanel;
    private static final Graph graph;
    
    static{
        graph = new Graph();
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
    }
    
    public void start(){
        
        JFrame frame = new JFrame("Grapher");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 15, 10, 15));
        panel.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        
        JButton resetViewButton = new JButton("reset view");
        resetViewButton.setFocusable(false);
        resetViewButton.addActionListener((ActionEvent e) -> {
            graph.resetView();
        });
        topPanel.add(resetViewButton);
        
        JButton addFunctionButton = new JButton("add function");
        addFunctionButton.addActionListener((ActionEvent e) -> {
            addFunction(new FunctionField("x*x"));
        });
        topPanel.add(addFunctionButton);
        
        panel.add(topPanel, BorderLayout.PAGE_START);
        
        graph.setOpaque(false);
        panel.add(graph, BorderLayout.CENTER);
        
        
        FunctionField field = new FunctionField("Math.sin(x)");
        addFunction(field);
        panel.add(fieldPanel, BorderLayout.PAGE_END);
        
        
        frame.add(panel);
        
        frame.setSize(600, 500);
        frame.setMinimumSize(new Dimension(475, 400));
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
        
        
    }//start
    
    public static void addFunction(FunctionField f){
        fieldPanel.add(f);
        fieldPanel.revalidate();
        graph.addFunction(f);
    }//addFunction
    
    public static void remove(FunctionField f){
        fieldPanel.remove(f);
        fieldPanel.revalidate();
        graph.remove(f);
    }//remove
    
    public static void repaint(){
        graph.repaint();
    }//repaint
    
    
}
