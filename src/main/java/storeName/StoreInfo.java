package storeName;

public class StoreInfo {
    String storeName;
    String storeId;

    StoreInfo(String name, String id) {
        storeName = name;
        storeId = id;
    }

    public String toString() {
        return storeId + ":" + storeName;
    }
}
