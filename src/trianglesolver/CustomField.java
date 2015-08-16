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

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


public class CustomField extends JPanel{
    
    private final String name;
    private final JTextField field;
    private final JCheckBox checkBox;
    private static boolean first = true;

    public CustomField(String name){
        super();
        
        this.name = name;
        field = new JTextField("", 4);
        field.setBorder(null);
        field.addFocusListener(new Selector());
        
        first = true;
        field.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                if(!first)
                    checkBox.setSelected(true);
                else
                    first = false;
            }
            @Override
            public void focusLost(FocusEvent e){
                checkBox.setSelected(!field.getText().isEmpty());
            }
        });
        checkBox = new JCheckBox();
        checkBox.setBorder(null);
        checkBox.setFocusable(false);
        checkBox.setBackground(Color.white);
        
        BufferedImage imageLocked = null, imageOpen = null;
        try(InputStream stream = getClass().getResourceAsStream("/trianglesolver/lockClosed.png")){
            imageLocked = ImageIO.read(stream);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "CustomField: " + e);
        }
        try(InputStream stream = getClass().getResourceAsStream("/trianglesolver/lockOpen.png")){
            imageOpen = ImageIO.read(stream);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "CustomField: " + e);
        }
        
        checkBox.setIcon(new ImageIcon(imageOpen));
        checkBox.setSelectedIcon(new ImageIcon(imageLocked));
        
        JPanel p = new JPanel();
        p.setBackground(Color.white);
        
        p.add(field);
        p.add(checkBox);
        
        JTextField info = new JTextField(" " + name + ":");
        info.setFocusable(false);
        info.setBorder(null);
        info.setEditable(false);
        info.setFont(info.getFont().deriveFont(Font.BOLD));
        
        add(info);
        add(p);
    }
    
    public boolean isSelected(){
        return checkBox.isSelected();
    }
    
    @Override
    public String getName(){
        return name;
    }

    public double getValue(){
        if(!field.getText().isEmpty())
            return Double.parseDouble(field.getText());
        return Double.NaN;
    }
    
    public void setValue(double d){
        if(!Double.isNaN(d))
            field.setText(Double.toString(d));
        else
            field.setText("");
    }
    
    private class Selector extends FocusAdapter{
        @Override
        public void focusGained(FocusEvent e){
            JTextField field = (JTextField)e.getComponent();
            field.selectAll();
        }
    }//selector
    
}
