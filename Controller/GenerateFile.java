import java.io.File;         // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.HashMap;
import java.util.List; 
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Arrays;
import java.io.FileWriter;

public class GenerateFile {
  
    Classify classifyProcess;
    String greekText;
    
    public GenerateFile (Classify givenClassifyProcess, String givenGreekText) {
        classifyProcess = givenClassifyProcess;
        greekText = givenGreekText;
    }
    
    int nbrOccurence(ArrayList<Verse> verses, float strongNumber, char cat) {
    	int occurence = 0;
    	for (Verse temp : verses) {	
        	int i = 0;
        	for (Float strongNmbr : temp.strongNumbers) {
            	if (Math.abs(strongNumber - strongNmbr) < 0.001
            	&& temp.morph.get(i).charAt(0) == cat) {
            		occurence ++;
                }
                i++;
            }
        }
        return occurence;
    }
    
    String createNameFile(String cat, GreekStrong strong, int occ) {
    	String file = "../../View/" + cat + "/(" + String.format("%02d", occ) + ") " + strong.unicode + " (n°";
		file += (strong.strongNumber == (int) strong.strongNumber)
				? (int) strong.strongNumber : strong.strongNumber;
		return file + ").md";
    }
    
    void newFile(String nameFile, GreekStrong strong) throws IOException {
		try { 		
			FileWriter writer = new FileWriter(nameFile);
			String tmp = strong.unicode;					
			tmp = Normalizer.normalize(tmp, Normalizer.Form.NFD);
			tmp = tmp.replaceAll("\\p{M}", "");
			String header = "<h2 align=\"center\">" + tmp.toUpperCase() + "</h2>\n\n|Texte grec (";
			header += greekText + ")|Traduction (Martin 1707)|Réference|\n|-----|-----|:---:\n";
			writer.write(header);
			writer.close();
		} catch (IOException tt) {}
    }
    
    // s'occuper des hapax legomenon
    // ordonner les fichiers
    // afficher dans les titres de dossier le nombre de verbe, d'adjectifs, etc…
    // voir si le problème de synchronisation ne vient pas de ce fichier.
    
    void generateFiles(HashMap<Reference, Verse> passagesTranslated) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        
        try
        {
            for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : classifyProcess.interestingClassifiedVerses.entrySet()) {
                
                GreekStrong strong = entry.getKey();
               	
                int s = 5; // Number of SemanticRoles accepted.
                String[] writersTitle = new String[s + 1]; // 0: verbe, 1: nom, 2: adverbe, 3: adjectifs, 4: tout. 
                boolean[] isFirst = new boolean[s + 1];
                Arrays.fill(isFirst, Boolean.TRUE);
                String nameFile = "";
                
                int allOcc = 0;
                for (Verse temp : entry.getValue()) {
                    for (Float strongNmbr : temp.strongNumbers) {
                        if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
                            allOcc++;
                        }
                    }
                }
                String nameToutFile = createNameFile("6. Tout", strong, allOcc);
				newFile(nameToutFile, strong);
				writersTitle[writersTitle.length-1] = nameToutFile;
                
                for (Verse temp : entry.getValue()) {
                
                	int i = 0;
					for (Float strongNmbr : temp.strongNumbers) {
						if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
							                	
							boolean first = false;
							String cat = "";
							int index = 0;
							char morphValue = temp.morph.get(i).charAt(0);
							switch(morphValue) {
								case 'V': // index = 0
									if (isFirst[0]) {
										first = true;
										isFirst[0] = false;
									}
									cat = "1. Verbes";
									index = 0;
									break;
								case 'N': // index = 1
									if (isFirst[1]) {
										first = true;
										isFirst[1] = false;
									}
									cat = "2. Noms";
									index = 1;
									break;
								case 'A': // index = 2
									if (isFirst[2]) {
										first = true;
										isFirst[2] = false;
									}
									cat = "4. Adjectifs";
									index = 2;
									break;
								case 'D': // index = 3
									if (isFirst[3]) {
										first = true;
										isFirst[3] = false;
									}
									cat = "3. Adverbes";
									index = 3;
									break;
								default:
									if (isFirst[4]) {
										first = true;
										isFirst[4] = false;
									}
									System.out.println(morphValue + " : " + strong.unicode + " : " + temp.ref.textFormat);
									cat = "5. Autres";
									break;
									// If we modify the number of SemanticRoles accepted, 
									// then we should add associated case to the present switch.
							}
							int occ = nbrOccurence(entry.getValue(), strong.strongNumber, morphValue);
							nameFile = createNameFile(cat, strong, occ);
							if (first) {
								newFile(nameFile, strong);
								for(int k = 0; k < writersTitle.length; k++) {
									if (k == index) {
										writersTitle[k] = nameFile;
										break;
									} 
								}
							}
							String translation = "";
							for (Reference aaa : passagesTranslated.keySet()) {
								if(aaa.textFormat.equals(temp.ref.textFormat)) {
									translation = passagesTranslated.get(aaa).text;
								}
							}
							String line = temp.text + "|" + translation + "|" + temp.ref.textFormat + "|\n";
							// System.out.println(index);
							try {
								FileWriter writer = new FileWriter(nameFile, true);
								writer.append(line);
								writer.close();
								// Pour le "Tout".
								FileWriter ToutWriter = new FileWriter(writersTitle[writersTitle.length-1]);
								ToutWriter.append(line);
								ToutWriter.close();
							}
							catch (IOException t) {}
						}
						i++;
					}

              	}

            }

			/* ajouter les catégories
            PrintWriter writer2 = new PrintWriter("../../View/tout/HapaxLegomenon.md", "UTF-8");
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
            */
        }
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
    }
}
