package TarTest.joe;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by joe321 on 2017/3/13.
 */
public class UntarTest {

    public static void writeResourceAsExecutable(final InputStream inputStream, String outputFile) {
        File file = new File(outputFile);

        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            System.out.println("Creating file " + outputFile + " with byte " + bytes.length);
            Files.copy(new ByteArrayInputStream(bytes), Paths.get(outputFile), StandardCopyOption.REPLACE_EXISTING);

            File f = new File(outputFile);
            f.setExecutable(true);
        } catch (IOException e) {
            System.out.println("write file error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            //This code may not be work if the tar file does not contain only rdl binary
            TarArchiveInputStream tarInput =
                new TarArchiveInputStream(
                    new GzipCompressorInputStream(
                        new FileInputStream("/Users/joe321/Downloads/rdl-1.4.11-linux.tgz")
                    )
                );

            TarArchiveEntry entry = null;

            while ((entry = tarInput.getNextTarEntry()) != null) {
                //if (entry.getName().equals("rdl-gen-swagger")) {
                    System.out.println("find " + entry.getName());
                    writeResourceAsExecutable(tarInput, "/Users/joe321/bbb/" + entry.getName());
                    //break;
                //}
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
