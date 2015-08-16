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

package vililaskin.categories;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class Variable extends JPanel{
    public Field text;
    JButton button;

    public Variable(String name, boolean selectable, Equation parent){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);

        //infotext
        JTextField f = new JTextField("  " + name);
        f.setEditable(false);
        f.setBorder(null);
        f.setFocusable(false);
        f.setFont(new Font("arial", 1, 12));

        text = new Field(name, 7);
        text.setOpaque(false);
        text.addFocusListener(new Selector());
        text.setBorder(null);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBackground(Color.white);
        p.setBorder(null);
        p.add(text);

        if(selectable){
            button = new JButton(" = ");
            button.setBorder(new EmptyBorder(2, 2, 2, 2));
            button.setFocusable(false);
            button.addActionListener(parent.buttonListener);
            p.add(button);
        }else{
            text.setBorder(new EmptyBorder(2, 2, 2, 2));
        }

        add(f);

        //p2 fixes a weird two-pixel-edge bug
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        p2.add(p);
        add(p2);
    }

    public class Field extends JTextField{
        
        public String name;
        
        public Field(String _name, int size){
            super("", size);
            
            name = _name;
        }
    }//Field
    
    private class Selector extends FocusAdapter{
        @Override
        public void focusGained(FocusEvent e){
            JTextField field = (JTextField)e.getComponent();
            field.selectAll();
        }
    }//selector
}