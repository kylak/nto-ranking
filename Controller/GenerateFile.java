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
    	// Je ne comprends pas pourquoi la version ternaire ne fonctionne pas.
		if (strong.strongNumber == (int)(strong.strongNumber)) {
			file += (int) strong.strongNumber;
		}
		else {
			file += strong.strongNumber;
		}
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
    
    String createNameHLFile(String cat, int occ) {
    	return "../../View/" + cat + "/(" + String.format("%02d", occ) + ") HapaxLegomenon.md";	
    }
    
    void newHLFile(String fileName) {
    	try { 		
			FileWriter writer = new FileWriter(fileName);
			String header = "|Greek word (with Strong number)|KJV translation|New Testament reference|\n|:---:|-----|:---:|\n";
			writer.write(header);
			writer.close();
		} catch (IOException tt) {}
    }
    
    String writeInHL(GreekStrong s, Verse v) {
    	String line = "";
    	if (s.strongNumber == (int) s.strongNumber) {
        	String Formated_KJV_Def = "";
            if (s.KJV_Def != null) {
            	Formated_KJV_Def = s.KJV_Def.replaceAll("\n", " ");
            }
            Formated_KJV_Def = Formated_KJV_Def.replaceAll("\t", " ");
            line = s.unicode + " (n°" + (int)s.strongNumber + ")|" + Formated_KJV_Def + "|" + v.ref.textFormat + "|";
        }
        else {
        	line = s.unicode + " (n°" + s.strongNumber + ")|?|" + v.ref.textFormat + "|";
        }
        return line + "\n";
    }
    
    // s'occuper des hapax legomenon puis merge sur master.
    // ordonner les fichiers puis merge sur master.
    // afficher dans les titres de dossier le nombre de verbe, d'adjectifs, etc… puis merge sur master.
    
    // voir si le problème ne viendrait pas de passage de référence d'objet à des fonctions.
    // supprimer la notion d'index et le tableau de string ?
    
    // voir si le problème de synchronisation ne vient pas de ce fichier.
    // le problème de sychronisation vient ne pas seulement de ce fichier, s'il vient aussi de ce fichier.
    
    // les références d'objet que l'on envoie en paramètre : toujours les copier en local même quand on ne les modifie pas ?
    // (pour éviter qu'un processsus asynchrone ne modifie l'objet en cours de route ?).
    
    void generateFiles(HashMap<Reference, Verse> passagesTranslated) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        
        try
        {
            for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : classifyProcess.interestingClassifiedVerses.entrySet()) {
                
                GreekStrong strong = entry.getKey();
               	
                int s = 5; // Number of SemanticRoles accepted.
                boolean[] isFirst = new boolean[s + 1];
                Arrays.fill(isFirst, Boolean.TRUE);
                
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
                
                for (Verse temp : entry.getValue()) {
                
                	int i = 0;
					for (Float strongNmbr : temp.strongNumbers) {
						if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
							                	
							boolean first = false;
							String cat = "";
							char morphValue = temp.morph.get(i).charAt(0);
							switch(morphValue) {
								case 'V': // index = 0
									if (isFirst[0]) {
										first = true;
										isFirst[0] = false;
									}
									cat = "1. Verbes";
									break;
								case 'N': // index = 1
									if (isFirst[1]) {
										first = true;
										isFirst[1] = false;
									}
									cat = "2. Noms";
									break;
								case 'A': // index = 2
									if (isFirst[2]) {
										first = true;
										isFirst[2] = false;
									}
									cat = "4. Adjectifs";
									break;
								case 'D': // index = 3
									if (isFirst[3]) {
										first = true;
										isFirst[3] = false;
									}
									cat = "3. Adverbes";
									break;
								default:
									if (isFirst[4]) {
										first = true;
										isFirst[4] = false;
									}
									cat = "5. Autres (pour débogage)";
									break;
									// If we modify the number of SemanticRoles accepted, 
									// then we should add associated case to the present switch.
							}
							/* Partie je trouve à alléger
							 (faisable, il me semble, en utilisant PrintWriter à la place de FileWriter). */
							int occ = nbrOccurence(entry.getValue(), strong.strongNumber, morphValue);
							String nameFile = createNameFile(cat, strong, occ);
							if (first) {
								if (occ == 1) newHLFile("../../View/" + cat + "/HapaxLegomenon.md");
								else newFile(nameFile, strong);
							}
							String translation = "";
							for (Reference aaa : passagesTranslated.keySet()) {
								if(aaa.textFormat.equals(temp.ref.textFormat)) {
									translation = passagesTranslated.get(aaa).text;
								}
							}
							try {
								if (occ == 1) { // hapax legomenon
									FileWriter writerHL = new FileWriter("../../View/" + cat + "/HapaxLegomenon.md", true);
									String line = writeInHL(entry.getKey(), temp);
									writerHL.append(line);
									writerHL.close();
								}
								else {
									FileWriter writer = new FileWriter(nameFile, true);
									String line = temp.text + "|" + translation + "|" + temp.ref.textFormat + "|\n";
									writer.append(line);
									writer.close();
									// Pour le "Tout".
									FileWriter ToutWriter = new FileWriter(nameToutFile, true);
									ToutWriter.append(line);
									ToutWriter.close();
								}
							}
							catch (IOException t) {}
						}
						i++;
					}

              	}
         }
         
            int allOcc = classifyProcess.uniqueThematicWords.size();
            if (allOcc > 0) {
               	String nameToutFile = createNameHLFile("6. Tout", allOcc);
				newHLFile(nameToutFile);
            }
            //est-ce qu'un hapax l'est même si le mot est plusieurs fois dans le même verset ? si oui, est-ce géré ?

            for (HashMap.Entry<GreekStrong, Verse> entry : classifyProcess.uniqueThematicWords.entrySet()) {
            	GreekStrong strong = entry.getKey();
				int s = 4;
				boolean[] isFirst = new boolean[s + 1];
				Arrays.fill(isFirst, Boolean.TRUE);

                	int i = 0;
					for (Float strongNmbr : entry.getValue().strongNumbers) {
						if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
							                	
							boolean first = false;
							String cat = "";
							char morphValue = entry.getValue().morph.get(i).charAt(0);
							switch(morphValue) {
								case 'V':
									if (isFirst[0]) {
										first = true;
										isFirst[0] = false;
									}
									cat = "1. Verbes";
									break;
								case 'N':
									if (isFirst[1]) {
										first = true;
										isFirst[1] = false;
									}
									cat = "2. Noms";
									break;
								case 'A':
									if (isFirst[2]) {
										first = true;
										isFirst[2] = false;
									}
									cat = "4. Adjectifs";
									break;
								case 'D':
									if (isFirst[3]) {
										first = true;
										isFirst[3] = false;
									}
									cat = "3. Adverbes";
									break;
								default:
									if (isFirst[4]) {
										first = true;
										isFirst[4] = false;
									}
									cat = "5. Autres (pour débogage)";
									break;
									// If we modify the number of SemanticRoles accepted, 
									// then we should add associated case to the present switch.
							}
							File tmp = new File("../../View/" + cat + "/HapaxLegomenon.md");
							if (!tmp.exists()) {
								newHLFile("../../View/" + cat + "/HapaxLegomenon.md");
							}
							try {
								FileWriter writerHL = new FileWriter("../../View/" + cat + "/HapaxLegomenon.md", true);
								String line = writeInHL(entry.getKey(), entry.getValue());
								writerHL.append(line);
								writerHL.close();
								// Pour le "Tout".
								FileWriter ToutWriterHL = new FileWriter(createNameHLFile("6. Tout", allOcc), true);
								ToutWriterHL.append(line);
								ToutWriterHL.close();
							}
							catch (IOException t) {}
						}
						i++;
					}
					
              	}

            }
        
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
        
        // compter le nombre d'hapax

    }
}
