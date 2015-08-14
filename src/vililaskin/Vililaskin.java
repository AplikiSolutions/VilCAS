package vililaskin;

import java.util.*;
import javax.swing.*;
import vililaskin.categories.*;


public class Vililaskin extends JPanel{
    
    
    public static final ArrayList<Category> categories = new ArrayList();
    public static HashMap<String, Float> constants = new HashMap();
    
    public static void main(String[] args) {
        
        try{
            
            Window.makeWindow();
            SaveFunctions.loadLocal();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Vililaskin.main: " + e);
            e.printStackTrace(System.out);
            Window.frame.dispose();
            System.exit(0);
        }
    }//main
    
    
    
    public static void addCategory(Category c){
        categories.add(c);
        Window.cc.add(c.checkBox);
        Window.frame.pack();
    }//addCategory
    
    
    public static void removeCategory(Category c){
        categories.remove(c);
        Window.cc.remove(c.checkBox);
        Window.panel.remove(c);
        Window.frame.pack();
    }//removeCategory
    
    
    public static void updateCategory(Category c){
        if(c.checkBox.isSelected())
            Window.panel.add(c);
        else
            Window.panel.remove(c);
        
        Window.frame.pack();
    }//updateCategories
    
    
    public static void addScript(Script s){
        for(Category c: categories){
            if(s.category.equals(c.name)){
                c.addScript(s);
                return;
            }
        }
        Category c = new Category(s.category);
        c.addScript(s);
        addCategory(c);
    }//addScript
    
    
    public static void removeScript(Script s){
        for(Category c: categories){
            if(s.category.equals(c.name)){
                c.removeScript(s);
                if(c.scripts.isEmpty() && !c.isPermanent())
                    removeCategory(c);
                break;
            }
        }
    }//removeScript
    
    
    public static ArrayList<Script> getScripts(){
        ArrayList<Script> scripts = new ArrayList();

        for(Category c: categories)
            scripts.addAll(c.scripts);
        
        
        return scripts;
    }//getScripts

    public static void quit(){
        
        SaveFunctions.saveLocally();

        Window.frame.dispose();
        System.exit(0);
    }
}
