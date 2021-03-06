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

package vilcas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.util.*;

public class Constants{
    
    public static float g = 9.81f, G = 6.674e-11f;
    
    private JPanel p;
    
    private ArrayList<Constant> list = new ArrayList();
    
    public void showEditor(){
        JFrame frame = new JFrame("Constants");
        
        //vertical top tier layout
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        
        //panel in scrollView, shows constants
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        //middle panel to pack p together
        JPanel midPanel = new JPanel();
        midPanel.add(p);
        midPanel.setBackground(Color.white);
        
        //actual scrollable view
        JScrollPane scrollPane = new JScrollPane(midPanel);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
        
        
        //add all known constants
        VilCAS.constants.keySet().stream().forEach((s) -> {
            list.add(new Constant(s, Float.toString(VilCAS.constants.get(s))));
        });
        //sort and add to panel
        refresh();
        
        //info text
        panel.add(info("    Create new:", null));
        
        //make new constant
        JPanel p2 = new JPanel();
        p2.setBorder(null);
        
        JTextField nameField = new JTextField("", 3);
        nameField.requestFocusInWindow();
        p2.add(nameField);
        
        p2.add(info("=", null));
        
        JTextField valueField = new JTextField("", 6);
        p2.add(valueField);
        
        //add the constant
        JButton addButton = new JButton("Add");
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.gray), new EmptyBorder(2, 2, 2, 2)));
        addButton.addActionListener((ActionEvent e) -> {
            Constant c = new Constant("const_" + nameField.getText(), valueField.getText());
            nameField.setText("");
            valueField.setText("");
            list.add(c);
            refresh();
        });
        p2.add(addButton);
        
        panel.add(p2);
        
        //exit buttons
        p2 = new JPanel();
        
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        p2.add(cancel);
        
        JButton save = new JButton("Save");
        save.addActionListener((ActionEvent e) -> {
            save(frame);
        });
        p2.add(save);
        panel.add(p2);
        
        //configure frame
        frame.add(panel);
        frame.setSize(250, 300);
        frame.setResizable(false);
        
        //ask for saving when exiting
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                int ans = JOptionPane.showConfirmDialog(frame, 
                        "Do you want to save changes?",
                        "Save query", JOptionPane.YES_NO_CANCEL_OPTION);
                if(ans == JOptionPane.YES_OPTION){
                    save(frame);
                }else if(ans == JOptionPane.NO_OPTION){
                    frame.dispose();
                }
                
            }
        });
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //center frame
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth()/2), 
                (int)(screen.getHeight() / 2 - frame.getHeight()/2));
        frame.setVisible(true);
        
    }//showEditor
    
    
    private void save(JFrame f){
        VilCAS.constants = new HashMap();
        list.stream().forEach((c) -> {
            VilCAS.constants.put(c.getName(), c.getValue());
        });
        f.dispose();
    }//save
    
    
    private void refresh(){
        list.sort((Object a, Object b) -> ((Constant)a).name.compareTo(((Constant)b).name));
        
        p.removeAll();
        list.stream().forEach((c) -> {
            p.add(c);
        });
        
        p.revalidate();
    }//refresh
    
    
    private JTextField info(String text, Border border){
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(border);
        field.setFocusable(false);
        
        return field;
    }//info
    
    
    
    private class Constant extends JPanel implements Comparable{
        private final String name;
        private final JTextField field;
        
        public Constant(String name, String value){
            super();
            this.name = name;
        
            setBackground(Color.white);
            
            //textfield for name and "="
            JTextField f = new JTextField(name + " = ", 6);
            f.setHorizontalAlignment(JTextField.RIGHT);
            f.setBackground(Color.white);
            f.setBorder(null);
            f.setEditable(false);
            f.setFocusable(false);
            add(f);
            
            field = new JTextField(value, 6);
            add(field);
            
            JButton deleteButton = new JButton(" del ");
            deleteButton.setBorder(new EmptyBorder(2, 2, 2, 2));
            deleteButton.setFocusable(false);
            
            deleteButton.addActionListener((ActionEvent e) -> {
                //confirm deletion
                int ans = JOptionPane.showConfirmDialog(null, 
                        "Do you want to delete \"" + name + "\"?",
                        "Delete constant", JOptionPane.YES_NO_OPTION);
                if(ans == JOptionPane.YES_OPTION){
                    list.remove(Constant.this);
                    refresh();
                }
            });
            add(deleteButton);
        }//init
        
        public float getValue(){
            return Float.parseFloat(field.getText() + "f");
        }//getValue
        
        @Override
        public String getName(){
            return name;
        }//getName
        
        @Override
        public int compareTo(Object o){
            return name.compareTo(((Constant)o).name);
        }
    }//Constant
}
