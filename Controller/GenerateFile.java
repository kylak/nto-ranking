import java.io.File;         // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.HashMap;
import java.util.List; 
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public class GenerateFile {
  
    Classify classifyProcess;
    String greekText;
    
    public GenerateFile (Classify givenClassifyProcess, String givenGreekText) {
        classifyProcess = givenClassifyProcess;
        greekText = givenGreekText;
    }
    
    void generateFiles(HashMap<Reference, Verse> passagesTranslated) throws FileNotFoundException, UnsupportedEncodingException {
        
        try
        {
            for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : classifyProcess.interestingClassifiedVerses.entrySet()) {
                GreekStrong strong = entry.getKey();
                
                int nbreOfThisStrong = 0;
                for (Verse temp : entry.getValue()) {
                    for (Float strongNmbr : temp.strongNumbers) {
                        if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
                            nbreOfThisStrong++;
                        }
                    }
                }
                
                PrintWriter writer;
                if (entry.getKey().strongNumber == (int) entry.getKey().strongNumber) {
                    writer = new PrintWriter("../../View/(" + String.format("%02d", nbreOfThisStrong) + ") " + strong.unicode + " (n°" + (int) strong.strongNumber + ").md", "UTF-8");
                    
                }
                else {
                    writer = new PrintWriter("../../View/(" + String.format("%02d", nbreOfThisStrong) + ") " + strong.unicode + " (n°" + strong.strongNumber + ").md", "UTF-8");
                }
                strong.unicode = Normalizer.normalize(strong.unicode, Normalizer.Form.NFD);
                strong.unicode = strong.unicode.replaceAll("\\p{M}", "");
                ArrayList<Verse> value = entry.getValue();
                String header = "<h2 align=\"center\">" + strong.unicode.toUpperCase() + "</h2>\n\n|Texte grec (" + greekText + ")|Traduction (Martin 1707)|Réference|\n|-----|-----|:---:"; // Add then the KJV translation.
                writer.println(header);

                for (Verse temp : value) {
                    String translation = "";
                    for (Reference aaa : passagesTranslated.keySet()) {
                        if(aaa.textFormat.equals(temp.ref.textFormat)) {
                            translation = passagesTranslated.get(aaa).text;
                        }
                    }
                    String line = temp.text + "|" + translation + "|" + temp.ref.textFormat + "|";
                    writer.println(line);
                }
                writer.close();
            }

            PrintWriter writer2 = new PrintWriter("../../View/HapaxLegomenon.md", "UTF-8");
            String header = "|Greek word (with Strong number)|KJV translation|New Testament reference|\n|:---:|-----|:---:|";
            writer2.println(header);
            for (HashMap.Entry<GreekStrong, Verse> entry : classifyProcess.uniqueThematicWords.entrySet()) {
                String line = "";
                if (entry.getKey().strongNumber == (int) entry.getKey().strongNumber) {
                    String Formated_KJV_Def = "";
                    if (entry.getKey().KJV_Def != null) {
                        Formated_KJV_Def = entry.getKey().KJV_Def.replaceAll("\n", " ");
                    }
                    Formated_KJV_Def = Formated_KJV_Def.replaceAll("\t", " ");
                    line = entry.getKey().unicode + " (n°" + (int)entry.getKey().strongNumber + ")|" + Formated_KJV_Def + "|" + entry.getValue().ref.textFormat + "|";
                }
                else {
                    line = entry.getKey().unicode + " (n°" + entry.getKey().strongNumber + ")|?|" + entry.getValue().ref.textFormat + "|";
                }
                writer2.println(line);
            }
            writer2.close();
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
    /*
    void generateTSV_forInterestingClassifiedVerses(GreekStrong strong, HashMap.Entry<GreekStrong, ArrayList<Verse>> entry) {
        try {
            PrintWriter writer = new PrintWriter("../../View/" + strong.unicode + " (n°" + strong.strongNumber + ").tsv", "UTF-8");
            ArrayList<Verse> value = entry.getValue();
            String header = "Greek text\tNew Testament reference"; // Add then the KJV translation.
            writer.println(header);
            for (Verse temp : value) {
                String line = temp.text + "\t" + temp.ref.textFormat;
                writer.println(line);
            }
            writer.close();
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
    
    void generateTSV_forUniqueThematicWords() {
        try {
            PrintWriter writer2 = new PrintWriter("../../View/HapaxLegomenon.tsv", "UTF-8");
            String header = "Greek word (with Strong number)\tKJV translation\tNew Testament reference";
            writer2.println(header);
            for (HashMap.Entry<GreekStrong, Verse> entry : classifyProcess.uniqueThematicWords.entrySet()) {
                String Formated_KJV_Def = entry.getKey().KJV_Def.replaceAll("\n", " ");
                Formated_KJV_Def = Formated_KJV_Def.replaceAll("\t", " ");
                String line = entry.getKey().unicode + " (n°" + entry.getKey().strongNumber + ")\t" + Formated_KJV_Def + "\t" + entry.getValue().ref.textFormat;
                writer2.println(line);
            }
            writer2.close();
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
    */
}
