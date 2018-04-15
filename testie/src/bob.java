import java.util.Scanner;

/**
 * Created by pickl on 3/9/2018.
 */
public class bob {
    public static void main(String[] args){
        System.out.println("Enter number of humans");
        Scanner scan = new Scanner(System.in);
        String stringnum = scan.nextLine();
        int numberofpeople = Integer.parseInt(stringnum);
        System.out.print("Enter number of matches");
        int numberofmatches = Integer.parseInt(scan.nextLine());

    }
}
