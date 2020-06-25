// package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;  
import java.util.ArrayList;
import java.util.Iterator;

public class Classify {      
    
	/*      
		Just below it's the Map of the reference we want to classify.      
		We have an array of Verse because we want to stock the normal verse
		as well as the formatted verse by the filer,      
		so Verse[] is an Array of two verses.
	*/
	HashMap<Reference, Verse[]> passages = new HashMap<Reference, Verse[]>();
    Filter filter;
    HashMap<GreekStrong, ArrayList<Verse>> interestingClassifiedVerses = new HashMap<GreekStrong, ArrayList<Verse>>();
    HashMap<GreekStrong, Verse> uniqueThematicWords = new HashMap<GreekStrong, Verse>(); // Si le strong est présent plusieurs fois dans le verset, ce strong n'est pas compté comme hapax. Si on veut qu'un tel mot soit compté comme hapax voir le commentaire ci-dessous pour changer la condition relative à la fonctionnalité.
    
    public Classify (HashMap<Reference, Verse[]> givenPassages) {
        passages = givenPassages;
        applyFilter();
        classify();
    }
     
    void setFilter() {
        filter = new Filter();
        List<SyntacticRoles> morph = Arrays.asList(
            SyntacticRoles.N, 
            SyntacticRoles.V, 
            SyntacticRoles.A, 
            SyntacticRoles.D);
        filter.roleToKeep.addAll(morph);
    }        
    
    void applyFilter() {
        setFilter();
        for (Reference i : passages.keySet()) {
            Verse[] versesSet = new Verse[2];
            versesSet[0] = passages.get(i)[0];  // On considère que le texte des versets à déjà été ajouté.
            String str = "";
            for (Float s : versesSet[0].strongNumbers) {
                str += Float.toString(s) + " ";
            }
            String mrph = "";
            for (String s : versesSet[0].morph) {
                mrph += s + " ";
            }
            // System.out.println(versesSet[0].ref.textFormat + " - " + versesSet[0].text + "\nstrong: " + str + "\nmorph: " + mrph + "\n");
            versesSet[1] = filter.applyOn(versesSet[0]);
            // System.out.println(i.textFormat + " - versesSet[1]: " + versesSet[1].text);
            passages.replace(i, versesSet);
        }
    }
    
    void classify() {
        HashMap<Reference, Verse[]> passagesForLoop = new HashMap<Reference, Verse[]>(passages);
        
        for (Reference r : passagesForLoop.keySet()) {
            
            // System.out.println("passagesForLoop.get(r)[1]: " + passagesForLoop.get(r)[1].text);
            
            // We loop on the filtered verse's words.
            for (int i = 0; i < passagesForLoop.get(r)[1].strongNumbers.size(); i++) {
                Float StrongToSearch = passagesForLoop.get(r)[1].strongNumbers.get(i);
                // System.out.println("StrongToSearch: " + StrongToSearch);
                ArrayList<Verse> versesHavingTheStrong = new ArrayList<Verse>();
                int nbreOccurenceTotal = 0;
                
                // An algorithmic variable to speed up the algorithm.
                boolean enterNowAlgo = false;
                int p = 0;
                /* We go through the verses, searching for all
                 passagesForLoop.get(r)[1].strongNumbers.get(i) Strong number occurences. */
                for (Reference seeThisRef : passagesForLoop.keySet()) {
                    // System.out.println("nouvelle référence de cheché: " + seeThisRef.textFormat);
                    /* We begin to go through the verses from the one at
                     the indice 'r' (from passagesForLoop.get(r)[1]) */
                    if (enterNowAlgo || (seeThisRef.textFormat).equals(r.textFormat)) { // this condition implied that we don't look back to the reference we previously checked.
                        
                        // We go through every verse's word.
                        for (int j = 0; j < passagesForLoop.get(seeThisRef)[1].strongNumbers.size() ; j++, p++) {
                            // System.out.println("Pour (" + StrongToSearch + ") - de " + seeThisRef.textFormat + ": " + passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j) + " - " + toString(Math.abs(StrongToSearch - passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j)) < 0.001));
                            // We check if the verse's word we are going through is that Strong number.
                            if (passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j) != null &&
                            Math.abs(StrongToSearch - passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j)) < 0.001 ) {
                                // We add this verse to the list of verse containing that Strong number.
                                // So versesHavingTheWord should at least contain passagesForLoop.get(r)[0].
                                /* System.out.println("strong: " + passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j));
                                System.out.println("les strongs: " + passagesForLoop.get(seeThisRef)[1].strongNumbers);
                                System.out.println(seeThisRef.textFormat + " - passagesForLoop.get(seeThisRef)[1].text: " + passagesForLoop.get(seeThisRef)[1].text); */
                                
                                if(versesHavingTheStrong.indexOf(passagesForLoop.get(seeThisRef)[0]) == -1) {
                                    versesHavingTheStrong.add(passagesForLoop.get(seeThisRef)[0]);
                                }
                                
                                /* We delete this Strong number occurence from that verse.
                                 This instruction normally speeds up the classify() algorithm.
                                 This instruction works with the enterNowAlgo variable. */
                                passagesForLoop.get(seeThisRef)[1].strongNumbers.remove(j);
                                j--;
                                if (seeThisRef == r && i > 0) {i--;}
                                nbreOccurenceTotal++;
                            }
                        }
                        // System.out.println("passagesForLoop.get(seeThisRef)[1].text: " + passagesForLoop.get(seeThisRef)[1].text);
                        // System.out.println("nombre de mot checké: " + p);
                        enterNowAlgo = true;
                    }
                }
                if (nbreOccurenceTotal == 1) { // Si on ne veut pas compter le nombre de fois que le strong est dans le verset de base : versesHavingTheStrong.size() == 1.
                    uniqueThematicWords.put(new GreekStrong(StrongToSearch), versesHavingTheStrong.get(0));
                }
                else {
                    interestingClassifiedVerses.put(new GreekStrong(StrongToSearch), new ArrayList<Verse>(versesHavingTheStrong));
                }
            }	
        }
        for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : interestingClassifiedVerses.entrySet()) {
            GreekStrong strong = entry.getKey();
            // System.out.println("strong.strongNumbers: " + strong.strongNumber);
        }
    }
    
    public String toString(boolean value) {
        return value ? "true" : "false";
    }
           
 }
