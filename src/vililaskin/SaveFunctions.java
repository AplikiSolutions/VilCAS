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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import vililaskin.categories.Category;

public class SaveFunctions {
    
    //TODO: make imports and exports in .zip format
    
    private static final String fileExt = ".vls";
    
    public static void loadLocal(){
        load(new File(System.getenv("APPDATA") + "\\Vililaskin"));
    }//loadLocal
    
    public static void loadFromFile(){
        JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION){
                load(fc.getSelectedFile());
            }
    }//loadFromFile
    
    private static void load(File targetFile){
        
        
        if(!targetFile.isDirectory()){
            return;
        }
        
        //load scripts
        for(File file: targetFile.listFiles()){
            
            if(!file.getName().endsWith(fileExt)){
                continue;
            }
            
            Properties props = new Properties();
            
            try(FileInputStream stream = new FileInputStream(file)){
                props.load(stream);
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Couldn't read Scripts:\n" + e);
                continue;
            }
            
            //get info
            String category = props.getProperty("category");
            String name = props.getProperty("function_name");
            String desc = props.getProperty("function_desc");
            
            //init arrays to hold varInfo
            int varNum = Integer.parseInt(props.getProperty("number_of_variables"));
            String[] names = new String[varNum];
            String[] functions = new String[varNum];
            boolean[] selectable = new boolean[varNum];
            
            //get variables
            for(int i = 0; i < names.length; i++){
                names[i] = props.getProperty("var_name_" + i);
                functions[i] = props.getProperty("var_function_" + i);
                selectable[i] = !props.getProperty("var_selectable_" + i, "true").equals("false");
            }
            
            Vililaskin.addScript(new Script(category, name, desc, names, functions, selectable));
        }//add a script
        
        //load constants
        Properties props = new Properties();
        
        try(FileInputStream stream = new FileInputStream(
                new File(targetFile.getAbsolutePath() + "\\constants.properties"))){
            props.load(stream);
        }catch(IOException e){
            return;
        }
        
        props.stringPropertyNames().stream().forEach((s) -> {
            Vililaskin.constants.put(s, Float.valueOf((String)props.get(s)));
        });
        
        
        //load other prefs
        Properties props2 = new Properties();
        
        try(FileInputStream stream = new FileInputStream(
                new File(targetFile.getAbsolutePath() + "\\preferences.properties"))){
            props2.load(stream);
        }catch(IOException e){
            return;
        }
        
        props2.stringPropertyNames().stream().forEach((s) -> {
            if(props2.get(s).equals("true")){
                for(Category c: Vililaskin.categories){
                    if(s.startsWith(c.name)){
                        c.checkBox.setSelected(true);
                    }
                }
            }
        });
    }//load
    
    public static void saveLocally(){
        saveScripts(new File(System.getenv("APPDATA") + "\\Vililaskin"));
    }//saveLocally
    
    public static void saveToFile(){
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveScripts(fc.getSelectedFile());
        }
    }//saveToFile
    
    private static void saveScripts(File targetFile){
        
        ArrayList<Script> scripts = Vililaskin.getScripts();
        
        if(!targetFile.isDirectory()){
            targetFile.mkdir();
        }else{
            for(File file: targetFile.listFiles()){
                if(file.getName().endsWith(fileExt))
                    file.delete();
            }
        }
        
        for(Script s: scripts){
            Properties props = new Properties();
            
            props.put("category", s.category);
            props.put("function_name", s.name);
            props.put("function_desc", s.tooltip);
            props.put("number_of_variables", Integer.toString(s.vars.length));
            
            for(int i = 0; i < s.vars.length; i++){
                props.put("var_name_" + i, s.vars[i].name);
                props.put("var_function_" + i, s.vars[i].function);
                if(!s.vars[i].selectable)
                    props.put("var_selectable_" + i, "false");
            }
            
            File f = new File(targetFile.getAbsolutePath() + "\\" + s.name + ".vls");
            
            try(FileOutputStream stream = new FileOutputStream(f)){
                props.store(stream, "Custom equation for Vililaskin\nDon't edit manually");
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Couldn't save scripts:\n" + e);
            }
        }//save a script
        
        Properties props = new Properties();
        
        Vililaskin.constants.keySet().stream().forEach((s) -> {
            props.put(s, Float.toString(Vililaskin.constants.get(s)));
        });
        
        File f = new File(targetFile.getAbsolutePath() +  "\\constants.properties");
        
        try(FileOutputStream stream = new FileOutputStream(f)){
            props.store(stream, "Constants for Vililaskin\nDon't edit manually");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Couldn't save scripts:\n" + e);
        }
        
        Properties props2 = new Properties();
        
        for(Category c: Vililaskin.categories){
            String s;
            if(c.checkBox.isSelected())
                s = "true";
            else
                s = "false";
            
            props2.put(c.name + "_open", s);
        }
        
        f = new File(targetFile.getAbsolutePath() +  "\\preferences.properties");
        
        try(FileOutputStream stream = new FileOutputStream(f)){
            props2.store(stream, "Preferences for Vililaskin\nDon't edit manually");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Couldn't save scripts:\n" + e);
        }
        
    }//saveScripts
    
}
