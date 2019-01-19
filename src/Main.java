import Interface.AmazonStorage;
import Interface.GoogleStorage;
import Interface.KeyValueInterface;

import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

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
        var KeyList = oStorage.getKeys();
        System.out.println(KeyList);
    }

    protected static void DoBenchmark(KeyValueInterface oStorage)
    {
       // System.out.println("Not working yet");
        //if(true)
           // return;

        /**Begin with the first Part of the Benchmark
         * This goes trough every dataset and Store all files
         * */
        File filedirectory = new File(oStorage.GetFilePath);
        String[] directories = filedirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(String directory : directories)
        {
            oStorage.NewBenchmark("Store dataset "+directory);
            File folder = new File(oStorage.GetFilePath+"/"+directory);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    oStorage.store(directory + "/" + file.getName(), file);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            WriteResultstoFile(oStorage.GetBenchmarkResults());
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**Second Part of the Benchmark
         * Getting all keys, to get a good result, this method is called 30 times
         * */
        oStorage.NewBenchmark("GetKeys");
        List<String> KeyList = new ArrayList<>();
        for(int Index = 0; Index < 30 ; Index++) {
            KeyList = oStorage.getKeys();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WriteResultstoFile(oStorage.GetBenchmarkResults());


        /**Third Part of the Benchmark
         * Download all files again to the local store
         * */
        


        /**Last Part of the Benchmark
         * Deleting all files in the Cloud
         * */


    }

    protected static void BenchmarkAllfiles(boolean isStoring)
    {

    }


    protected static void WriteResultstoFile(KeyValueInterface.ResultsBenchmark oBench)
    {
        List<Long> Totaltime = oBench.GetTotalTime();
        List<Long> Lantecy = oBench.GetLatency();
        List<Double> Throughput = oBench.GetThroughput();
        String BenchName = oBench.Name();

        //TODO print the Results in a table file
    }
}
