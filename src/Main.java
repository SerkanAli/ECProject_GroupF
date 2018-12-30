import Interface.AmazonStorage;
import Interface.GoogleStorage;
import Interface.KeyValueInterface;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        boolean bLoopControl = true;
        do {
            System.out.println("*********MENU*********");
            System.out.println("Choose one Option");
            System.out.println("1- Test connection to AWS");
            System.out.println("2- Test connection to Google");
            System.out.println("3- Benchmark AWS");
            System.out.println("4- Benchmark Google");
            System.out.println("0- Exit");
            System.out.println("*********************");
            Scanner oScan = new Scanner(System.in);
            if(!oScan.hasNextBigInteger()) {
                System.out.println("False input!");
                continue;
            }
            switch(oScan.nextInt())
            {
                default:
                    System.out.println("False input!");
                    continue;
                case 1: {
                    KeyValueInterface oStore = new AmazonStorage();
                    DoTest(oStore);
                    break;
                }
                case 2: {
                    KeyValueInterface oStore = new GoogleStorage();
                    DoTest(oStore);
                    break;
                }
                case 3: {
                    KeyValueInterface oStore = new AmazonStorage();
                    DoBenchmark(oStore);
                    break;
                }
                case 4: {
                    KeyValueInterface oStore = new GoogleStorage();
                    DoBenchmark(oStore);
                    break;
                }
                case 0:
                    bLoopControl = false;
                    break;
            }
        }while(bLoopControl);
    }

    protected static void DoTest(KeyValueInterface oStorage)
    {
        // TODO Test
        System.out.println("TODO Test in " + oStorage.StorageName());
    }

    protected static void DoBenchmark(KeyValueInterface oStorage)
    {
        //TODO Benchmark
        System.out.println("TODO Benchmark in " + oStorage.StorageName());
    }
}
