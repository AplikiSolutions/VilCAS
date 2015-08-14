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
    
    
    public CategoryChooser(){
        add(getUpdateBox());
    }
    
    public void initCategories(){
        Vililaskin.addCategory(new CategoryMatematiikka());
        Vililaskin.addCategory(new CategoryFysiikka());
        Vililaskin.addCategory(new CategoryRaketti());
    }
    
    
    
    private JCheckBox getUpdateBox(){
        final JCheckBox updateChoice = new JCheckBox("Update all");
        
        updateChoice.setToolTipText("<html>When a value is calculated,<br>"
                + "all fields with that quantity<br>will be updated</html>");
        
        updateChoice.setSelected(true);
        updateChoice.setFocusable(false);
        updateChoice.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        
        updateChoice.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Equation.updateAll = updateChoice.isSelected();
            }
        });
        
        return updateChoice;
        
    }
    
    
    
}
