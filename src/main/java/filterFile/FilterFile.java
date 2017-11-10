package filterFile;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joe321 on 2017/7/21.
 */
public class FilterFile {


    public static void main(String[] args) throws IOException {
//        File f = new File("fileWithIframe");
//        BufferedReader br = new BufferedReader(new FileReader(f));
//        String[] blackList = {"<iframe"};
//        String line;
//
//        StringBuilder filterContent = new StringBuilder("");
//        boolean firstLine = true;
//        while ((line = br.readLine()) != null) {
//            if (StringUtils.indexOfAny(line, blackList) == -1) {
//                if (!firstLine) {
//                    filterContent.append(System.lineSeparator());
//                } else {
//                    firstLine = false;
//                }
//                filterContent.append(line);
//            }
//        }
//
//        System.out.println("##"+filterContent.toString() +"##");

        File f = new File("file1");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        String line1 = null;

        StringBuilder filterContent = new StringBuilder("");
        while ((line = br.readLine()) != null) {
            line1 = line;
        }

        f = new File("file2");
        br = new BufferedReader(new FileReader(f));
        filterContent = new StringBuilder("");

        String line2 = null;
        while ((line = br.readLine()) != null) {
            line2 = line;
        }

        System.out.println(line1 + "##");
        System.out.println(line2 + "##");


    }
}
