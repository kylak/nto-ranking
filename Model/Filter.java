import java.util.ArrayList;
import java.util.regex.*;
import java.io.BufferedReader;
import java.text.DecimalFormat;

public class Filter {

    ArrayList<SyntacticRoles> roleToKeep; // List of the morphological syntactic roles to keep.
    
    public Filter() {
        roleToKeep = new ArrayList<SyntacticRoles>();
    }
   
    public Filter(SyntacticRoles[] role) {
        roleToKeep = new ArrayList<SyntacticRoles>();
        setRoles(role);
    }
    
    void setRoles(SyntacticRoles[] role) {
        for(int i = 0; i < role.length; i++) {
            roleToKeep.add(role[i]);
        }
    }
    
    void addARoleToKeep(SyntacticRoles role) {
        roleToKeep.add(role);
    }
    // vérifier que le texte que l'on prend à bien au final toutes les ponctuations.
    Verse applyOn(Verse givenVerse) {
        String new_text = givenVerse.text.replaceAll("\\.|,|;|·|\\(|\\)", " ");
        new_text = new_text.trim();
        String[] classifiedText = new_text.split(" +");
        /* for (String t : classifiedText) {
            System.out.println(t);
        } */
        //if (givenVerse.morph.size() == classifiedText.length && givenVerse.strongNumbers.size() == classifiedText.length) {
            String filteredText = "";
            ArrayList<String> filteredMorph = new ArrayList<String>();
            ArrayList<Float> filteredStrongNumbers = new ArrayList<Float>();
            for (int i = 0; i < classifiedText.length; i++) {
                if ( isThatRoleToBeKept(givenVerse.morph.get(i).charAt(0)) ) {
                    filteredText += classifiedText[i] + " ";
                    filteredMorph.add(givenVerse.morph.get(i));
                    filteredStrongNumbers.add(givenVerse.strongNumbers.get(i));
                }
                else {
                    // System.out.println("givenVerse.morph.get(i): " + givenVerse.morph.get(i));
                }
            }
            Verse verseFiltered = new Verse(givenVerse.ref, filteredText, filteredStrongNumbers, filteredMorph, givenVerse.source);
            return verseFiltered;
        /*}
        else {
            System.out.println("givenVerse: " + new_text);
            System.out.println("givenVerse.morph.size(): " + givenVerse.morph.size());
            System.out.println("classifiedText.length: " + classifiedText.length);
            System.out.println("givenVerse.strongNumbers.size(): " + givenVerse.strongNumbers.size());
            return null;
        }*/
    }
    
    boolean isThatRoleToBeKept(char role) {
        for (SyntacticRoles temp : roleToKeep) {
            if (temp.abbr.name().equals(String.valueOf(role))) {
                return true;
            }
        } 
        return false;
    }
    
}
