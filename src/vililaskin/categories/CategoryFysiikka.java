package vililaskin.categories;

import vililaskin.Equation;
import vililaskin.Constants;


public class CategoryFysiikka extends Category{
    
    public CategoryFysiikka(){
        super("Physics");
    }
    
    @Override
    public void addEquations(){
        
        String name = "x = vt + ½at²";
        String desc = "<html>Final position as<br>a function of initial velocity,<br>acceleration and time</html>";
        //Kiihtyvyys
        addEquation(new Equation(name, desc, "Position", "Velocity", "Acceleration", "Time"){
            private final int x = 0, v = 1, a = 2, t = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case x:
                        //calculate position
                        set(x, get(v) * get(t) + get(a) / 2 * square(get(t)));
                        break;
                    case v:
                        //calculate velocity
                        set(v, (get(x) - get(a) / 2 * square(get(t)))/get(t));
                        break;
                    case a:
                        //calculate acceleration
                        set(a, 2 * (get(x) - get(v) * get(t)) / square(get(t)));
                        break;
                    case t:
                        
                        //calculate time (quadratic, has two answers)
                        double d1 = (-get(v) + Math.sqrt(square(get(v)) + 2 * get(a) * get(x))) / get(a);
                        double d2 = (-get(v) - Math.sqrt(square(get(v)) + 2 * get(a) * get(x))) / get(a);
                        
                        setMultiple(t, d1, d2);
                        break;
                }
            }
        });
        
        
        name = "F = ma";
        desc = "Newton's second law of motion";
        //Voima
        addEquation(new Equation(name, desc, "Force", "Mass", "Acceleration"){
            private final int F = 0, m = 1, a = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case F:
                        //calculate force
                        set(F, get(m) * get(a));
                        break;
                    case m:
                        //calculate mass
                        set(m, get(F) / get(a));
                        break;
                    case a:
                        //calculate acceleration
                        set(a, get(F) / get(m));
                        break;
                }
                
            }
        });
        
        
        name = "Orbital period";
        desc = "<html>Orbital period of a light object<br>orbiting a massive body</html>";
        //Orbital period
        addEquation(new Equation(name, desc, "Time", "Semi-major axis", "Mass"){
            private final int t = 0, a = 1, M = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case t:
                        set(t, 2 * Math.PI * Math.sqrt(Math.pow(get(a), 3) / (get(M) * Constants.G)));
                        break;
                    case a:
                        set(a, Math.cbrt(square(get(t) / (2 * Math.PI)) * Constants.G * get(M)));
                        break;
                    case M:
                        set(M, Math.pow(get(a), 3) / square(get(t) / (2 * Math.PI)) / Constants.G);
                        break;
                }
                
            }
        });
        
    }
    
    private double square(double d){
        return d * d;
    }
}
