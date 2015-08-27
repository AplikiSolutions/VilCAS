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

public class Window extends JPanel{
    
    private static double[] values = new double[6];
    private static CustomField[] fields;
    private static JPanel imageView;
    
    public void start(){
        
        for(int i = 0; i < 3; i++){
            values[i] = 1;
            values[i+3] = 60;
        }
        
        JFrame frame = new JFrame("Triangle solver");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //top-tier layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        imageView = new Window();
        panel.add(imageView, BorderLayout.CENTER);
        
        //layout containing textfields and calculate-button
        JPanel p = new JPanel();
        
        //panels containing textFields
        fields = new CustomField[6];
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
            
            
        });
        p.add(button);
        
        panel.add(p, BorderLayout.PAGE_END);
        frame.add(panel);
        
        frame.setSize(500, 450);
        frame.setResizable(false);
        
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.setVisible(true);
    }//start
    
    
    public static void calculate(){
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
                values = Solver.solve(values);
                for(int i = 0; i < values.length; i++){
                    fields[i].setValue(values[i]);
                }
                
                imageView.repaint();
            }else{
                JOptionPane.showMessageDialog(null, "You have to give more information");
            }
    }
    
    
    @Override
    public void paint(Graphics g){
        int ax = 75, ay = getHeight() - 50, bx = getWidth() - 75, by = getHeight() - 50;
        double scale = (bx - ax) / values[0];
        
        g.clearRect(0, 0, getWidth(), getHeight());
        
        double cx1 = values[1] * Math.cos(Math.PI / 180 * values[5]);
        double cy1 = values[1] * Math.sin(Math.PI / 180 * values[5]);
        
        double cx2 = values[2] * Math.cos(Math.PI / 180 * values[4]);
        double cy2 = values[2] * Math.sin(Math.PI / 180 * values[4]);
        
        ax = (int)(getWidth() / 2 - scale * values[0] / 2);
        bx = (int)(getWidth() / 2 + scale * values[0] / 2);
        
        while(!contains(ax + (int)(cx1 * scale), ay - (int)(cy1 * scale)) ||
                !contains(bx - (int)(cx2 * scale), by - (int)(cy2 * scale))){
            scale /= 1.1;
            
            ax = (int)(getWidth() / 2 - scale * values[0] / 2);
            bx = (int)(getWidth() / 2 + scale * values[0] / 2);
        }
        
        
        g.setColor(Color.black);
        g.drawLine(ax, ay, bx, by);
        g.drawLine(ax, ay, ax + (int)(cx1 * scale), ay - (int)(cy1 * scale));
        g.drawLine(bx, by, bx - (int)(cx2 * scale), by - (int)(cy2 * scale));
        
        
        
    }//paint
    
}
