package storeName;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get information of "family".
 */
public class FamilyListGetterImpl implements StoreListGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger("CRONJOB");

    public void setProcessFileList(List<String> processFileList) {
        this.processFileList = processFileList;
    }

    public List<String> processFileList = new ArrayList<>();

    /**
     * get store id list by date files.
     *
     * @param currentDate date
     * @return List
     */
    @NotNull
    public List<StoreInfo> getStoreIdList(Date currentDate) {

        if (processFileList.isEmpty()) {
            processFileList = getProcessFileList(currentDate);
        }

        // family got only one logistic file for now.

        List<StoreInfo> storeIdList = new ArrayList<>();

        if (!processFileList.isEmpty()) {
            String processFile = processFileList.get(0);
            // only 1 file for now.
            File f = new File(processFile);
            if (f.exists() && !f.isDirectory() && f.length() != 0) {
                storeIdList = parse(processFile);
            }
        }

        return storeIdList;

    }

    /**
     * parse file.
     *
     * @param path file path
     * @return list
     */
    public List<StoreInfo> parse(String path) {
        List<StoreInfo> result = new ArrayList<>();

        try {
            File inputFile = new File(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);
            List<Node> nodes = document.selectNodes("/doc/BODY/R00");
            // need to add dep for JAXEN to pom.xml
            for (Node node : nodes) {
                // result.add(node.selectSingleNode("StoreId").getText());

                String storeId = "F" + node.selectSingleNode("OLD_STORE").getText();
                String name = node.selectSingleNode("STORE_NAME").getText();
                result.add(new StoreInfo(name, storeId));

                // add the F + OLD_STORE as storeId to the result list.
                // ticket: https://jira.corp.yahoo.com/browse/NEVEC-4749
            }
        } catch (DocumentException e) {
            LOGGER.error("parse Family XML file error: " + e.getMessage());
            // Parsing XML file error, just ignore it.
        }

        return result;

    }

    /**
     * get process file list.
     *
     * @param currentDate current date
     * @return list
     */
    public List<String> getProcessFileList(Date currentDate) {

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // 2016-03-28
        String date1 = sdFormat.format(currentDate);

        sdFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH); // 20160328
        String date2 = sdFormat.format(currentDate);

//        String filePath = String.format("/home/y/var/logistics/fami/receive_file/%s/R00YAHDFM%s.XML", date1, date2);
        String filePath = "/Users/joe321/convfile/R00YAHDFM20181014.XML";
        // family: /home/y/var/logistics/fami/receive_file/2016-03-10/R00YAHDFM20160310.XML

        processFileList.add(filePath);

        return processFileList;
    }
}
