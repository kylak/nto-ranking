import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;


public class Start {
    
    public static void main(String[] args) {
        
        /*
        enum CriticalTexts {
            WH, NA28, SBL, RP, KJTR, ST//, BHP
        }
        */
        String greekText = "KJTR";
        GetPassages findings = new GetPassages("List_of_verses.csv", greekText);
        Classify classifiedVerses = new Classify(findings.passages);
        GenerateFile gf = new GenerateFile(classifiedVerses, greekText);
        try {
            gf.generateFiles(findings.passagesTranslated);
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
    
}
