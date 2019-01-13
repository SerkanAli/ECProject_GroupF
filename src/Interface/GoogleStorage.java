package Interface;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GoogleStorage implements KeyValueInterface, Serializable {

    String bucketName = "ec-assignment";

    @Override
    public Object getValue(String key) {
        // Instantiate a Google Cloud Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Get specific file from specified bucket
        Blob blob = storage.get(BlobId.of(bucketName, key));

        return blob;
    }

    @Override
    public List<String> getKeys() {
        Storage storage = StorageOptions.getDefaultInstance().getService();

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
        Storage storage = StorageOptions.getDefaultInstance().getService();
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
        Storage storage = StorageOptions.getDefaultInstance().getService();

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
