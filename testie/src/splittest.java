import java.io.File;
import java.util.Scanner;

/**
 * Created by pickl on 3/13/2018.
 */
public class splittest {
    static File file = new File("C:\\Users\\pickl\\Desktop\\DataFeed\\winloss.xlsx");
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print(file.getName().contains(".xlsx"));
        while(scanner.hasNext())
            System.out.print(scanner.nextLine().contains(".xlsx"));

    }
    public static void changeString(String[] arr){
        arr[0] = "bob";
    }
}
