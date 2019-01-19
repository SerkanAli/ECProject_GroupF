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
       /* // TODO Test
        System.out.println("TODO Test in " + oStorage.StorageName());
        oStorage.store("4", "4");*/
     /*   List<String> keyList = oStorage.getKeys();
        for(String key : keyList)
        {
            System.out.println("--Key name:" + key);
        }*/
        System.out.println("Value of key 3:" + oStorage.getValue("3"));
    }

    protected static void DoBenchmark(KeyValueInterface oStorage)
    {
        System.out.println("Not working yet");
        if(true)
            return;
        File filedirectory = new File("/path/to/directory");
        String[] directories = filedirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(String directory : directories)
        {
            oStorage.NewBenchmark(directory);
            File folder = new File(directory);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    oStorage.store("", file);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            WriteResultstoFile(oStorage.GetBenchmarkResults());
        }



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
