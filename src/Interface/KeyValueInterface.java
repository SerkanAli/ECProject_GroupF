package Interface;

import com.google.api.services.storage.Storage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Specifies the generic interface of a key value store.
 * Keys are always Strings, whereas values are of type Serializable, i.e., any Java Object that can be serialized.
 */
public interface KeyValueInterface {

    String GetFilePath = System.getProperty("user.dir")+"/DataSets";;
    String DownloadPath = System.getProperty("user.dir")+"/DownloadData";

    /**
     * returns a value for a given key
     * @param key
     * @return
     */
    File getValue(String key);

    /**
     * returns a list of all keys
     */
    List<String> getKeys();

    /**
     * stores a key value pair
     * @param key
     * @param value
     */
    void store(String key, File value);

    /**
     * deletes the key value pair for a given key
     * @param key
     */
    void delete(String key);

    // Stores the Name of the System
    String StorageName();

    /**
    * This class provides var and methods to store and calculate the benchmark results
     */
    class ResultsBenchmark
    {
        private List<Long> nTotalTime;
        private List<Long> nLatency;
        private List<Double> nThroughput;
        protected String sBenchmarkName;

        public List<Long> GetTotalTime() { return nTotalTime;}
        public List<Long> GetLatency() { return nLatency;}
        public List<Double> GetThroughput() { return nThroughput;}
        public String Name() {return sBenchmarkName;}

        public ResultsBenchmark(){
            nTotalTime = new ArrayList<>();
            nLatency = new ArrayList<>();
            nThroughput = new ArrayList<>();
        }

        public void SetName(String name){ sBenchmarkName = name;}

        public void Clear(){
            nTotalTime.clear();
            nLatency.clear();
            nThroughput.clear();
        }

        private Long nZeroTime;
        private Long nFirstByte;
        private boolean bSetLatency;
        private Long nFileSize;
        private  boolean bSetSize;

        protected  void Begin() {
            nZeroTime =  new Date().getTime();
            bSetLatency = false;
            bSetSize = false;
        }
        protected  void Latency() { nFirstByte = new Date().getTime(); bSetLatency = true;}
        protected  void Size(Long nSize) {nFileSize = nSize; bSetSize = true;}
        protected  void End(){
            Long nTime = (new Date().getTime()) - nZeroTime;
            nTotalTime.add(nTime);
            if(bSetLatency)
                nLatency.add(nFirstByte - nZeroTime);
            else
                nLatency.clear();
            if(bSetSize)
                nThroughput.add((double)(nFileSize / nTime));
            else
                nThroughput.clear();
        }

        public void writeResults(){
            try {
                StringBuilder logs = new StringBuilder();

                logs.append("\n").append("Total time").append("\t").append("Latency").append("\t").append("Throughput");

                int size = nTotalTime.size();

                for(int i=0 ; i<size;i++){
                    logs.append("\n").append(nTotalTime.get(i)).append("\t").append(nLatency.get(i)).append("\t").append(nThroughput.get(i));
                }

                FileUtils.writeStringToFile(new File("logs/"+this.sBenchmarkName+".txt"),logs.toString(), "utf8", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ResultsBenchmark oBench = new ResultsBenchmark();
    default void NewBenchmark(String BenchName){
       oBench.Clear();
       oBench.SetName(BenchName);
    }
    default ResultsBenchmark GetBenchmarkResults() { return oBench;}
}
