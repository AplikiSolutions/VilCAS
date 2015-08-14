package vililaskin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import vililaskin.categories.CategoryFysiikka;
import vililaskin.categories.CategoryMatematiikka;
import vililaskin.categories.CategoryRaketti;

public class CategoryChooser extends JPanel{
    
    
    public CategoryChooser(){}
    
    public void initCategories(){
        Vililaskin.addCategory(new CategoryMatematiikka());
        Vililaskin.addCategory(new CategoryFysiikka());
        Vililaskin.addCategory(new CategoryRaketti());
    }
    
    
    
    
}
