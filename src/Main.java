import Interface.AmazonStorage;
import Interface.GoogleStorage;
import Interface.KeyValueInterface;

import java.awt.event.KeyEvent;
import java.sql.Time;
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
                    try {
                        DoBenchmark(oStore);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    KeyValueInterface oStore = new GoogleStorage();
                    try {
                        DoBenchmark(oStore);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 0:
                    bLoopControl = false;
                    break;
            }
        }while(bLoopControl);
    }

    private static void DoTest(KeyValueInterface oStorage)
    {
        //System.out.println(oStorage.getKeys());
     /*   oStorage.NewBenchmark("nocheintest");
        oStorage.getValue("Dataset2/1 (2).docx");
        oStorage.getValue("Dataset2/1 (6).pdf");
        oStorage.getValue("Dataset2/4 (4).mp3");
        oStorage.GetBenchmarkResults().writeResults();
        oStorage.GetBenchmarkResults().writeAvg();*/
        var KeyList = oStorage.getKeys();
        for(String Key : KeyList)
        {
            oStorage.delete(Key);
            System.out.println("-->Delete key: "+ Key);
        }
    }

    private static void DoBenchmark(KeyValueInterface oStorage) throws InterruptedException {

        /**Begin with the first Part of the Benchmark
         * This goes trough every dataset and Store all files
         * */
        System.out.println("-> Begin Benchmark in: " + oStorage.StorageName());
        File filedirectory = new File(oStorage.GetFilePath);
        String[] directories = filedirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println("-> First benchmark: Upload");
        for(String directory : directories)
        {
            System.out.println("-->Dataset begins:" + directory);
            oStorage.NewBenchmark("Store dataset "+directory);
            File folder = new File(oStorage.GetFilePath+"/"+directory);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    oStorage.store(directory + "/" + file.getName(), file);
                    System.out.println("---> Stores:" + directory + "/" + file.getName());
                    TimeUnit.SECONDS.sleep(1);

                }
            }
            oStorage.GetBenchmarkResults().writeResults();
            System.out.println("--> Dataset finished, sleep");
            TimeUnit.MINUTES.sleep(1);
        }
        /**Second Part of the Benchmark
         * Getting all keys, to get a good result, this method is called 30 times
         * */
        System.out.println("-> Second Benchmark: Get all keys");
        oStorage.NewBenchmark("GetKeys");
        List<String> KeyList = new ArrayList<>();
        for(int Index = 0; Index < 30 ; Index++) {
            KeyList = oStorage.getKeys();
            System.out.println("--> for " + String.valueOf(Index)+ "/30");
            TimeUnit.SECONDS.sleep(5);
        }
        oStorage.GetBenchmarkResults().writeResults();


        /**Third Part of the Benchmark
         * Download all files again to the local store
         * */
        System.out.println("->Third benchmark: Download");
        String directory = "*blank*";
        boolean firstKey = true;
        for(String Key : KeyList)
        {
            if(!Key.contains(directory))
            {
                if(!firstKey) {
                    oStorage.GetBenchmarkResults().writeResults();
                    System.out.println("--> Dataset finished, sleeps");
                    TimeUnit.MINUTES.sleep(1);
                }
                firstKey = false;
                directory = Key.split("/")[0];
                oStorage.NewBenchmark("Get value from dataset "+ directory);
                System.out.println("-->Download dataset:" + directory);
            }
            oStorage.getValue(Key);
            System.out.println("---> Downloaded: "+ Key);
            TimeUnit.SECONDS.sleep(1);
        }
        oStorage.GetBenchmarkResults().writeResults();

        /**Last Part of the Benchmark
         * Deleting all files in the Cloud
         * */
        System.out.println("->Last benchmark: Delete");
        oStorage.NewBenchmark("DeleteAllKeys");
        for(String Key : KeyList)
        {
            oStorage.delete(Key);
            System.out.println("-->Delete key: "+ Key);
        }
        oStorage.GetBenchmarkResults().writeResults();
        System.out.println("->Benchmark finished!");
    }
}
