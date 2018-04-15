import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by pickl on 3/10/2018.
 */
public class Cookie {
    static final File folder = new File("C:\\Users\\pickl\\Desktop\\Cookies");
    static String[][] array;
    static final int posName = 0;
    static final int posMatchNum = 1;
    static final int posBetColor= 2;
    static final int posChipsNum = 3;
    static ArrayList<String> names;
    static String[] matchresults;

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        matchresults = scan.nextLine().split(",");
        /*System.out.print(matchresults.length);
        int count =1;
        for(String temp:matchresults) {
            System.out.println(temp + ":" +count);
            count++;
        }*/
        names = new ArrayList<>();
        array = new String[folder.listFiles().length][4];
        int iterate = 0;
        String temp = "";
        for(File file:folder.listFiles()){
            try{
                BufferedReader reader=new BufferedReader(new FileReader(file));
                String matchnumber = file.getName().replaceAll("\\D+","");

                temp = reader.readLine();

                array[iterate][posName] = temp.split(",")[0].toLowerCase();//name
                if(!names.contains(array[iterate][posName]))
                    names.add(array[iterate][posName]);
                array[iterate][posMatchNum] = matchnumber;
                try{
                    array[iterate][posBetColor] = temp.split(",")[1];//color of team that is bet on
                } catch (Exception e){
                    array[iterate][posBetColor] = "";
                }
                array[iterate][posChipsNum] = temp.split(",")[2];
                iterate++;

            } catch (Exception e){e.printStackTrace();System.out.println(temp);}
        }
        for(String name:names){
            System.out.println(name + ": " + calculateCookiesPerPerson(name));
        }
    }
    public static int calculateCookiesPerPerson(String name){
        int total  = 0;
        int count = 0;
        for(String[] arr:array){
            if(arr[posName].equals(name)){try{
                if(arr[posBetColor].equals(matchresults[ Integer.parseInt(arr[posMatchNum])  -1].toLowerCase()))
                    total+=Integer.parseInt(arr[posChipsNum]);
                else if(matchresults[ Integer.parseInt(arr[posMatchNum]) - 1 ].equals("Tie")||
                        arr[posBetColor].equals(""))
                    total+=0;
                else {
                    total -= Integer.parseInt(arr[posChipsNum]);
                    //System.out.println(arr[posBetColor] + arr[posMatchNum]+ "-" + matchresults[ Integer.parseInt(arr[posMatchNum]) - 1 ] + "-" + arr[posName]);
                }
                count++;
            }catch (Exception e){e.printStackTrace();}}//System.out.print(arr[0] + "-"+arr[1] + count+" ");count++;}
        }
        return total;
    }
    //public static void
}
