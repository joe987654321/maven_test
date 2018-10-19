package storeName;

import java.util.Date;
import java.util.List;

/**
 * Store list getter is an interface.
 */
public interface StoreListGetter {

    /**
     * get store list from files.
     *
     * @param currentDate date
     * @return List
     */
    List<StoreInfo> getStoreIdList(Date currentDate);

    /**
     * get process file list.
     *
     * @param currentDate current date
     * @return list
     */
    List<String> getProcessFileList(Date currentDate);

    void setProcessFileList(List<String> strings);
}
