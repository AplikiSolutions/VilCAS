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

package vilcas.categories;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;


public abstract class Equation extends JPanel{
    
    public static boolean updateAll = true;
    
    protected JTextField guide;
    private Variable[] variables;
    
    ButtonListener buttonListener = new ButtonListener();
    ButtonGroup group;
    public String category;
    static Font guideFont = new Font("arial", Font.PLAIN, 18);
    
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
            variables[j] = new Variable(names[j], selectable(j), this);
            add(variables[j]);
        }//for panelSetup
        
        
    }//init
    
    
    //this will provide the actual calculations
    public abstract void calculate(int functionID) throws Exception;
    
    
    
    private double round(double d){
        final int accuracy = 5;
        
        double multiplier = accuracy - 1 - (int)Math.log10(d);
        return Math.round(d * Math.pow(10, multiplier)) / Math.pow(10, multiplier);
        
    }//round
    
    
    //sets fields' values
    protected void set(int field, double value){
        
        if(updateAll){
            String name = variables[field].text.name;
            for(Variable v: variables){
                if(v.text.name.equals(name)){
                    v.text.setText(String.valueOf(round(value)));
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
                null, options, answers.get(0));
        
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
    
    
    
    protected boolean selectable(int i){
        return true;
    }
    
    public class ButtonListener implements ActionListener{
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
