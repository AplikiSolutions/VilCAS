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


import vililaskin.Equation;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import vililaskin.Vililaskin;
import vililaskin.scripter.Scripter;

public class Script extends Equation{
    
    public customVariable[] vars;
    public String name, tooltip;
    
    
    public Script(String category, String name, String desc, String[] names, String[] functions, boolean[] selectable){
        
        customVariable[] newVars = new customVariable[names.length];
        
        for(int i = 0; i < newVars.length; i++){
            newVars[i] = new customVariable(names[i], functions[i], selectable[i]);
        }
        
        this.vars = newVars;
        this.name = name;
        this.category = category;
        tooltip = desc;
        
        super.init(name, desc, names);
        
        JButton editButton = new JButton("edit");
        add(editButton);
        
        editButton.addActionListener((ActionEvent e1) -> {
            Scripter.edit(this);
        });
    }
    
    //edit script
    public void change(String category, String name, String desc){
        guide.setText(name);
        guide.setToolTipText(desc);
        
        if(!this.category.equals(category)){
            Vililaskin.removeScript(this);
            this.category = category;
            Vililaskin.addScript(this);
        }
    }
    
    //evaluate script
    @Override
    public void calculate(int object) throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        for(int i = 0; i < vars.length; i++){
            if(i == object)
                continue;
            
            engine.put(vars[i].name, get(i));
        }
        
        Vililaskin.constants.keySet().stream().forEach((s) -> {
            engine.put(s, Vililaskin.constants.get(s));
        });
        
        try{
            set(object, ((Number)(engine.eval(vars[object].function))).doubleValue());
        }catch(ScriptException | ClassCastException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//calculate
    
    //checks if has a function
    @Override
    public boolean selectable(int object){
        return vars[object].selectable;
    }
    
    public static class customVariable{
        public String name, function;
        public boolean selectable;
        
        public customVariable(String name, String function, boolean editable){
            this.name = name;
            this.function = function;
            this.selectable = editable;
        }
    }
}
