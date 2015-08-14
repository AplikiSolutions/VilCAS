package vililaskin.categories;

import vililaskin.Script;
import vililaskin.Equation;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import vililaskin.*;


public class Category extends JPanel{
    public JCheckBox checkBox;

    public String name;
    public final ArrayList<Script> scripts = new ArrayList();
    private final ArrayList<Equation> permanentEquations = new ArrayList();

    public Category(){}

    public Category(String _name){
        super();
        name = _name;
        checkBox = new JCheckBox(name);
        checkBox.setFocusable(false);
        checkBox.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(border, name));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        checkBox.addActionListener((ActionEvent e) -> {
            Vililaskin.updateCategory(Category.this);
        });
        addEquations();
        
        if(name.equals("Mathematics")){
            checkBox.setSelected(true);
        }
    }


    public void addEquations(){};
    
    public void addEquation(Equation e){
        add(e);
        permanentEquations.add(e);
    }

    public void addScript(Script s){
        scripts.add(s);
        update();
    }
    
    public void removeScript(Script s){
        scripts.remove(s);
        update();
    }
    
    public void update(){
        removeAll();
        
        permanentEquations.stream().forEach((e) -> {
            add(e);
        });
        scripts.stream().forEach((s) -> {
            add(s);
        });
        
        Window.frame.pack();
    }
    
        
    public boolean isPermanent(){
        return !permanentEquations.isEmpty();
    }
}
