package EL.Norm;

/**
 * Created by wang on 2017/8/25.
 *
 * In our EL Ontology Langugage, we define
 * '&&' as 'and', 'Ext.' as 'Exist', '=>' as 'Belong to','==' as 'Equal'
 * Each atomic concept should be ONE upclass letter without N
 * Each role should be ONE lowclass letter
 * Tokens should not be split by space
 * TBox will be normalized by nf1-nf6
 * Do not support () at this version
 *
 */
public class Normalization {

    private static int MaxAxiom=50;
    private static int MaxRole=50;
    private static int MaxExpression=100;

    public int num_newAxiom=0;

    public int num_Axiom=0;

    public int num_Role=0;

    public int num_Exp=0;

    public char[] list_Axiom=new char[MaxAxiom];

    public char[] list_Role=new char [MaxRole];

    public String[] norm_expressions=new String [MaxExpression];

    private void expressionGenerator(String expression,int linenum)
    {
        if(expression.indexOf("==")==-1 && expression.indexOf("=>")!=-1){
            String subexp_left=expression.split("=>")[0];
            String subexp_right=expression.split("=>")[1];
            String[] rightpart=subexp_right.split("&&");

            if (rightpart.length==1){
                if (rightpart[0].split("Ext.").length<=2)
                {
                    String[] leftpart=subexp_left.split("&&");
                    if (leftpart.length>=3){
                        num_newAxiom++;
                        String newleft="";
                        for (int i=0;i<=leftpart.length-3;i++){
                            newleft=newleft+leftpart[i]+"&&";
                        }
                        newleft=newleft+leftpart[leftpart.length-2];
                        String newexp1=newleft=newleft+"=>"+"N"+num_newAxiom;
                        String newexp2="N"+num_newAxiom+"&&"+leftpart[leftpart.length-1]+"=>"+subexp_right;
                        expressionGenerator(newexp1,linenum);
                        expressionGenerator(newexp2,linenum);
                    }else
                    {
                        if (leftpart.length==2) {
                            if (leftpart[0].split("Ext.").length>1){
                                num_newAxiom++;
                                String newexp1=leftpart[0]+"=>"+"N"+num_newAxiom;
                                String newexp2="N"+num_newAxiom+"&&"+leftpart[1]+"=>"+subexp_right;
                                expressionGenerator(newexp1,linenum);
                                expressionGenerator(newexp2,linenum);
                            }else
                            if (leftpart[1].split("Ext.").length>1){
                                num_newAxiom++;
                                String newexp1=leftpart[1]+"=>"+"N"+num_newAxiom;
                                String newexp2=leftpart[0]+"&&"+"N"+num_newAxiom+"=>"+subexp_right;
                                expressionGenerator(newexp1,linenum);
                                expressionGenerator(newexp2,linenum);
                            }else
                            {
                                num_Exp++;
                                norm_expressions[num_Exp]=expression;
                                //System.out.println(expression);
                            }

                        }
                        else{
                            String[] eleftpart=leftpart[0].split("Ext.");
                            if (eleftpart.length>2)
                            {
                                num_newAxiom++;
                                String newleft="";
                                for (int i=1;i<=eleftpart.length-2;i++){
                                    newleft+="Ext."+eleftpart[i];
                                }
                                newleft+="N"+num_newAxiom;
                                String newexp1=newleft+"=>"+subexp_right;
                                String newexp2="Ext."+eleftpart[eleftpart.length-1]+"=>"+"N"+num_newAxiom;
                                expressionGenerator(newexp1,linenum);
                                expressionGenerator(newexp2,linenum);
                            }else
                            {
                                num_Exp++;
                                norm_expressions[num_Exp]=expression;
                                //System.out.println(expression);
                            }
                        }
                    }
                }else
                {
                    String[] erightpart=rightpart[0].split("Ext.");
                    num_newAxiom++;
                    String newright="";
                    for (int i=1;i<=erightpart.length-2;i++)
                        newright=newright+"Ext."+erightpart[i];
                    newright=newright+"N"+num_newAxiom;

                    String newexp1=subexp_left+"=>"+newright;
                    String newexp2="N"+num_newAxiom+"=>"+"Ext."+erightpart[erightpart.length-1];
                    expressionGenerator(newexp1,linenum);
                    expressionGenerator(newexp2,linenum);
                }
            }else
            {
                for (int i=0;i<=rightpart.length-1;i++)
                    expressionGenerator(subexp_left+"=>"+rightpart[i],linenum);
            }
        }else
            System.out.println("Syntax Error:Line "+linenum+", single => or == not found,invalid el expression");
    }


    private void lineGenerator(String line,int linenum){
        if(line.indexOf("==")!=-1 && line.indexOf("=>")==-1)
        {
            String subexp_left=line.split("==")[0];
            String subexp_right=line.split("==")[1];
            expressionGenerator(subexp_left+"=>"+subexp_right,linenum);
            expressionGenerator(subexp_right+"=>"+subexp_left,linenum);
        }else
        if(line.indexOf("==")==-1 && line.indexOf("=>")!=-1){
            expressionGenerator(line,linenum);
        }else
            System.out.println("Syntax Error:Line "+linenum+", single => or == not found,invalid el expression");
    }

    public void TBoxProgress(String[] TBoxStrings) {
        for (int i=0;i<TBoxStrings.length;i++)
        {
            lineGenerator(TBoxStrings[i],i+1);
        }
        for (int i=1;i<=num_Exp;i++) {
            System.out.println(norm_expressions[i]);
            for (int j=0;j<=norm_expressions[i].length()-1;j++)
            {
                if (norm_expressions[i].charAt(j)>='A' && norm_expressions[i].charAt(j)<='Z' && norm_expressions[i].charAt(j)!='T' && norm_expressions[i].charAt(j)!='E' && norm_expressions[i].charAt(j)!='N') {
                    Boolean flag=true;
                    for (int k = 0; k <= num_Axiom; k++) {
                        if (list_Axiom[k] == norm_expressions[i].charAt(j)) flag=false;
                    }
                    if (flag){
                        num_Axiom++;
                        list_Axiom[num_Axiom]= norm_expressions[i].charAt(j);
                    }
                }

                if (norm_expressions[i].charAt(j)>='a' && norm_expressions[i].charAt(j)<='z' && norm_expressions[i].charAt(j)!='t' && norm_expressions[i].charAt(j)!='x') {
                    Boolean flag=true;
                    for (int k = 0; k <= num_Role; k++) {
                        if (list_Role[k] == norm_expressions[i].charAt(j)) flag=false;
                    }
                    if (flag){
                        num_Role++;
                        list_Role[num_Role]= norm_expressions[i].charAt(j);
                    }
                }
            }

        }

        for (int i=1;i<=num_newAxiom;i++)
        {
            num_Axiom++;
            list_Axiom[num_Axiom]=(char)(i+'0');
        }
        System.out.print("Axioms:");
        for (int i=1;i<=num_Axiom;i++)
            System.out.print(list_Axiom[i]+" ");
        System.out.println();
        System.out.print("Roles:");
        for (int i=1;i<=num_Role;i++)
            System.out.print(list_Role[i]+" ");
        System.out.println();
    }


    public static void main(String[] args) {

        Normalization normalization=new Normalization();
        //String[] test={"A&&B&&C=>Ext.r.Ext.r.Ext.s.D"};

        //String[] test={"Ext.s.Ext.r.Ext.r.A&&Ext.s.Ext.r.Ext.r.B=>C"};
        String[] test={"A=>B&&Ext.r.C",
                        "C=>Ext.s.D",
                        "Ext.r.Ext.s.T&&B=>D"};

        normalization.TBoxProgress(test);

        Reasoning reasoning=new Reasoning();
        reasoning.ReasonProgress(normalization);

    }

}
