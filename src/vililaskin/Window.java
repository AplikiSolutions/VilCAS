package vililaskin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import vililaskin.scripter.*;

public class Window extends JFrame{
    
    //TODO finish menu bar
    
    JPanel panel;
    CategoryChooser cc;
    
    private JPanel fullPanel;
    
    private static final int WIDTH = 600, HEIGHT = 600;
    
    public Window(String s){
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
                Vililaskin.quit();
            }
        });
    }//build
    
    
    //make menu items
    private void makeMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(makeFile());
        menuBar.add(makeEdit());
        menuBar.add(makeView());
        
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
        
        JMenuItem out = new JMenuItem("Export all functions", KeyEvent.VK_X);
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
            Vililaskin.categories.stream().forEach((c) -> {
                c.checkBox.setSelected(true);
                Vililaskin.updateCategory(c);
            });
        });
        menuItem.add(viewAll);
        
        JMenuItem viewNone = new JMenuItem("No functions", KeyEvent.VK_N);
        viewNone.addActionListener((ActionEvent e) -> {
            Vililaskin.categories.stream().forEach((c) -> {
                c.checkBox.setSelected(false);
                Vililaskin.updateCategory(c);
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
}
