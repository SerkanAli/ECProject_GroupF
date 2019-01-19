package Interface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;


public class AmazonStorage  implements KeyValueInterface, Serializable {

    private static final String clientRegion = "eu-central-1";
    private static final String bucketName = "ecprojectgproup9";
    private static final String stringObjKeyName = "Stringexample";
    private static final String fileObjKeyName = "FileExample";
    private static final String fileName = "E:\\Users\\nerka\\Onedrive\\Uni Ordner\\EC\\ECProject\\TestKeyValue.txt";

    private  AmazonS3 s3Client;

    public AmazonStorage()
    {
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new ProfileCredentialsProvider())
                .build();
        System.out.println("*Connecting AWS successful!*");
    }
    @Override
    public File getValue(String key) {
        oBench.Begin();
        // Get specific file from specified bucket
        S3Object o = s3Client.getObject(new GetObjectRequest(bucketName, key));
        oBench.Latency();
        oBench.Size(o.getObjectMetadata().getContentLength());
        // Download File to Local space
        InputStream stream = o.getObjectContent();
        OutputStream outputStream = null;
        File localFile = new File(DownloadPath + key);
        try {
            outputStream = new FileOutputStream(localFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while((bytesRead = stream.read(buffer)) !=-1){
                outputStream.write(buffer, 0, bytesRead);
            }
            stream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        oBench.End();
        //3  System.out.println(o.getExpirationTime());
        return  localFile;
    }

    @Override
    public List<String> getKeys() {
        List<String> keyList = new ArrayList<String>();
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os: objects) {
            keyList.add(os.getKey());
        }
        return keyList;
    }

    @Override
    public void store(String key, File value) {

            PutObjectResult ovalue = s3Client.putObject(bucketName, key, value);
    }

    @Override
    public void delete(String key) {
        try {
            s3Client.deleteObject(bucketName, key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }

    }

    @Override
    public String StorageName() {
        return "Amazon";
    }




    private static File createStorageFile(Serializable value) throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(value.toString());
        writer.close();

        return file;
    }

}