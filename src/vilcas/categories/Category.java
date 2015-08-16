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

package vilcas.categories;

import vilcas.Vililaskin;
import vilcas.scripter.Script;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;



public class Category extends JPanel{
    public JCheckBox checkBox;

    public String name;
    public final ArrayList<Script> scripts = new ArrayList();
    private final ArrayList<Equation> permanentEquations = new ArrayList();

    public Category(){}

    public Category(String _name){
        super();
        name = _name;
        checkBox = new JCheckBox(name);
        checkBox.setFocusable(false);
        checkBox.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(border, name));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        checkBox.addActionListener((ActionEvent e) -> {
            Vililaskin.updateCategory(Category.this);
        });
        addEquations();
        
    }


    public void addEquations(){};
    
    public void addEquation(Equation e){
        add(e);
        permanentEquations.add(e);
    }

    public void addScript(Script s){
        scripts.add(s);
        update();
    }
    
    public void removeScript(Script s){
        scripts.remove(s);
        update();
    }
    
    public void update(){
        removeAll();
        
        permanentEquations.stream().forEach((e) -> {
            add(e);
        });
        scripts.stream().forEach((s) -> {
            add(s);
        });
        
    }
    
        
    public boolean isPermanent(){
        return !permanentEquations.isEmpty();
    }
}
