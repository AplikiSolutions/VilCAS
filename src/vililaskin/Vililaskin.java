package vililaskin;

import java.util.*;
import javax.swing.*;
import vililaskin.categories.*;


public class Vililaskin extends JPanel{
    
    private static Window window;
    public static final ArrayList<Category> categories = new ArrayList();
    public static HashMap<String, Float> constants = new HashMap();
    
    public static void main(String[] args) {
        
        try{
            
            window = new Window("Vililaskin");
            window.build();
            SaveFunctions.loadLocal();
            
            
            //open categories
            categories.stream().forEach((c) -> {
                if(c.checkBox.isSelected())
                    updateCategory(c);
            });
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Vililaskin.main: " + e);
            e.printStackTrace(System.out);
            window.dispose();
            System.exit(0);
        }
    }//main
    
    
    
    public static void addCategory(Category c){
        categories.add(c);
        window.cc.add(c.checkBox);
        window.panel.revalidate();
    }//addCategory
    
    
    public static void removeCategory(Category c){
        categories.remove(c);
        window.cc.remove(c.checkBox);
        window.panel.remove(c);
        window.panel.revalidate();
    }//removeCategory
    
    
    public static void updateCategory(Category c){
        if(c.checkBox.isSelected())
            window.panel.add(c);
        else
            window.panel.remove(c);
        
        window.panel.revalidate();
        
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
        window.panel.revalidate();
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
        window.panel.revalidate();
    }//removeScript
    
    
    public static ArrayList<Script> getScripts(){
        ArrayList<Script> scripts = new ArrayList();

        for(Category c: categories)
            scripts.addAll(c.scripts);
        
        
        return scripts;
    }//getScripts

    public static void quit(){
        
        SaveFunctions.saveLocally();

        window.dispose();
        System.exit(0);
    }
}
