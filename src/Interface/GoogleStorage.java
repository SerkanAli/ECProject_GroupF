package Interface;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
    public Object getValue(String key) {
        // Get specific file from specified bucket
        Blob blob = storage.get(BlobId.of(bucketName, key));

        return blob;
    }

    @Override
    public List<String> getKeys() {
        // Creates the new bucket
        Bucket bucket = storage.get( bucketName);

        Page<Blob> blobs = bucket.list();
        List<String> keys = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            // do something with the blob
            keys.add(blob.getName());
        }

        return keys;
    }

    @Override
    public void store(String key, Serializable value) {
        BlobId blobId = BlobId.of(bucketName, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        try {
            Blob blob = storage.create(blobInfo, value.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        BlobId blobId = BlobId.of(bucketName, key);
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            // the blob was deleted
        } else {
            // the blob was not found
        }
    }

    @Override
    public String  StorageName() {
        return "Google";
    }
}
