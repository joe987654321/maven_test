package filterFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class copyDirAndFilter {

    public static void main(String[] args) throws IOException {

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                System.out.println("#" + pathname.getPath() + " $$ " + pathname.getName());
                if (pathname.getName().equals(".git")) {
                    return false;
                }
                return true;
            }
        };

        File a = new File("aaa");
        File b = new File("bbb");
        FileUtils.copyDirectory(a,b, fileFilter);
    }
}
