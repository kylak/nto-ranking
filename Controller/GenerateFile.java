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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.io.FileReader;

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
			header += greekText + ")|Traduction (Martin 1707)|Référence|\n|-----|-----|:---:\n";
			writer.write(header);
			writer.close();
		} catch (IOException tt) {}
    }
    
    String createNameHLFile(String cat, int occ) {
    	return "../../View/" + cat + "/HapaxLegomenon (" + String.format("%02d", occ) + ").md";	
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
    
    boolean verseAlreadyAdded(String filename, String ref) {
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			//now read the file line by line...
			int lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNum++;  
				if(line.contains(ref)) return true;
			}
		} catch(FileNotFoundException e) {}
		return false;
    }
    
    int lineNumber(String path) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		return lines;
    } 
    
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
                System.out.println("new for");
                for (Verse temp : entry.getValue()) {
                
                	AtomicInteger i = new AtomicInteger(0);;
                	synchronized(i) {
					for (Float strongNmbr : temp.strongNumbers) {
						if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
							                	
							boolean first = false;
							String cat = "";
							char morphValue = temp.morph.get(i.get()).charAt(0);
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
							System.out.println("i: " + i.get() + " -> " + nameFile);
							if (/*first == false && */verseAlreadyAdded(nameFile, temp.ref.textFormat)) continue;
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
									// On ne prend pas en compte les hapax legomenon qui ne le sont qu'au niveau de catégorie grammaticale.
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
						i.incrementAndGet();
					}

              	}
              	}
         }
         
         // Partie concernant les hapax legomenon.
         	String nameToutFileHL = "";
            int allOcc = classifyProcess.uniqueThematicWords.size(); //
            if (allOcc > 0) {
               	nameToutFileHL = createNameHLFile("6. Tout", allOcc);
				newHLFile(nameToutFileHL);
            }

            for (HashMap.Entry<GreekStrong, Verse> entry : classifyProcess.uniqueThematicWords.entrySet()) {
            	GreekStrong strong = entry.getKey();

                	int i = 0;
					for (Float strongNmbr : entry.getValue().strongNumbers) {
					
						if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {	                	
							String cat = "";
							char morphValue = entry.getValue().morph.get(i).charAt(0);
							switch(morphValue) {
								case 'V':
									cat = "1. Verbes";
									break;
								case 'N':
									cat = "2. Noms";
									break;
								case 'A':
									cat = "4. Adjectifs";
									break;
								case 'D':
									cat = "3. Adverbes";
									break;
								default:
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
								FileWriter ToutWriterHL = new FileWriter(nameToutFileHL, true);
								String line2 = writeInHL(entry.getKey(), entry.getValue());
								ToutWriterHL.append(line2);
								ToutWriterHL.close();
							}
							catch (IOException t) {}
							break;
						}
						
						i++;
					}
					
              	}

            }
        
        catch (FileNotFoundException | UnsupportedEncodingException tt) {}
        
        // compter le nombre d'hapax
        String morphs[] = {"1. Verbes", "2. Noms", "4. Adjectifs", "3. Adverbes", "5. Autres (pour débogage)"};
        for (String morph : morphs) { 
        
        	String path = "../../View/" + morph + "/HapaxLegomenon.md";
      		File f1 = new File(path);
      		if(f1.exists()) {
				File f2 = new File(createNameHLFile(morph, lineNumber(path) - 2));
				boolean b = f1.renameTo(f2);
			}
			
			String path2 = "../../View/" + morph;
			int nbrF = new File( "../../View/" + morph + "/").listFiles().length - 1;
      		File f3 = new File(path2);
      		if(f3.isDirectory()) {
				File f4 = new File(path2 + " (" + nbrF + ")"); // -1 due to the hapax legomenon file.
				System.out.println(path2 + " (" + nbrF + ")");
				boolean b = f3.renameTo(f4);
			}

        }
        
        String path2 = "../../View/6. Tout";
		int nbrF = new File( "../../View/6. Tout/").listFiles().length - 1;
      	File f3 = new File(path2);
      	if(f3.isDirectory()) {
			File f4 = new File(path2 + " (" + nbrF + ")"); // -1 due to the hapax legomenon file.
			System.out.println(path2 + " (" + nbrF + ")");
			boolean b = f3.renameTo(f4);
		}

    }
}
