import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class GetMartinTranslation {
    
    static String locA = "/Users/gustavberloty/Documents/GitHub/";
    static String locB = "nto-ranking/Model/Data/";
    static String filename = "Bible Martin 1707.txt";
    static String source = "https://sites.google.com/view/martin1707"; // February 2020.
    
    HashMap<Reference, Verse> passagesTranslated = new HashMap<Reference, Verse>();
    
    public GetMartinTranslation(HashMap<Reference, Verse[]> passages) {
        for (Reference ref : passages.keySet()) {
            passagesTranslated.put(ref, getVerse(ref, -1));
        }
    }
    
    Verse getVerse(Reference ref, int indice) {
        
        String text = "";
        
        if (ref.textFormat.contains("-") && indice == -1) {
            Verse[] tabVerse = new Verse[ref.verse.length];
            for (int i = 0; i < ref.verse.length; i++) {
                tabVerse[i] = getVerse(ref, ref.verse[i]);
            }
            return concatVerse(ref, tabVerse);
        }
        else {
            text = get(ref, indice);
        }
        
        ArrayList<Float> strong = new ArrayList<Float>();
        ArrayList<String> morph = new ArrayList<String>();
        
        return new Verse(ref, text, strong, morph, source);
    }
    
    static String get(Reference ref, int indice) {
        try {
            String regEx = "\\X*?\\R";
            String url  = locA + locB + filename ;
            String translation = new String(Files.readAllBytes(Paths.get(url)));
            Pattern pattern = Pattern.compile(regEx, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(translation);

            while (matcher.find()) {
                String convertedRef = convertRefToMartinFileFormat(ref, indice);
                // System.out.println("convertedRef: " + convertedRef);
                if(matcher.group(0).contains(convertedRef)) {
                    // for markdown.
                    String verse = matcher.group(0).replaceAll("\\[|\\]", "_");
                    verse = verse.replaceAll(convertedRef + " ", "");
                    verse = verse.replaceAll("\n", "");
                    return verse;
                }
            }
        }
        catch (IOException e) {System.out.println("error");}
        return null;
    }
    
    static String convertRefToMartinFileFormat(Reference ref, int indice){
        indice = (indice==-1)?ref.verse[0]:indice;
        String Refbeginning;
        Refbeginning = ref.convertRefTo(RefFormat.MartinTranslation);
        return Refbeginning + " " + ref.chapter + ":" + indice;
    }
    
    Verse concatVerse(Reference ref, Verse[] tab) {
        
        String text = "";
        ArrayList<Float> strong = new ArrayList<Float>();
        ArrayList<String> morph = new ArrayList<String>();
        
        for (int i = 0; i < tab.length; i++) {
            text += tab[i].text + " ";
            strong.addAll(tab[i].strongNumbers);
            morph.addAll(tab[i].morph);
        }
        
        return new Verse(ref, text, strong, morph, source);
    }
}
