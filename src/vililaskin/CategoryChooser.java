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
