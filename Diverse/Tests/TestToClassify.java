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


public class TestToClassify {
    
    
    public static void testGivenAVerse_WhenApplyOnFilterOnIt_ThenTheVerseIsFiltered() throws FileNotFoundException, UnsupportedEncodingException {
    
        // So we use Col 2:6 to test the filter.
        
        HashMap<Reference, Verse[]> passages = new HashMap<Reference, Verse[]>();
        
            Reference referenceTest = new Reference("Col 2:6");
            String greekText = "ωσ ουν παρελαβετε τον χν ιν τον κν εν αυτω περιπατειτε";
        ArrayList<Float> strongNumbers = new ArrayList<Float>(Arrays.asList(5613.0f, 3767.0f, 3880.0f, 3588.0f, 5547.0f, 2424.0f, 3588.0f, 2962.0f, 1722.0f, 846.0f, 4043.0f));
            ArrayList<String> morphCode = new ArrayList<String>(Arrays.asList("CS", "CC", "V-IAA2P", "EA-AMS", "N-AMS", "N-AMS", "EA-AMS", "N-AMS", "P", "RP-3DMS", "V-MPA2P"));
        Verse verseTest = new Verse(referenceTest, greekText, strongNumbers, morphCode, "BHP+");
        
        Verse[] setVerseTest = new Verse[2];
        setVerseTest[0] = verseTest;
        passages.put(referenceTest, setVerseTest);
        
            Reference referenceTest2 = new Reference("Eph 5:2");
            String greekText2 = "και περιπατειτε εν αγαπη καθωσ και ο χσ ηγαπησεν ημασ και παρεδωκεν εαυτον υπερ ημων προσφοραν και θυσιαν τω θω εισ οσμην ευωδιασ";
        ArrayList<Float> strongNumbers2 = new ArrayList<Float>(Arrays.asList(2532.0f, 4043.0f, 1722.0f, 26.0f, 2531.0f, 2532.0f, 3588.0f, 5547.0f, 25.0f, 1473.0f, 2532.0f, 3860.0f, 1438.0f, 5228.0f, 1473.0f, 4376.0f, 2532.0f, 2378.0f, 3588.0f, 2316.0f, 1519.0f, 3744.0f, 2175.0f));
            ArrayList<String> morphCode2 = new ArrayList<String>(Arrays.asList("CC", "V-MPA2P", "P", "N-DFS", "D", "D", "EA-NMS", "N-NMS", "V-IAA3S", "RP-1AP", "CC", "V-IAA3S", "RE-3AMS", "P", "RP-1GP", "N-AFS", "CC", "N-AFS", "EA-DMS", "N-DMS", "P", "N-AFS", "N-GFS"));
            Verse verseTest2 = new Verse(referenceTest2, greekText2, strongNumbers2, morphCode2, "BHP+");
        
        Verse[] setVerseTest2 = new Verse[2];
        setVerseTest2[0] = verseTest2;
        passages.put(referenceTest2, setVerseTest2);
           
        // We have to implement the translation for that
        // Classify classifyTest = new Classify(passages);
        
        // We test if the filter worked well.
        System.out.println("\nAfter filtering 1: \n\t" + classifyTest.passages.get(referenceTest)[1].text);
        System.out.println("After filtering 2: \n\t" + classifyTest.passages.get(referenceTest2)[1].text + "\n");
        
        System.out.println("dd : " + classifyTest.uniqueThematicWords);
        GenerateFile gf = new GenerateFile(classifyTest, "ST");
        
        try {
            gf.generateFiles();
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
    
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            testGivenAVerse_WhenApplyOnFilterOnIt_ThenTheVerseIsFiltered();
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
}
