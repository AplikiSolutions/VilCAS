package vililaskin.categories;

import vililaskin.Equation;


public class CategoryMatematiikka extends Category{
    
    public CategoryMatematiikka(){
        super("Mathematics");
    }
    
    @Override
    public void addEquations(){
        
        String name = "a² + b² + c² = d²";
        String desc = "<html>Pythagoras' theorem in 3d<br>(leave a, b or c as 0 for 2d)</html>";
        //Pythagoras
        addEquation(new Equation(name, desc, "a", "b", "c", "d"){
            private final int a = 0, b = 1, c = 2, d = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case a:
                        set(a, Math.sqrt(square(get(d)) - square(get(b)) - square(get(c))));
                        break;
                    case b:
                        set(b, Math.sqrt(square(get(d)) - square(get(a)) - square(get(c))));
                        break;
                    case c:
                        set(c, Math.sqrt(square(get(d)) - square(get(a)) - square(get(b))));
                        break;
                    case d:
                        set(d, Math.sqrt(square(get(a)) + square(get(b)) + square(get(c))));
                        break;
                }
            }
        });
        
        name = "ax² + bx + c = 0";
        desc = "The quadratic equation";
        //Quadratic
        addEquation(new Equation(name, desc, "x", "a", "b", "c"){
            private final int x = 0, a = 1, b = 2, c = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case x:
                        double d1 = -get(b) + Math.sqrt(square(get(b)) - 4 * get(a) * get(c));
                        d1 /= 2 * get(a);
                        double d2 = -get(b) - Math.sqrt(square(get(b)) - 4 * get(a) * get(c));
                        d1 /= 2 * get(a);
                        setMultiple(x, d1, d2);
                        break;
                }
                
            }
            
            @Override
            protected boolean selectable(int object){
                //can only calculate x
                return object == x;
            }
        });
        
        
        name = "Normal distribution";
        desc = "<html>The probability density<br>function for a non-normalized<br>"
                + "standard deviation<br>p = probability density<br>"
                + "x = value<br>μ = expected value<br>σ = deviation</html>";
        //Normal distribution
        addEquation(new Equation(name, desc, "p", "x", "μ", "σ"){
            private final int p = 0, x = 1, μ = 2, σ = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case p:
                        double d = - square((get(x) - get(μ)) / get(μ)) / 2;
                        d = Math.exp(d);
                        d /= get(σ) * Math.sqrt(2 * Math.PI);
                        set(p, d);
                        break;
                }
                
            }
            
            @Override
            protected boolean selectable(int object){
                //can only calculate x
                return object == p;
            }
            
        });
        
    }
    
    private double square(double d){
        return d * d;
    }
}
