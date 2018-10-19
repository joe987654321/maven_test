package storeName;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get information of "711".
 */
public class SevenListGetterImpl implements StoreListGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger("CRONJOB");

    private static final Charset CHARSET = Charset.forName("Big5");

    private static final int LINE_LENGTH = 137;
    private static final int STORE_ID_START = 1;
    private static final int STORE_ID_END = 6;

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

        // seven got two logistic files, one for normal seller, the other is vip seller only.

        List<StoreInfo> storeIdList = new ArrayList<>();

        if (!processFileList.isEmpty()) {
            for (String processFile: processFileList) {
                //System.out.println(parse(processFile));
                File f = new File(processFile);
                if (f.exists() && !f.isDirectory() && f.length() != 0) {
                    storeIdList.addAll(parse(processFile));
                }
            }
        } else {
            LOGGER.error("file not found. processFileList is empty.");
        }

        return storeIdList;

    }

    /**
     *  parser.
     *
     * @param line line
     * @param start start
     * @param end end
     * @return string
     */
    private String get(byte[] line, int start, int end) {
        return new String(line, start - 1, (end - start + 1), CHARSET);
    }

    /**
     *  parser file.
     * @param path file path
     * @return list
     */
    public List<StoreInfo> parse(String path) {

        List<StoreInfo> result = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] line = new byte[LINE_LENGTH + 2];
            while (true) {
                int len = fis.read(line, 0, line.length);
                if (len == -1) { // no more lines
                    fis.close();
                    return result;
                }
                if (len != line.length || line[line.length - 1] != '\n') {
                    throw new RuntimeException("bad line?");
                    // @TODO: maybe ignore bad line ?
                }

                String lineS = new String(line, CHARSET);
                String storeId = lineS.substring(STORE_ID_START, STORE_ID_END);
                String name = lineS.substring(STORE_ID_END, lineS.indexOf(" "));
                // byte 1 ~ byte 6 is storeId
                result.add(new StoreInfo(name, storeId));
            }
        } catch (Exception e) {
            return result;
        }
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

        sdFormat = new SimpleDateFormat("yyMMdd", Locale.ENGLISH); // 160328
        String date2 = sdFormat.format(currentDate);

//        String filePath1 = String.format("/home/y/var/logistics/seven/receive_file/%s/0120%s.STD", date1, date2);
        String filePath1 = "/Users/joe321/convfile/0120181014.STD";
        // /home/y/var/logistics/seven/receive_file/2016-03-29/0120160329.STD - seller
        // /home/y/var/logistics/seven/receive_file/2016-03-28/0120160328.STD - seller

        processFileList.add(filePath1);

//        String filePath2 = String.format("/home/y/var/logistics/seven/receive_file/%s/0120%s01.STD", date1, date2);
        String filePath2 = "/Users/joe321/convfile/012018101401.STD";
        // /home/y/var/logistics/seven/receive_file/2016-03-29/012016032901.STD - vip seller
        // /home/y/var/logistics/seven/receive_file/2016-03-28/012016032801.STD - vip seller
        processFileList.add(filePath2);

        return processFileList;
    }

}
