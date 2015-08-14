package vililaskin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveFunctions {
    
    //TODO: make imports and exports in .zip format
    
    public static void loadLocal(){
        load(new File(System.getenv("APPDATA") + "\\Vililaskin"));
    }//loadLocal
    
    public static void loadFromFile(){
        JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(Window.frame);

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
            
            if(!file.getName().endsWith(".vls")){
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
    }//load
    
    public static void saveLocally(){
        saveScripts(new File(System.getenv("APPDATA") + "\\Vililaskin"));
    }//saveLocally
    
    public static void saveToFile(){
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(Window.frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveScripts(fc.getSelectedFile());
        }
    }//saveToFile
    
    private static void saveScripts(File targetFile){
        
        ArrayList<Script> scripts = Vililaskin.getScripts();
        
        if(!targetFile.isDirectory()){
            targetFile.mkdir();
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
        
    }//saveScripts
    
}
