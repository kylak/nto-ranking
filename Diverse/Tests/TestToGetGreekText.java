import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FilenameFilter;

public class TestToGetGreekText {
    
    enum CriticalTexts {
        WH, NA28, SBL, RP, KJTR, ST, BHP
    }
    
    public static void main(String[] args) {
        
        
        File file = new File("/Users/gustavberloty/Documents/GitHub/greekNTO-classificationProgram/Model/Data/New Testament/");
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        
        String sourceName = "NA28";
        String verseNumber = "40005013";
        
        String text = "";
        
        // à implémenter : les variantes et le BHP+.
        try {
            
            String sourceNumber = "";
            if (CriticalTexts.valueOf(sourceName) instanceof CriticalTexts) {
                sourceNumber = "CT" + sourceName;
            }

            String dataFilename = verseNumber.substring(0, 5) + ".htm";
            String regex = "<a\\s*?href=index\\.htm\\?" + sourceNumber + "#" + verseNumber + ">" + sourceName + "<\\/a><\\/td><td>\\X+?<\\/td>\\X*?<td>(\\X+?)<\\/td>\\s*?(<tr|<\\/tbody>)";
            String Mt5_verses = new String(Files.readAllBytes(Paths.get("/Users/gustavberloty/Documents/GitHub/greekNTO-classificationProgram/Model/Data/New Testament/" + directories[0] + "/" + dataFilename)));
            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(Mt5_verses);
            while (matcher.find()) {
                text = matcher.group(1).replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            }
        }
        catch (IOException e) {System.out.println("error");}
        
        System.out.println(text);
    }
}
