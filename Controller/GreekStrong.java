import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GreekStrong {
    
    float strongNumber;
    String unicode;
    String translit;
    String pronunciation;
    String defStrong;
    String KJV_Def;
    String anOtherLanguageDef;
    float strongRef;

    public GreekStrong(float number) {
        strongNumber = number;
        fillData();
    }
    
    public void fillData() {
        // Create matcher on file
        try {
            if (strongNumber == (int)(strongNumber)) {
                String regEx = "<strongs>" + (int)strongNumber + "<\\/strongs>\\s*?<greek\\s*?BETA=\"\\X+?\"\\s*?unicode=\"(\\X+?)\"\\s*?translit=\"(\\X+?)\"\\/>\\s*?<pronunciation\\s*?strongs=\"(\\X+?)\"\\/>\\s+?<strongs_derivation>\\X+?<strongsref\\s*?language=\"\\X+?\"\\s*?strongs=\"(\\X+?)\"\\/>\\X+?<\\/strongs_derivation><strongs_def>\\s*?(\\X+?)<\\/strongs_def><kjv_def>:--(\\X+?).<\\/kjv_def>";
                String dictionary = new String(Files.readAllBytes(Paths.get("/Users/gustavberloty/Documents/GitHub/nto-ranking/Model/Data/Strongs dictionary/strongsgreek.xml")));
                Pattern pattern = Pattern.compile(regEx, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(dictionary);
                // System.out.println(matcher.group(1));
                // Find all matches
                while (matcher.find()) {
                    // Get the matching string
                    unicode = matcher.group(1);
                    translit = matcher.group(2);
                    pronunciation = matcher.group(3);
                    defStrong = matcher.group(5);
                    KJV_Def = matcher.group(6);
                    strongRef = Float.parseFloat(matcher.group(4));
                }
            }
        }
        catch (IOException e) {System.out.println("error");}
    }
}
