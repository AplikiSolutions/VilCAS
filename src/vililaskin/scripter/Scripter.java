package vililaskin.scripter;


import vililaskin.Script;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import vililaskin.CategoryChooser;
import vililaskin.Vililaskin;

import vililaskin.categories.*;

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
        String[] categoryNames = new String[Vililaskin.categories.size()];
        for(int i = 0; i < Vililaskin.categories.size(); i++)
            categoryNames[i] = Vililaskin.categories.get(i).name;
        
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
                Vililaskin.addScript(new Script(category, name, desc, names, functions, selectable));
                
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
        String[] categoryNames = new String[Vililaskin.categories.size()];
        for(int i = 0; i < Vililaskin.categories.size(); i++)
            categoryNames[i] = Vililaskin.categories.get(i).name;
        
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
            Vililaskin.removeScript(s);
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
