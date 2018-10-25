import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by pickl on 11/29/2017.
 */
public class testies {

    public static void main(String[] args){
        //File filetxt = new File("C:\\Users\\pickl\\Desktop\\misc\\teams.txt");
        File filetxt = new File("C:\\Users\\pickl\\OneDrive\\Desktop\\chezy.txt");
        Scanner scan = null;
        ArrayList<String> teams = new ArrayList();

        try{
            scan = new Scanner(filetxt);
        } catch (Exception e){
            System.out.print("bob");
        }
        int teamscount = 0;
        String temp;
        while(scan.hasNext()){
            //temp = scan.nextLine();
            //teams.add(scan.nextLine().replaceAll("[^\\d.]", ""));

            //System.out.println(scan.nextLine());
            if(isNumeric(temp=scan.nextLine()))
                teams.add(temp);
        }
        System.out.print(teams.size()+"{");
        for(String item:teams){
            System.out.print("\""+item + "\",");
            //System.out.println(item);

        }
        System.out.print("};");
    }//tells if a string is a number
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}


