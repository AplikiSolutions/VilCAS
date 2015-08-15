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

package vililaskin;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;
import java.util.ArrayList;


import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;


public abstract class Equation extends JPanel{
    
    public static boolean updateAll = true;
    
    protected JTextField guide;
    private Variable[] variables;
    
    ButtonGroup group;
    public String category;
    static Font guideFont = new Font("arial", Font.PLAIN, 18);
    
    private static final ArrayList<Field> allFields = new ArrayList();
    
    public Equation(){}
    
    public Equation(String equationName, String toolTip, String... names){
        init(equationName, toolTip, names);
    }
    
    //is called from Script
    public final void init(String equationName, String toolTip, String... names){
        
        guide = new JTextField(equationName);
        guide.setEditable(false);
        guide.setBorder(null);
        guide.setFocusable(false);
        guide.setFont(guideFont);
        
        if(toolTip != null && !toolTip.equals(""))
            guide.setToolTipText(toolTip);
        add(guide);
        
        variables = new Variable[names.length];
        group = new ButtonGroup();
        
        //set up a panel for a variable
        for(int j = 0; j < names.length; j++){
            variables[j] = new Variable(names[j], selectable(j));
            add(variables[j]);
        }//for panelSetup
        
        
    }//init
    
    
    //this will provide the actual calculations
    public abstract void calculate(int functionID) throws Exception;
    
    
    
    private double round(double d){
        final int accuracy = 5;
        
        double multiplier = accuracy - 1 - (int)Math.log10(d);
        return Math.rint(d * Math.pow(10, multiplier)) / Math.pow(10, multiplier);
        
    }//round
    
    
    //sets fields' values
    protected void set(int field, double value){
        
        if(updateAll){
            String name = variables[field].text.name;
            for(Field f: allFields){
                if(f.name.equals(name)){
                    f.setText(String.valueOf(round(value)));
                }
            }
        }else{
            variables[field].text.setText(String.valueOf(round(value)));
        }
    }//set
    
    
    protected void setMultiple(int field, double... input){
        
        
        ArrayList<Double> answers = new ArrayList();
        for(double d: input){
            answers.add(round(d));
        }
        
        //check if multiple same answers
        for(int i = 0; i < answers.size(); i++){
            
            Double d = answers.get(i);
            
            //remove non-numbers
            while(d.isNaN()){
                if(answers.size() == 1){
                    set(field, d);
                    return;
                }
                answers.remove(i);
                d = answers.get(i);
            }
            
            for(int j = i + 1; j < answers.size(); j++){
                double d2 = answers.get(j);
                if(Math.abs(d - d2) < Math.max(d, d2) / 10000){
                    answers.remove(j);
                    j--;
                }
            }
        }
        
        if(answers.size() == 1){
            set(field, answers.get(0));
            return;
        }
        
        String[] options = new String[answers.size()];
        
        for(int i = 0; i < answers.size(); i++){
            options[i] = Double.toString(answers.get(i));
        }
        
        String selection =(String)JOptionPane.showInputDialog(null,
                "Due to roots, there were multiple\npossible answers, choose one:",
                "Multiple answers", JOptionPane.PLAIN_MESSAGE,
                null, options, answers.get(0)
        );
        
        if(selection != null){
            set(field, Double.valueOf(selection));
        }else{
            set(field, answers.get(0));
        }
    }//setMultiple
    
    
    //returns a double from selected field
    protected double get(int field) throws Exception{
        String num = variables[field].text.getText();
        
        if(num.isEmpty())
            throw new Exception("Empty value");
        
        return Double.parseDouble(num);
    }//get
    
    
    private class Selector extends FocusAdapter{
        @Override
        public void focusGained(FocusEvent e){
            JTextField field = (JTextField)e.getComponent();
            field.selectAll();
        }
    }//selector
    
    
    public class Field extends JTextField{
        
        public String name;
        
        public Field(String _name, int size){
            super("", size);
            
            name = _name;
        }
    }//Field
    
    protected boolean selectable(int i){
        return true;
    }
    
    private final Selector selector = new Selector();
    private final ButtonListener buttonListener = new ButtonListener();
    
    public class Variable extends JPanel{
        public Field text;
        JButton button;
        
        public Variable(String name, boolean selectable){
            
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
            text.addFocusListener(selector);
            text.setBorder(null);
            allFields.add(text);
            
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
            p.setBackground(Color.white);
            p.setBorder(null);
            p.add(text);
            
            if(selectable){
                button = new JButton(" = ");
                button.setBorder(new EmptyBorder(2, 2, 2, 2));
                button.setFocusable(false);
                button.addActionListener(buttonListener);
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
    }
    
    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int k = -1;
            
            for(int i = 0; i < variables.length; i++){
                if(variables[i].button.equals(e.getSource())){
                    k = i;
                    break;
                }
            }
            
            try{
                //get answer
                calculate(k);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Couldn't perform operation:\n" + ex);
            }
        }
    }
}
