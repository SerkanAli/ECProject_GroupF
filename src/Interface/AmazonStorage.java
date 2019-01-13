package Interface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

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
    public Object getValue(String key) {
        return  s3Client.getObject(bucketName, key);
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
    public void store(String key, Serializable value) {
        try {
            s3Client.putObject(bucketName, key, createStorageFile(value));
        } catch (IOException e) {
            System.err.println("Creating File failed!");
        }
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