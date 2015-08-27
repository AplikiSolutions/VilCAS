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

package vilcas.scripter;


import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import vilcas.CategoryChooser;
import vilcas.VilCAS;


public class Scripter{
    
    JPanel panel;
    JFrame frame;
    String name, desc, category, names[], functions[];
    boolean[] selectable;
    int numberOfVars;
    
    public void makeScript() throws Exception{
        
        frame = new JFrame("Scripter");
        
        //vertical top tier layout
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        //info text
        panel.add(info("Category", null));
        
        //get category names
        String[] categoryNames = new String[VilCAS.categories.size()];
        for(int i = 0; i < VilCAS.categories.size(); i++)
            categoryNames[i] = VilCAS.categories.get(i).name;
        
        //Category chooser
        JComboBox catField = new JComboBox(categoryNames);
        catField.setEditable(true);
        panel.add(catField);
        
        //info text
        panel.add(info("Name your function", new EmptyBorder(10, 0, 0, 0)));
        
        JTextField nameField = new JTextField("", 10);
        panel.add(nameField);
        
        //info text
        panel.add(info("Give it a tooltip", new EmptyBorder(10, 0, 0, 0)));
        
        JTextField descField = new JTextField("", 10);
        panel.add(descField);
        
        //info text
        panel.add(info("How many variables are there?", new EmptyBorder(10, 0, 0, 0)));
        
        JTextField numberField = new JTextField("", 2);
        numberField.addActionListener((ActionEvent e) -> {
            try{
                
                name = nameField.getText();
                desc = descField.getText();
                category = (String)catField.getSelectedItem();
                numberOfVars = Integer.parseInt(numberField.getText());
                ok();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        panel.add(numberField);
        
        JButton okButton = new JButton("ok");
        okButton.addActionListener((ActionEvent e) -> {
            try{
                name = nameField.getText();
                desc = descField.getText();
                category = (String)catField.getSelectedItem();
                numberOfVars = Integer.parseInt(numberField.getText());
                ok();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        JPanel p = new JPanel();
        p.add(okButton);
        panel.add(p);
        
        frame.add(panel);
        frame.pack();
        
        //center frame
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth()/2), 
                (int)(screen.getHeight() / 2 - frame.getHeight()/2));
        frame.setVisible(true);
        
    }
    
    public void ok(){
        frame.setVisible(false);
        panel.removeAll();
        
        //info text
        JTextArea infoField = new JTextArea("Name variables and create functions"
                + " for them.\nStraight crossreferences will work. Leave a\n"
                + "function empty if it cannot be calculated", 3, 20);
        infoField.setOpaque(false);
        infoField.setEditable(false);
        infoField.setBorder(null);
        infoField.setFocusable(false);
        panel.add(infoField);
        
        //info text
        panel.add(info("Var name     Function", new EmptyBorder(10, 0, 0, 0)));
        
        final JTextField[] nameFields = new JTextField[numberOfVars];
        final JTextArea[] functionFields = new JTextArea[numberOfVars];
        
        for(int i = 0; i < numberOfVars; i++){
            JPanel p = new JPanel();
            
            nameFields[i] = new JTextField("", 3);
            p.add(nameFields[i]);
            
            JTextField field = new JTextField(" = ");
            field.setEditable(false);
            field.setBorder(null);
            field.setFocusable(false);
            p.add(field);
            
            functionFields[i] = new JTextArea(2, 20);
            functionFields[i].setLineWrap(true);
            JScrollPane scroll = new JScrollPane(functionFields[i]);
            
            p.add(scroll);
            
            panel.add(p);
        }
        
        JButton button = new JButton("Apply");
        button.addActionListener((ActionEvent e) -> {
            try{
                names = new String[numberOfVars];
                functions = new String[numberOfVars];
                selectable = new boolean[numberOfVars];
                
                for(int i = 0; i < numberOfVars; i++){
                    names[i] = nameFields[i].getText();
                    functions[i] = functionFields[i].getText();
                    selectable[i] = !functions[i].isEmpty();
                }
                
                frame.dispose();
                VilCAS.addScript(new Script(category, name, desc, names, functions, selectable));
                
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        JPanel p = new JPanel();
        p.add(button);
        panel.add(p);
        
        frame.add(panel);
        frame.pack();
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth()/2), 
                (int)(screen.getHeight() / 2 - frame.getHeight()/2));
        frame.setVisible(true);
    }
    
    
    private static JTextField info(String text, Border border){
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(border);
        field.setFocusable(false);
        
        return field;
    }
    
    
    public static void edit(Script s){
        JFrame frame = new JFrame("Script editor");
        
        //vertical top tier layout
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        //info text
        panel.add(info("Category", null));
        
        //get category names
        String[] categoryNames = new String[VilCAS.categories.size()];
        for(int i = 0; i < VilCAS.categories.size(); i++)
            categoryNames[i] = VilCAS.categories.get(i).name;
        
        //Category chooser
        JComboBox catField = new JComboBox(categoryNames);
        catField.setEditable(true);
        panel.add(catField);
        
        //info text
        panel.add(info("Script's name", new EmptyBorder(10, 0, 0, 0)));
        
        JTextField nameField = new JTextField(s.name);
        panel.add(nameField);
        
        //info text
        panel.add(info("Script's description", new EmptyBorder(10, 0, 0, 0)));
        
        JTextField descField = new JTextField(s.tooltip);
        panel.add(descField);
        
        //info text
        panel.add(info("Script's functions", new EmptyBorder(10, 0, 0, 0)));
        
        JTextArea[] functionFields = new JTextArea[s.vars.length];
        
        for(int i = 0; i < s.vars.length; i++){
            JPanel p = new JPanel();
            
            //info text
            p.add(info(" " + s.vars[i].name + " = ", null));
            
            functionFields[i] = new JTextArea(s.vars[i].function, 2, 20);
            functionFields[i].setLineWrap(true);
            
            JScrollPane scroll = new JScrollPane(functionFields[i]);
            
            p.add(scroll);
            panel.add(p);
        }
        
        
        JButton saveButton = new JButton("save");
        saveButton.addActionListener((ActionEvent e) -> {
            frame.dispose();
            for(int i = 0; i < s.vars.length; i++){
                s.vars[i].function = functionFields[i].getText();
                s.vars[i].selectable = !s.vars[i].function.isEmpty();
            }
            
            s.change((String)catField.getSelectedItem(), nameField.getText(), descField.getText());
        });
        
        JButton deleteButton = new JButton("delete script");
        deleteButton.addActionListener((ActionEvent e) -> {
            frame.dispose();
            VilCAS.removeScript(s);
        });
        
        JPanel p = new JPanel();
        p.add(saveButton);
        p.add(deleteButton);
        panel.add(p);
        
        frame.add(panel);
        frame.pack();
        
        //center frame
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth()/2), 
                (int)(screen.getHeight() / 2 - frame.getHeight()/2));
        frame.setVisible(true);
    }
    
    
    /*
    public void makeFunction() throws Exception{
        
        frame = new JFrame();
        
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel functionPanel = new JPanel();
        
        JPanel buttonPanel = getButtons();
        
        
        
    }
    
    private JPanel getButtons(){
        JPanel p = new JPanel();
        
        return p;
    }
    */
    
}
