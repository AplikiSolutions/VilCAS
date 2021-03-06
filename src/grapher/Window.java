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
    
    private static JFrame frame;
    private static JPanel fieldPanel, backPanel;
    private static Graph graph;
    private static JScrollPane scrollPane;
    private static boolean graphOpen = false;
    
    public void start(){
        
        if(graphOpen){
            JOptionPane.showMessageDialog(null, "Grapher is already open");
            return;
        }
        
        graphOpen = true;
        
        frame = new JFrame("Grapher");
        
        backPanel = new JPanel();
        backPanel.setBorder(new EmptyBorder(0, 15, 10, 15));
        backPanel.setLayout(new BorderLayout());
        
        graph = new Graph();
        graph.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setFocusable(false);
        
        JButton resetViewButton = new JButton("reset view");
        resetViewButton.setFocusable(false);
        resetViewButton.addActionListener((ActionEvent e) -> {
            graph.resetView();
        });
        JPanel midp = new JPanel();
        midp.setLayout(new BorderLayout());
        midp.add(resetViewButton, BorderLayout.LINE_START);
        midp.setOpaque(false);
        graph.add(midp, BorderLayout.PAGE_START);
        
        JButton addFunctionButton = new JButton("add function");
        addFunctionButton.addActionListener((ActionEvent e) -> {
            addFunction(new FunctionField("x*x"));
        });
        topPanel.add(addFunctionButton);
        
        backPanel.add(topPanel, BorderLayout.PAGE_START);
        
        
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setMinimumSize(new Dimension(400, 125));
        
        JPanel midpanel = new JPanel();
        midpanel.add(fieldPanel);
        
        scrollPane = new JScrollPane(midpanel);
        backPanel.add(scrollPane, BorderLayout.CENTER);
        
        
        frame.add(backPanel);
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                close();
            }
        });
        
        frame.setSize(500, 300);
        frame.setMinimumSize(new Dimension(500, 200));
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
        
        initFunctionOptions();
        
        FunctionField.setGraph(graph);
        FunctionField field = new FunctionField("Math.sin(x)");
        addFunction(field);
        
    }//start
    
    public static void addFunction(FunctionField f){
        fieldPanel.add(f);
        scrollPane.revalidate();
        graph.addFunction(f);
    }//addFunction
    
    public static void initFunctionOptions(){
        backPanel.add(new FunctionOptions(), BorderLayout.PAGE_END);
        backPanel.revalidate();
    }//initFunctionOptions
    
    public static void remove(FunctionField f){
        fieldPanel.remove(f);
        scrollPane.revalidate();
        graph.remove(f);
    }//remove
    
    public static void repaint(){
        if(graph != null)
            graph.repaint();
    }//repaint
    
    public static boolean graphOpen(){
        return graphOpen;
    }//graphOpen
    
    public static void close(){
        graphOpen = false;
        graph.close();
        frame.dispose();
    }//close
    
}
