package vililaskin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import vililaskin.scripter.*;
import vililaskin.categories.Category;

public class Window {
    
    //TODO finish menu bar
    
    public static JFrame frame;
    static JPanel panel;
    static JMenuBar menuBar;
    static CategoryChooser cc;
    
    public static void makeWindow(){
        frame = new JFrame("Vililaskin");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //vertical top-tier layout
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        makeMenuBar();
        
        cc = new CategoryChooser();
        cc.initCategories();
        panel.add(cc);
        
        for(Category c: Vililaskin.categories){
            if(c.checkBox.isSelected()){
                Vililaskin.updateCategory(c);
            }
        }
        
        frame.add(panel);
        
        frame.pack();
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
            (int)(screen.getHeight() / 2 - 200));
        frame.setVisible(true);

        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                Vililaskin.quit();
            }
        });
    }//makeWindow
    
    
    private static void makeMenuBar(){
        menuBar = new JMenuBar();
        
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        
        JMenuItem in = new JMenuItem("Import all functions", KeyEvent.VK_I);
        in.addActionListener((ActionEvent e) -> {SaveFunctions.loadFromFile();});
        file.add(in);
        
        JMenuItem out = new JMenuItem("Export all functions", KeyEvent.VK_X);
        out.addActionListener((ActionEvent e) -> {SaveFunctions.saveToFile();});
        file.add(out);
        
        JMenuItem javaScriptIn = new JMenuItem("Import Javascript", KeyEvent.VK_J);
        javaScriptIn.addActionListener((ActionEvent e) -> {SaveFunctions.loadFromFile();});
        file.add(javaScriptIn);
        
        JMenu create = new JMenu("Edit");
        create.setMnemonic('E');
        
        JMenuItem constant = new JMenuItem("Edit constants", KeyEvent.VK_C);
        constant.addActionListener((ActionEvent e) -> {
            try{
                new Constants().showEditor();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        create.add(constant);
        
        JMenuItem script = new JMenuItem("Make new script", KeyEvent.VK_S);
        script.addActionListener((ActionEvent e) -> {
            try{
                new Scripter().makeScript();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        create.add(script);
        
        
        JMenuItem function = new JMenuItem("Make new function", KeyEvent.VK_F);
        function.addActionListener((ActionEvent e) -> {
            try{
                new Scripter().makeFunction();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        create.add(function);
        
        JCheckBoxMenuItem update = new JCheckBoxMenuItem("Update variables globally");
        update.setSelected(true);
        
        
        menuBar.add(file);
        menuBar.add(create);
        
        frame.setJMenuBar(menuBar);
    }//makeMenuBar
    
}
