package EL.Shell;

import EL.Norm.Normalization;
import EL.Norm.Reasoning;

import java.util.Scanner;

/**
 * Created by wang on 2017/8/25.
 *
 * EL Shell for input and output
 *
 */
public class ELShell {

    private static int state;

    private static int MaxExpression=100;

    public static void commandProcess(){
        Scanner sc=new Scanner(System.in);
        System.out.print(">");
        String m=sc.next();
        switch (m) {
            case ":help":
                HintMessage.showHelpMessage();
                break;
            case ":EL":
                state=2;
                break;
            default:
                System.out.println("Unknown Command");
        }

    }

    public static void elProcess(){
        System.out.print("#");
        Scanner sc=new Scanner(System.in);
        String line;
        String[] tbox=new String[MaxExpression];
        int num_line=0;
        while (true){
            line=sc.next();
            if (line.equals("!")) {state=1;break;}
            num_line++;
            tbox[num_line]=line;
            System.out.print("#");
        }
        String[] input=new String[num_line];
        for (int i=1;i<=num_line;i++)
            input[i-1]=tbox[i];
        Normalization normalization=new Normalization();
        normalization.TBoxProgress(input);
        Reasoning reasoning=new Reasoning();
        reasoning.ReasonProgress(normalization);
    }


    public static void shellConsole(){
        state=1;
        while (true) {
            switch (state) {
                case 1:
                    commandProcess();
                    break;
                case 2:
                    System.out.println("Input EL Tbox,end with !");
                    elProcess();
                    break;
            }
        }
    }
}
