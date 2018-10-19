package storeName;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get HiLife cvstore is list
 */
public class HiLifeListGetterImpl implements StoreListGetter {

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
                storeIdList = parse(processFile, currentDate);
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
    public List<StoreInfo> parse(String path, Date currentDate) {

        List<StoreInfo> result = new ArrayList<>();

        try {
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.setTime(currentDate);
            tomorrow.add(Calendar.DAY_OF_YEAR, +1);

            Calendar closeDate = Calendar.getInstance(TimeZone.getTimeZone("UTC+8"));
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // 2016-03-28

            // xml file for HiLife
            File inputFile = new File(path);

            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);
            List<Node> nodes = new ArrayList<>(document.selectNodes("/doc/BODY/H00"));
            nodes.addAll(document.selectNodes("/DOC/BODY/H00"));
            nodes.addAll(document.selectNodes("/dco/BODY/H00"));
            // need to add dep for JAXEN to pom.xml

            for (Node node : nodes) {
                Node closeDateNode = node.selectSingleNode("STORE_CLOSE_DATE");
                if (closeDateNode != null) {
                    try {
                        String closeDateText = closeDateNode.getText();
                        if (!closeDateText.isEmpty()) {
                            closeDate.setTime(sdFormat.parse(closeDateText));
                            if (closeDate.before(tomorrow)) {
                                continue; //this store will be close tomorrow, remove it now
                            }
                        }
                    } catch (ParseException e) {
                        LOGGER.error("parse date error, assume that the store is open", e);
                    }
                }

                String storeId = node.selectSingleNode("SHOPID").getText();
                String name = node.selectSingleNode("SHOPNA").getText();
                result.add(new StoreInfo(name, storeId));
                // ticket: https://jira.corp.yahoo.com/browse/NEVEC-5575
                // cause this is a daily cronjob run at 22:30 CST,
                // so we will close the store which STORE_CLOSE_DATE is tomorrow.
            }

        } catch (DocumentException e) {
            LOGGER.error("parse HiLife XML file error: " + e.getMessage());
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

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMM", Locale.ENGLISH); // 2016-03-28
        String date1 = sdFormat.format(currentDate);

        sdFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH); // 20160328
        String date2 = sdFormat.format(currentDate);

//        String filePath = String.format("/home/y/var/nevec_egs_bastet/CVStore/HiLife/%s/H00YSTHIL%s.xml", date1, date2);
        String filePath = "/Users/joe321/convfile/H00YSTHIL20181014.xml";
        // HiLife: /home/y/var/nevec_egs_bastet/CVStore/HiLife/201612/H00YSTHIL20161206.xml

        processFileList.add(filePath);
        // only 1 file for HiLife.

        return processFileList;
    }

}
