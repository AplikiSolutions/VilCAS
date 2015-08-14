package vililaskin.categories;

import vililaskin.Equation;
import vililaskin.Constants;


public class CategoryRaketti extends Category{
    
    public CategoryRaketti(){
        super("Rocketry");
    }
    
    @Override
    public void addEquations(){
        
        String name = "Delta v";
        String desc = "<html>The Tsiolkovsky rocket<br>equation for delta v</html>";
        //Delta v
        addEquation(new Equation(name, desc, "Delta v", "Isp", "Wet mass", "Dry mass"){
            private final int dv = 0, Isp = 1, m0 = 2, m1 = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case dv:
                        set(dv, get(Isp) * Constants.g * Math.log(get(m0) / get(m1)));
                        break;
                    case Isp:
                        set(Isp, get(dv) / (Constants.g * Math.log(get(m0) / get(m1))));
                        break;
                    case m0:
                        set(m0, get(m1) * Math.exp(get(dv) / (get(Isp) * Constants.g)));
                        break;
                    case m1:
                        set(m1, get(m0) / Math.exp(get(dv) / (get(Isp) * Constants.g)));
                        break;
                }
            }
        });
        
        
        name = "Re = Rt * sqrt(E)";
        desc = "<html>Equation for the expansion<br>ratio of a nozzle</html>";
        //Kuristimen expansion ratio
        addEquation(new Equation(name, desc, "Exit radius", "Throat radius", "Exp ratio"){
            private final int re = 0, rt = 1, E = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case re:
                        set(re, get(rt) * Math.sqrt(get(E)));
                        break;
                    case rt:
                        set(rt, get(re) / Math.sqrt(get(E)));
                        break;
                    case E:
                        double d = get(re) / get(rt);
                        d *= d;
                        set(E, d);
                        break;
                }
            }
        });
        
    }
}
