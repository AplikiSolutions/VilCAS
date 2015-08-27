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
    
    private static JTextField field;
    
    public void start(){
        
        JFrame frame = new JFrame("Grapher");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        
        
        JPanel infoPanel = new JPanel();
        
        JTextField infoText = new JTextField(" y = ");
        infoText.setEditable(false);
        infoText.setBorder(null);
        infoText.setFocusable(false);
        infoPanel.add(infoText);
        
        Graph graph = new Graph();
        graph.setOpaque(false);
        panel.add(graph, BorderLayout.CENTER);
        
        
        field = new JTextField("Math.sin(x)", 30);
        field.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                graph.repaint();
            }
        });
        infoPanel.add(field);
        
        JButton resetViewButton = new JButton("reset view");
        resetViewButton.addActionListener((ActionEvent e) -> {
            graph.resetView();
        });
        infoPanel.add(resetViewButton);
        panel.add(infoPanel, BorderLayout.PAGE_END);
        
        
        frame.add(panel);
        
        frame.setSize(600, 500);
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
        
        
    }//start
    
    
    public static String getEquation(){
        return field.getText();
    }
    
}
