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

import vilcas.categories.Category;
import vilcas.scripter.Script;
import java.util.*;
import javax.swing.*;


public class VilCAS extends JPanel{
    
    private static MainWindow window;
    public static final ArrayList<Category> categories = new ArrayList();
    public static HashMap<String, Float> constants = new HashMap();
    
    public static void main(String[] args) {
        
        try{
            
            window = new MainWindow("VilCAS");
            window.build();
            SaveFunctions.loadLocal();
            
            
            //open categories
            categories.stream().forEach((c) -> {
                if(c.checkBox.isSelected())
                    updateCategory(c);
            });
            
            //init slow processes
            new Thread(){
                @Override
                public void run(){
                    //init Script's Javascript engine
                    Script.init();

                    //init grapher's Javascript engine
                    grapher.FunctionField.init();
                }
            }.start();
            
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
