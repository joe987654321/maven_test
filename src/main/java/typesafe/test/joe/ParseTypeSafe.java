package typesafe.test.joe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        File keyFile = new File("./file1");
        Config typeSafeConf = ConfigFactory.parseFile(keyFile).resolve();

//
//        System.out.println(typeSafeConf.getAnyRef("keyProxy.content").getClass());
//        System.out.println(typeSafeConf.getString("keyProxy.mock"));
//        Map<String, String> map = (Map<String, String>) typeSafeConf.getAnyRef("keyProxy.content");
//        System.out.println(map);


        //docHostingOrg = ApexTest
        //docHostingRootUrlTemplate = "https://git.corp.yahoo.com/pages/"${docHostingOrg}"/${repoName}"
//        String docHostingOrg = typeSafeConf.getString("git.docHostingOrg");
//        String docHostingRootUrlTemplate = typeSafeConf.getString("git.docHostingRootUrlTemplate2");
//        System.out.println(docHostingOrg);
//        System.out.println(genRootUrl(docHostingRootUrlTemplate));

//        Config certConfig = typeSafeConf.getConfig("bastet.athenz.allowed_cert_cn");
//        System.out.println(certConfig);
//        Config roleConfig = certConfig.getConfig("\"nevec.bastet.webapp:role.access.bastet.beta.without_user_auth\"");
//        System.out.println(roleConfig.getBoolean("checkCookie"));



        Map<String, Object> aaa = new HashMap<>();

        Map<String, Map<String, Object>> imageProfileConfigs = new HashMap<>();
        List<String> imageProfiles = typeSafeConf.getStringList("imageProfiles");

        for (String imageProfile: imageProfiles) {
            imageProfileConfigs.put(imageProfile, (Map<String, Object>) typeSafeConf.getAnyRef(imageProfile));
        }

        Map<String, Object> filedata = imageProfileConfigs.get("tw-ec00-0");
        final Pattern XY_PATTERN = Pattern.compile("^([0-9.]+)x([0-9.]+)");

        final Map<String, Map<String, String>> dims = new HashMap<String, Map<String, String>>();
        if (filedata != null) {
            final Map<String, Object> image = (Map<String, Object>) filedata.get("image");
            final Map<String, Object> dimension = (Map<String, Object>) image.get("dimension");
            final Map<String, Object> global = (Map<String, Object>) dimension.get("GLOBAL");

            // convert params
            for (final String name: dimension.keySet()) {
                if (!"GLOBAL".equalsIgnoreCase(name)) {
                    final Map<String, Object> dim = (Map<String, Object>) dimension.get(name);
                    dim.putAll(global);

                    final Map<String, String> params = new HashMap<String, String>();
                    for (final String key: dim.keySet()) {
                        if ("quality".equalsIgnoreCase(key)) {
                            params.put("q", String.valueOf(dim.get(key)));
                        } else if ("crop".equalsIgnoreCase(key) && (Boolean) dim.get("crop")) {
                            params.put("fi", "fill");
                        } else if ("width".equalsIgnoreCase(key)) {
                            params.put("w", String.valueOf(dim.get(key)));
                        } else if ("height".equalsIgnoreCase(key)) {
                            params.put("h", String.valueOf(dim.get(key)));
                        } else if ("sharpen".equalsIgnoreCase(key)) {
                            final Matcher matcher = XY_PATTERN.matcher((String) dim.get("sharpen"));
                            if (matcher.find()) {
                                params.put("sr", matcher.group(1));
                                params.put("ss", matcher.group(2));
                            }
                        } else if ("auto_orientation".equalsIgnoreCase(key)
                                && Integer.parseInt(dim.get("auto_orientation").toString()) == 2) {
                            params.put("rotate", "auto");
                        }
                    }
                    dims.put(name, params);
                }
            }
        }

        System.out.println(dims);
    }

    private static String genRootUrl(String docHostingRootUrlTemplate) {
        Map<String, String> map = new HashMap<>();
        map.put("repoName", "test_mkdocs");

        StrSubstitutor strSubstitutor = new StrSubstitutor(map);
        return strSubstitutor.replace(docHostingRootUrlTemplate) + "/site";
    }
}
