package typesafe.test.joe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe321 on 2017/3/3.
 */
public class ParseTypeSafe {

    public static void main(String[] args) {
//        File apexConfFile = new File("./apex.conf");
//        Config typeSafeUserConf = ConfigFactory.parseFile(apexConfFile).resolve();
//
//        System.out.println(typeSafeUserConf.getString("doc_src_path"));
//        System.out.println(typeSafeUserConf.getString("review_url"));
//
//        String [] keys = {"aaa","bbb","ccc","ddd","eee"};
//        for (String key: keys) {
//            System.out.println(typeSafeUserConf.getString(key));
//        }

        File keyFile = new File("./key");
        Config typeSafeConf = ConfigFactory.parseFile(keyFile).resolve();

//
//        System.out.println(typeSafeConf.getAnyRef("keyProxy.content").getClass());
//        System.out.println(typeSafeConf.getString("keyProxy.mock"));
//        Map<String, String> map = (Map<String, String>) typeSafeConf.getAnyRef("keyProxy.content");
//        System.out.println(map);


        //docHostingOrg = ApexTest
        //docHostingRootUrlTemplate = "https://git.corp.yahoo.com/pages/"${docHostingOrg}"/${repoName}"
        String docHostingOrg = typeSafeConf.getString("git.docHostingOrg");
        String docHostingRootUrlTemplate = typeSafeConf.getString("git.docHostingRootUrlTemplate2");
        System.out.println(docHostingOrg);
        System.out.println(genRootUrl(docHostingRootUrlTemplate));
    }

    private static String genRootUrl(String docHostingRootUrlTemplate) {
        Map<String, String> map = new HashMap<>();
        map.put("repoName", "test_mkdocs");

        StrSubstitutor strSubstitutor = new StrSubstitutor(map);
        return strSubstitutor.replace(docHostingRootUrlTemplate) + "/site";
    }
}
