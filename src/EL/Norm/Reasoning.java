package EL.Norm;

/**
 * Created by wang on 2017/8/31.
 */
public class Reasoning {

    private static int MaxAxiom=50;
    private static int MaxRole=50;
    private static int MaxExpression=100;

    public int num_Axiom=0;

    public int num_newAxiom=0;

    public int num_Role=0;

    public int num_Exp=0;

    public char[][] set_S=new char[MaxAxiom][MaxAxiom];

    public int[] set_S_num=new int[MaxAxiom];

    public char[][][] set_R=new char[MaxAxiom][MaxAxiom][MaxAxiom];

    public int[][] set_R_num=new int[MaxAxiom][MaxAxiom];

    class MyCommand{
        public int type;
        public char[] axiom;
        public char role;

        public MyCommand(){
            axiom=new char[10];
        }

    }

    public MyCommand[] commands;
    public int command_num=0;

    private Normalization normalization;

    private int getIndex(char axiom)
    {
        for (int i=1;i<=num_Axiom;i++)
        {
            if (normalization.list_Axiom[i]==axiom) return i;
        }
        return -1;
    }

    private char getChar(String axiom)
    {
        if (axiom.charAt(0)=='N'){
            return axiom.charAt(1);
        }else
        {
            return axiom.charAt(0);
        }
    }

    private Boolean reason_Type0(char axiom1,char axiom2)
    {
        Boolean Tag=false;
        for (int t=1;t<=num_Axiom;t++) {
            Boolean p1=false,p2=true;
            for (int i = 1; i <= set_S_num[t]; i++) {
                if (axiom2 == set_S[t][i]) p2 = false;
                if (axiom1 == set_S[t][i]) p1 = true;
            }
            if (p1&&p2) {
                set_S_num[t]++;
                set_S[t][set_S_num[t]] = axiom2;
                Tag=true;
            }
        }
        return Tag;
    }

    private Boolean reason_Type1(char axiom1,char axiom2,char axiom3)
    {
        Boolean Tag=false;
        for (int i=1;i<=num_Axiom;i++)
        {
            Boolean p1=false,p2=false,p3=true;
            for (int j=1;j<=set_S_num[i];j++)
            {
                if (set_S[i][j]==axiom1) p1=true;
                if (set_S[i][j]==axiom2) p2=true;
                if (set_S[i][j]==axiom3) p3=false;
            }
            if (p1&&p2&&p3){
                set_S_num[i]++;
                set_S[i][set_S_num[i]]=axiom3;
                Tag=true;
            }
        }
        return Tag;
    }

    private Boolean reason_Type3(char role,char axiom1,char axiom2)
    {
        Boolean Tag=false;
        int index2=getIndex(axiom2);
        for (int i=1;i<=num_Axiom;i++)
        {
            Boolean p1=false,p2=true;
            for (int j=1;j<=set_S_num[i];j++)
            {
                if (set_S[i][j]==axiom1) p1=true;
            }
            for (int j=1;j<=set_R_num[i][index2];j++)
            {
                if (set_R[i][index2][j]==role) p2=false;
            }
            if (p1&&p2){
                Tag=true;
                set_R_num[i][index2]++;
                set_R[i][index2][set_R_num[i][index2]]=role;
                set_R_num[index2][i]++;
                set_R[index2][i][set_R_num[index2][i]]=role;
            }
        }
        return Tag;
    }

    private Boolean reason_Type2(char role,char axiom1,char axiom2){
        Boolean Tag=false;
        for (int i=1;i<=num_Axiom;i++)
         for (int j=1;j<=num_Axiom;j++)
         {
             Boolean p1=false,p2=false,p3=true;
             for (int k=1;k<=set_R_num[i][j];k++){
                 if (set_R[i][j][k]==role) p1=true;
             }
             for (int k=1;k<=set_S_num[j];k++)
             {
                 if (set_S[j][k]==axiom1) p2=true;
             }
             for (int k=1;k<=set_S_num[i];k++)
             {
                 if (set_S[i][k]==axiom2) p3=false;
             }
             if (p1&&p2&&p3){
                 set_S_num[i]++;
                 set_S[i][set_S_num[i]]=axiom2;
                 Tag=true;
             }
        }
        return Tag;
    }


    private void reason_ExpDivide()
    {
        for (int i=1;i<=num_Exp;i++)
        {
            String this_exp=normalization.norm_expressions[i];
            int type;
            if (this_exp.indexOf("Ext.")!=-1) {
                if (this_exp.indexOf("Ext.") == 0) {
                    type = 2;
                    command_num++;
                    commands[command_num]=new MyCommand();
                    String axiom1=this_exp.split("=>")[0].split("Ext.")[1].split("\\.")[1];
                    String role=this_exp.split("=>")[0].split("Ext.")[1].split("\\.")[0];
                    String axiom2=this_exp.split("=>")[1];
                    commands[command_num].axiom[1]=getChar(axiom1);
                    commands[command_num].axiom[2]=getChar(axiom2);
                    commands[command_num].role=role.charAt(0);
                    commands[command_num].type=type;
                } else {
                    type = 3;
                    command_num++;
                    commands[command_num]=new MyCommand();
                    String axiom1=this_exp.split("=>Ext.")[0];
                    String role=this_exp.split("=>Ext.")[1].split("\\.")[0];
                    String axiom2=this_exp.split("=>Ext.")[1].split("\\.")[1];
                    commands[command_num].axiom[1]=getChar(axiom1);
                    commands[command_num].axiom[2]=getChar(axiom2);
                    commands[command_num].role=role.charAt(0);
                    commands[command_num].type=type;
                }
            }else
                if (this_exp.indexOf("&&")!=-1){
                    type=1;
                    command_num++;
                    commands[command_num]=new MyCommand();
                    String axiom1=this_exp.split("=>")[0].split("&&")[0];
                    String axiom2=this_exp.split("=>")[0].split("&&")[1];
                    String axiom3=this_exp.split("=>")[1];
                    commands[command_num].axiom[1]=getChar(axiom1);
                    commands[command_num].axiom[2]=getChar(axiom2);
                    commands[command_num].axiom[3]=getChar(axiom3);
                    commands[command_num].type=type;
                }
                else{
                    type=0;
                    command_num++;
                    commands[command_num]=new MyCommand();
                    String axiom1=this_exp.split("=>")[0];
                    String axiom2=this_exp.split("=>")[1];
                    commands[command_num].axiom[1]=getChar(axiom1);
                    commands[command_num].axiom[2]=getChar(axiom2);
                    commands[command_num].type=type;
                }
        }
    }

    private void doCommands(){
        while (true){
            Boolean p=false;
            for (int i=1;i<=command_num;i++){
                switch (commands[i].type) {
                    case 0:
                        p=reason_Type0(commands[i].axiom[1],commands[i].axiom[2])||p;
                        break;
                    case 1:
                        p=reason_Type1(commands[i].axiom[1],commands[i].axiom[2],commands[i].axiom[3])||p;
                        break;
                    case 2:
                        p=reason_Type2(commands[i].role,commands[i].axiom[1],commands[i].axiom[2])||p;
                        break;
                    case 3:
                        p=reason_Type3(commands[i].role,commands[i].axiom[1],commands[i].axiom[2])||p;
                        break;
                }
            }
            if (!p) break;
        }
    }

    private void showResult()
    {
        for (int i=1;i<=num_Axiom;i++)
        {
            System.out.print(normalization.list_Axiom[i]+":");
            for (int j=1;j<=set_S_num[i];j++)
            {
                System.out.print(set_S[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }

    public void ReasonProgress(Normalization tmp_normalization) {

        commands=new MyCommand[MaxExpression];

        num_Axiom=tmp_normalization.num_Axiom;
        num_newAxiom=tmp_normalization.num_newAxiom;
        num_Role=tmp_normalization.num_Role;
        num_Exp=tmp_normalization.num_Exp;

        normalization=tmp_normalization;

        for (int i=1;i<=normalization.num_Axiom;i++) {
            set_S[i][1] = normalization.list_Axiom[i];
            set_S[i][2]='T';
            set_S_num[i]=2;
        }

        reason_ExpDivide();
        doCommands();
        showResult();
    }


}
