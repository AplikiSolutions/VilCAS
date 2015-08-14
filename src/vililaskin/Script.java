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
