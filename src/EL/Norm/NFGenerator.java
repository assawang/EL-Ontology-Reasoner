package EL.Norm;


/**
 * Created by wang on 2017/8/26.
 */
public class NFGenerator {

    public static void genNF1(){
    }

    public static void genNF2(){
    }

    public static void genNF3(){
    }




    public static Boolean isAtomic(String expression) {
        Boolean Tag=false;
        if (expression.indexOf("Ext.")==-1 &&
            expression.indexOf("&&")==-1 &&
            expression.indexOf("==")==-1 &&
            expression.indexOf("=>")==-1)
            Tag=true;
        return Tag;
    }




    public static void main(String[] args) {
        System.out.print(NFGenerator.isAtomic("A=>Ext.A"));
    }

}
