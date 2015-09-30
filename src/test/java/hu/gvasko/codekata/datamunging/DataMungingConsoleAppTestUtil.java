package hu.gvasko.codekata.datamunging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * Created by gvasko on 2015.09.30..
 */
public class DataMungingConsoleAppTestUtil {
    static String runApp(String[] arguments) throws Exception {
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        String encoding = Charset.defaultCharset().name();

        PrintStream testOutput = new PrintStream(tempStream, true, encoding);
        System.setOut(testOutput);
        System.setErr(testOutput);

        DataMungingConsoleApp.main(arguments);
        return tempStream.toString(encoding);
    }
}
