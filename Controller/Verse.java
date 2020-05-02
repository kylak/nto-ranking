import java.util.List;
import java.util.ArrayList;

public class Verse {
    
    /*
     It could to seem crazy to have such proprerty variables for a Bible's verse.
     Therefore, any Bible's verse should be able to give
     his original language source (greek or latin or syriac, etc…) and each strong number.
     */
    
    final Reference ref;                     // The reference of the verse.
    final String text;                       // The verse itself.
    final ArrayList<GreekStrong> strongs;    // The verses's strongs.
    final ArrayList<Float> strongNumbers;  // The strong number for each verse's word.
    final ArrayList<String> morph;           // The morphology code for each verse's word.
    final String source;                     // Where does that verse come from (na28, tr, etc…).
    
    public Verse(Reference givenReference, String givenText, ArrayList<Float> givenStrongNumbers, ArrayList<String> givenMorph, String givenSource) {
        ref = givenReference;
        text = givenText;
        strongNumbers = new ArrayList<Float>(givenStrongNumbers);
        strongs = new ArrayList<GreekStrong>();
        for (int i = 0; i < strongNumbers.size(); i++) {
            strongs.add(new GreekStrong(strongNumbers.get(i)));
        }
        morph = new ArrayList<String>(givenMorph);
        source = givenSource;
    }
}
