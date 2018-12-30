package Interface;

import java.io.*;
import java.util.List;

public class GoogleStorage implements KeyValueInterface, Serializable {

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public List<String> getKeys() {
        return null;
    }

    @Override
    public void store(String key, Serializable value) {

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public String  StorageName() {
        return "Google";
    }
}
