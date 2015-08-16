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

package trianglesolver;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Window {
    
    //TODO: make the picture a real-time view
    
    public void start(){
        JFrame frame = new JFrame("Triangle solver");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //top-tier layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        BufferedImage image = null;
        try(InputStream stream = getClass().getResourceAsStream("/trianglesolver/Triangle.png")){
            image = ImageIO.read(stream);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Window: " + e);
        }
        JLabel imageView = new JLabel(new ImageIcon(image));
        panel.add(imageView, BorderLayout.PAGE_START);
        
        //layout containing textfields and calculate-button
        JPanel p = new JPanel();
        
        //panels containing textFields
        CustomField[] fields = new CustomField[6];
        String[] names = {"a", "b", "c", "A", "B", "C"};
        
        for(int i = 0; i < 3; i++){
            JPanel p2 = new JPanel();
            p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
            
            fields[i] = new CustomField(names[i]);
            p2.add(fields[i]);
            
            fields[i+3] = new CustomField(names[i+3]);
            p2.add(fields[i+3]);
            
            p.add(p2);
        }
        
        JButton button = new JButton("Calculate");
        button.addActionListener((ActionEvent e) ->{
            
            double[] values = new double[6];
            int counter = 0;
            
            for(int i = 0; i < fields.length; i++){
                if(fields[i].isSelected()){
                    values[i] = fields[i].getValue();
                    counter++;
                }else{
                    values[i] = Double.NaN;
                }
            }
            
            if(counter >= 2){
                //solve
                double[] answers = Solver.solve(values);
                for(int i = 0; i < answers.length; i++){
                    fields[i].setValue(answers[i]);
                }
            }else{
                JOptionPane.showMessageDialog(null, "You have to give more information");
            }
        });
        p.add(button);
        
        panel.add(p, BorderLayout.CENTER);
        frame.add(panel);
        
        frame.pack();
        frame.setResizable(false);
        
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
    }//start
    
}
