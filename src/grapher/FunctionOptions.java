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

package grapher;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class FunctionOptions extends JPanel{
    
    private static FunctionField active;
    private static JComboBox diffChooser;
    
    public FunctionOptions(){
        
        JButton solveButton = new JButton("solve f(x) = 0");
        solveButton.addActionListener((ActionEvent e) -> {
            String ans = active.solve();
            if(ans.equals("NaN"))
                JOptionPane.showMessageDialog(null, "no answer found on screen");
            JOptionPane.showMessageDialog(null, "x ≈ " + active.solve());
        });
        add(solveButton);
        
        String[] diffStrings = {"f(x)", "Df(x)", "∫f(x)"};
        diffChooser = new JComboBox(diffStrings);
        diffChooser.setSelectedIndex(0);
        diffChooser.addActionListener((ActionEvent e) -> {
            if(active == null)
                return;
            active.setDiff(diffChooser.getSelectedIndex());
            Window.repaint();
        });
        add(diffChooser);
        
        
        JButton removeButton = new JButton("remove");
        //removeButton.setBorder(new LineBorder(Color.gray));
        removeButton.setFocusable(false);
        removeButton.addActionListener((ActionEvent e) -> {
            if(active == null)
                return;
            Window.remove(active);
        });
        add(removeButton);
    }//FunctionOptions
    
    public static void setActive(FunctionField f){
        active = f;
        
        diffChooser.setSelectedIndex(active.getDiff());
    }//setActive
    
    public static FunctionField getActive(){
        return active;
    }//getActive
}
