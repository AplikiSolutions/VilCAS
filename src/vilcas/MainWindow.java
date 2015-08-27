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

import vilcas.scripter.Scripter;
import vilcas.categories.Equation;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame{
    
    //TODO finish menu bar
    
    JPanel panel;
    CategoryChooser cc;
    
    private JPanel fullPanel;
    
    private static final int WIDTH = 650, HEIGHT = 600;
    
    public MainWindow(String s){
        super(s);
    }//Window
    
    public void build(){
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //top-tier layout
        fullPanel = new JPanel();
        fullPanel.setLayout(new BorderLayout());
        fullPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        makeMenuBar();
        
        //vertical layout for categories
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        //keeps categories grouped
        JPanel midPanel = new JPanel();
        midPanel.add(panel);
        
        //scrollable main view
        JScrollPane scrollPane = new JScrollPane(midPanel);
        fullPanel.add(scrollPane, BorderLayout.CENTER);
        
        cc = new CategoryChooser();
        cc.initCategories();
        fullPanel.add(cc, BorderLayout.PAGE_START);
        add(fullPanel);
        
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int)(screen.getWidth() / 2 - WIDTH / 2), 
            (int)(screen.getHeight() / 2 - HEIGHT / 2), WIDTH, HEIGHT);
        setVisible(true);

        
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                VilCAS.quit();
            }
        });
    }//build
    
    
    //make menu items
    private void makeMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(makeFile());
        menuBar.add(makeEdit());
        menuBar.add(makeView());
        menuBar.add(makeExtensions());
        
        setJMenuBar(menuBar);
    }//makeMenuBar
    
    
    //make the "File" item
    private JMenu makeFile(){
        JMenu menuItem = new JMenu("File");
        menuItem.setMnemonic('F');
        
        JMenuItem in = new JMenuItem("Import all functions", KeyEvent.VK_I);
        in.addActionListener((ActionEvent e) -> {
            SaveFunctions.loadFromFile();
        });
        menuItem.add(in);
        
        JMenuItem out = new JMenuItem("Export all functions");
        out.addActionListener((ActionEvent e) -> {
            SaveFunctions.saveToFile();
        });
        menuItem.add(out);
        
//        JMenuItem javaScriptIn = new JMenuItem("Import Javascript", KeyEvent.VK_J);
//        javaScriptIn.addActionListener((ActionEvent e) -> {SaveFunctions.loadFromFile();});
//        menuItem.add(javaScriptIn);
        
        return menuItem;
    }//makeFile
    
    
    //make the "Edit" item
    private JMenu makeEdit(){
        JMenu menuItem = new JMenu("Edit");
        menuItem.setMnemonic('E');
        
        JMenuItem constant = new JMenuItem("Edit constants", KeyEvent.VK_C);
        constant.addActionListener((ActionEvent e) -> {
            try{
                new Constants().showEditor();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        menuItem.add(constant);
        
        JMenuItem script = new JMenuItem("Make new script", KeyEvent.VK_S);
        script.addActionListener((ActionEvent e) -> {
            try{
                new Scripter().makeScript();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        menuItem.add(script);
        
        JCheckBoxMenuItem updateChoice = new JCheckBoxMenuItem("Update variables globally");
        updateChoice.setToolTipText("<html>When a value is calculated,<br>"
                + "all fields with that quantity<br>will be updated</html>");
        updateChoice.setSelected(true);
        updateChoice.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Equation.updateAll = updateChoice.isSelected();
            }
        });
        menuItem.add(updateChoice);
        
        /*
        JMenuItem function = new JMenuItem("Make new function", KeyEvent.VK_F);
        function.addActionListener((ActionEvent e) -> {
            try{
                new Scripter().makeFunction();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        });
        menuItem.add(function);
        */
        return menuItem;
    }//makeEdit
    
    
    //make the "View" item
    private JMenu makeView(){
        JMenu menuItem = new JMenu("View");
        menuItem.setMnemonic('V');
        
        JMenuItem viewAll = new JMenuItem("All functions", KeyEvent.VK_A);
        viewAll.addActionListener((ActionEvent e) -> {
            VilCAS.categories.stream().forEach((c) -> {
                c.checkBox.setSelected(true);
                VilCAS.updateCategory(c);
            });
        });
        menuItem.add(viewAll);
        
        JMenuItem viewNone = new JMenuItem("No functions", KeyEvent.VK_N);
        viewNone.addActionListener((ActionEvent e) -> {
            VilCAS.categories.stream().forEach((c) -> {
                c.checkBox.setSelected(false);
                VilCAS.updateCategory(c);
            });
        });
        menuItem.add(viewNone);
        
        JCheckBoxMenuItem viewSelector = new JCheckBoxMenuItem("Category select bar");
        viewSelector.addActionListener((ActionEvent e) -> {
            cc.setVisible(viewSelector.isSelected());
            fullPanel.revalidate();
        });
        viewSelector.setSelected(true);
        menuItem.add(viewSelector);
        
        
        return menuItem;
    }//makeView
    
    
    //make the "Extensions" item
    private JMenu makeExtensions(){
        JMenu menuItem = new JMenu("Extensions");
        menuItem.setMnemonic('X');
        
        JMenuItem triangleSolver = new JMenuItem("Triangle solver", KeyEvent.VK_T);
        triangleSolver.addActionListener((ActionEvent e) -> {
            try{
                new trianglesolver.Window().start();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "MainWindow: " + ex);
                ex.printStackTrace(System.out);
            }
        });
        menuItem.add(triangleSolver);
        
        JMenuItem grapher = new JMenuItem("Grapher", KeyEvent.VK_G);
        grapher.addActionListener((ActionEvent e) -> {
            try{
                new grapher.Window().start();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "MainWindow: " + ex);
                ex.printStackTrace(System.out);
            }
        });
        menuItem.add(grapher);
        
        return menuItem;
    }//makeWindow
}
