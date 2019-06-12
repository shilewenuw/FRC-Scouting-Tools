import java.io.*;
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
        BufferedReader br = null;
        try{
            scan = new Scanner(filetxt);
        } catch (Exception e){
            System.out.print("bob");
        }
        int teamscount = 0;
        String temp;
        try {
            br = new BufferedReader(new FileReader(filetxt));;
            String availalbe;
            while((availalbe = br.readLine()) != null) {
                if(isNumeric(availalbe))
                    teams.add(availalbe);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /*while(scan.hasNext()){
            //temp = scan.nextLine();
            //teams.add(scan.nextLine().replaceAll("[^\\d.]", ""));

            System.out.println(scan.nextLine());
            if(isNumeric(temp=scan.nextLine()))
                teams.add(temp);
        }*/

        System.out.print(teams.size()+"{");
        int iter=0;
        for(String item:teams){
            System.out.print("\""+item + "\",");
            iter++;
            if (iter%9==0)
                System.out.println();
            //System.out.println(item);

        }
        System.out.print("};");
    }//tells if a string is a number
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}


