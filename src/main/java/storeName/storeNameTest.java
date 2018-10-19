package storeName;

import java.util.Date;
import java.util.List;

public class storeNameTest {

    public static void main(String[] args) {

        Date currentDate = new Date();

        List<StoreInfo> storeInfos = new FamilyListGetterImpl().getStoreIdList(currentDate);

        int length2 = storeInfos.size();
        System.out.println(storeInfos.subList(length2-10, length2));


        List<StoreInfo> storeInfos2 = new SevenListGetterImpl().getStoreIdList(currentDate);

        int length = storeInfos2.size();
        System.out.println(storeInfos2.subList(length-10, length));

        List<StoreInfo> storeInfos3 = new HiLifeListGetterImpl().getStoreIdList(currentDate);

        int length3 = storeInfos3.size();
        System.out.println(storeInfos3.subList(length3-10, length3));
    }
}
