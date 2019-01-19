package Interface;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GoogleStorage implements KeyValueInterface, Serializable {

    private static final String bucketName = "ec-assignment";

    private  Storage storage;

    public GoogleStorage(){
        // Instantiate a Google Cloud Storage client
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public File getValue(String key) {
        oBench.Begin();
        // Get specific file from specified bucket
        Blob blob = storage.get(BlobId.of(bucketName, key));
        oBench.Latency();
        oBench.Size((long) blob.getContent().length);
        // Download File to Local space
        File localFile = new File(DownloadPath + key);
        blob.downloadTo(localFile.toPath());
        oBench.End();
        return localFile;
    }

    @Override
    public List<String> getKeys() {
        // Creates the new bucket
        oBench.Begin();
        Bucket bucket = storage.get( bucketName);
        Page<Blob> blobs = bucket.list();
        List<String> keys = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            // do something with the blob
            keys.add(blob.getName());
        }
        oBench.End();
        return keys;
    }

    @Override
    public void store(String key, File value) {
        oBench.Begin();
        BlobId blobId = BlobId.of(bucketName, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        try {
            Blob blob = storage.create(blobInfo, Files.readAllBytes(value.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        oBench.End();
    }

    @Override
    public void delete(String key) {
        oBench.Begin();
        BlobId blobId = BlobId.of(bucketName, key);
        storage.delete(blobId);
        oBench.End();
    }

    @Override
    public String  StorageName() {
        return "Google";
    }
}
