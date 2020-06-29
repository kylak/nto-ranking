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
import java.util.Collections;
import java.io.FileFilter;

public class GenerateFile {
  
    Classify classifyProcess;
    String greekText;
	
    public GenerateFile (Classify givenClassifyProcess, String givenGreekText) {
        classifyProcess = givenClassifyProcess;
        greekText = givenGreekText;
    }
    
    int nbrOccurence(ArrayList<Verse> verses, float strongNumber, char cat) {
    	int occurence = 0, i = 0;
    	Object process1 = new Object();
    	for (Verse temp : verses) {
    		synchronized(process1) {
        		i = 0;
        	}
        	synchronized(process1) {
        		Object process11 = new Object();
				for (Float strongNmbr : temp.strongNumbers) {
					synchronized(process11) {
						if (Math.abs(strongNumber - strongNmbr) < 0.001
						&& temp.morph.get(i).charAt(0) == cat) {
							occurence ++;
						}
					}
					synchronized(process11) {
						i++;
					}
				}
			}
        }
        return occurence;
    }
    
    String createNameFile(String cat, GreekStrong strong, int occ) {
    	String file, number, unicode;
    	float strongNumber;
    	int castedStrongNumber;
    	Object process1 = new Object();
    	synchronized(process1) {
    		number = String.format("%02d", occ);
    		unicode = strong.unicode;
    		strongNumber = strong.strongNumber;
    		castedStrongNumber = (int) strong.strongNumber;
    	}
    	synchronized(process1) {
    		file = "../../View/" + cat + "/(" + number + ") " + unicode + " (n°";
    	}
    	// Je ne comprends pas pourquoi la version ternaire ne fonctionne pas.
    	// file += (strongNumber == castedStrongNumber) ? castedStrongNumber : strongNumber;
    	synchronized(process1) {
    		if (strongNumber == castedStrongNumber)
				file += castedStrongNumber;
			else file += strongNumber;
    	}
    	synchronized(process1) {
			return file + ").md";
		}
    }
    
    void newFile(String nameFile, GreekStrong strong) throws IOException {
		try { 		
			Object process1 = new Object();
			FileWriter writer;
			String tmp, header = "";
			synchronized(process1) {
				writer = new FileWriter(nameFile);
				tmp = strong.unicode;
			}		
			synchronized(process1) {			
				tmp = Normalizer.normalize(tmp, Normalizer.Form.NFD);
			}
			synchronized(process1) {
				tmp = tmp.replaceAll("\\p{M}", "");
			}
			synchronized(process1) {
				header = "<h2 align=\"center\">" + tmp.toUpperCase() + "</h2>\n\n|Texte grec (";
			}
			synchronized(process1) {
				header += greekText + ")|Traduction (Martin 1707)|Référence|\n|-----|-----|:---:\n";
			}
			synchronized(process1) {
				writer.write(header);
			}
			synchronized(process1) {
				writer.close();
			}
		} catch (IOException tt) {}
    }
    
    String createNameHLFile(String cat, int occ) {
    	return "../../View/" + cat + "/HapaxLegomenon (" + String.format("%02d", occ) + ").md";	
    }
    
    void newHLFile(String fileName) {
    	try { 		
			FileWriter writer = new FileWriter(fileName);
			Object process1 = new Object();
			synchronized(process1) {
				writer.write("|Greek word (with Strong number)|KJV translation|New Testament reference|\n|:---:|-----|:---:|\n");
			}
			synchronized(process1) {
				writer.close();
			}
		} catch (IOException tt) {}
    }
    
    String writeInHL(GreekStrong s, Verse v) {
    	String line = "";
    	if (s.strongNumber == (int) s.strongNumber) {
    		Object process1 = new Object();
    		String Formated_KJV_Def = "";
			synchronized(process1) {
				if (s.KJV_Def != null) {
					Object subprocess1 = new Object();
					String KJV_Def_tmp = "";
					synchronized(subprocess1) {
						KJV_Def_tmp = s.KJV_Def;
					}
					synchronized(subprocess1) {
						Formated_KJV_Def = KJV_Def_tmp.replaceAll("\n", " ");
					}
				}
			}
			synchronized(process1) {
            	Formated_KJV_Def = Formated_KJV_Def.replaceAll("\t", " ");
            }
            synchronized(process1) {
            	line = s.unicode + " (n°" + (int)s.strongNumber + ")|" + Formated_KJV_Def + "|" + v.ref.textFormat + "|";
            }
        }
        else {
        	line = s.unicode + " (n°" + s.strongNumber + ")|?|" + v.ref.textFormat + "|";
        }
        return line + "\n";
    }
    
    boolean verseAlreadyAdded(String filename, String ref) { //here
		File file;
		Object process1 = new Object();
		synchronized(process1) {
			file = new File(filename);
		}
		try {
			Scanner scanner;
			synchronized(process1) {
				scanner = new Scanner(file);
			}
			//now read the file line by line...
			synchronized(process1) {
				Object subprocess1 = new Object();
				String line = "";
				while (scanner.hasNextLine()) {
					synchronized(subprocess1) {
						line = scanner.nextLine();
					}
					synchronized(subprocess1) { 
						if(line.contains(ref)) return true;
					}
				}
			}
		} catch(FileNotFoundException e) {}
		return false;
    }
    
    int lineNumber(String path) throws FileNotFoundException, IOException {
		BufferedReader reader;
		int lines;
		Object process1 = new Object();
		synchronized(process1) {
			reader = new BufferedReader(new FileReader(path));
			lines = 0;
		}
		synchronized(process1) {
			while (reader.readLine() != null) lines++;
		}
		synchronized(process1) {
			reader.close();
		}
		synchronized(process1) {
			=return lines;
		} 
    } 
    
    String boldStrong(String text) {
    	return text;
    }
    
    // voir si le problème ne viendrait pas de passage de référence d'objet à des fonctions.
    
    // le problème de sychronisation vient ne pas seulement de ce fichier, s'il vient aussi de ce fichier.
    
    // les références d'objet que l'on envoie en paramètre : toujours les copier en local même quand on ne les modifie pas ?
    // (pour éviter qu'un processsus asynchrone ne modifie l'objet en cours de route ?).
    
    
    // essayer de bien repositionner les déclarations d'objets pour que le programme est bien le temps de les créer synchroniser sur un objet envoyé en paramètre pour la création de l'objet generateFile ?    
    void generateFiles(HashMap<Reference, Verse> passagesTranslated) throws FileNotFoundException, UnsupportedEncodingException, IOException{
    	
        Object process = new Object();
		synchronized(process) {
		
			try
			{
				final Object process1 = new Object();
				
				synchronized(process1) {
	
					GreekStrong strong;
					int allOcc;
					String nameToutFile;
					boolean[] isFirst;
					
					// IMPLEMENTER (AVEC LA SYNCHRONICITE) TOUTES LES CLES EN GLOBAL (ET EN FINAL?). (avec un tableau "processus", genre processus[i])
							
					final Object subprocess1 = new Object();
						
					// Cela permet de s'assurer que les variables existent bien avant d'entrer dans la boucle.
					synchronized(subprocess1) {
						strong = null;
						nameToutFile = null;
						isFirst = null;
					}
							
					for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : classifyProcess.interestingClassifiedVerses.entrySet()) {
							
						synchronized(subprocess1) {
							// **** SUBPROCESS 1.1 ****
							final Object subprocess11 = new Object();
							synchronized(subprocess11) {
								strong = entry.getKey();
								allOcc = 0;
							}				
							synchronized(subprocess11) {
								for (Verse temp : entry.getValue()) {
									// Should we synchronize here ? or move the declaration before ?
									final Object subprocess111 = new Object();
									for (Float strongNmbr : temp.strongNumbers) {
										synchronized(subprocess111) {
											final Object subprocess1111 = new Object();
											float strNumber, diffNumber;
											synchronized(subprocess1111) {
												strNumber = strong.strongNumber;
											}
											synchronized(subprocess1111) {
												diffNumber = strNumber - strongNmbr;
											}
											synchronized(subprocess1111) {
												if (Math.abs(diffNumber) < 0.001) allOcc++;
											}
										}
									}
								}
							}
							synchronized(subprocess11) {
								nameToutFile = createNameFile("6. Tout", strong, allOcc);
							}
							synchronized(subprocess11) {
								newFile(nameToutFile, strong);
							}
							// **********************
								
								
							// **** SUBPROCESS 1.2 ****
							final Object subprocess12 = new Object();
							int s = 0;
							synchronized(subprocess12) {
								s = 5; // Number of SemanticRoles accepted.
							}
							synchronized(subprocess12) {
								isFirst = new boolean[s + 1];
							}
							synchronized(subprocess12) {
								Arrays.fill(isFirst, Boolean.TRUE);
							}
							// **********************
						}
							
						synchronized(subprocess1) {
								
							final Object subprocess11 = new Object(); // implémenter en synchronisé ?
							final Object subprocessY = new Object();
							boolean first; String cat, translation, nameFile, line; char morphValue; int occ;
							FileWriter writer, ToutWriter, writerHL;
							for (Verse temp : entry.getValue()) {
								
								synchronized(subprocess11) {
									int i;
									final Object subprocess111;
									final Object subprocess112;
									synchronized(subprocessY) {
										i = 0;
										subprocess111 = new Object();
										subprocess112 = new Object();
										for (Reference aaa : passagesTranslated.keySet()) {
											if(aaa.textFormat.equals(temp.ref.textFormat)) {
												translation = passagesTranslated.get(aaa).text;
											}
										}
									}
									synchronized(subprocessY) {
										for (Float strongNmbr : temp.strongNumbers) {
											synchronized(subprocess111) {
												if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) { // strong nécéssite subprocess 1	
													synchronized(subprocess112) {				
														first = false;
														cat = "";
														translation = "";
														morphValue = temp.morph.get(i).charAt(0);
													}
													synchronized(subprocess112) {
														final Object subprocess1121 = new Object();
														synchronized(subprocess1121) {
															switch(morphValue) {
																case 'V': // index = 0
																	if (isFirst[0]) { // nécéssite subprocess 2 done.
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
														}
														synchronized(subprocess1121) {
															/* Partie je trouve à alléger
															 (faisable, il me semble, en utilisant PrintWriter à la place de FileWriter). */
															occ = nbrOccurence(entry.getValue(), strong.strongNumber, morphValue); // strong nécéssite subprocess 1
														}
														synchronized(subprocess1121) {
															nameFile = createNameFile(cat, strong, occ); // strong nécéssite subprocess 1
														}
														synchronized(subprocess1121) {
															// System.out.println("i: " + i.get() + " -> " + nameFile);
															if (/*first == false && */verseAlreadyAdded(nameFile, temp.ref.textFormat)) continue;
															if (first) {
																if (occ == 1) newHLFile("../../View/" + cat + "/HapaxLegomenon.md");
																else newFile(nameFile, strong); // strong nécéssite subprocess 1
															}
														}
														synchronized(subprocess1121) {
															try {
																if (occ == 1) { // hapax legomenon
																	final Object subprocess11211 = new Object();
																	synchronized(subprocess11211) {
																		writerHL = new FileWriter("../../View/" + cat + "/HapaxLegomenon.md", true);
																		line = writeInHL(entry.getKey(), temp);
																	}
																	synchronized(subprocess11211) {
																		writerHL.append(line);
																	}
																	synchronized(subprocess11211) {
																		writerHL.close();
																	}
																		// On ne prend pas en compte les hapax legomenon qui ne le sont qu'au niveau de catégorie grammaticale.
																}
																else {
																	final Object subprocess11212 = new Object();
																	synchronized(subprocess11212) {
																		writer = new FileWriter(nameFile, true);
																		line = temp.text + "|" + translation + "|" + temp.ref.textFormat + "|\n";
																	}
																	synchronized(subprocess11212) {
																		writer.append(line);
																	}
																	synchronized(subprocess11212) {
																		writer.close();
																	}
																	// Pour le "Tout".
																	synchronized(subprocess11212) {
																		ToutWriter = new FileWriter(nameToutFile, true); // nameToutFile nécéssite subprocess 1
																	}
																	synchronized(subprocess11212) {
																		ToutWriter.append(line);
																	}
																	synchronized(subprocess11212) {
																		ToutWriter.close();
																	}
																}
															}
															catch (IOException t) {}
														}
													}
												}
											} // synchronized subprocess111
											synchronized(subprocess111) {
												i++;
											}
										} // for loop
									}
								} // synchronized subprocess11		
							} // for loop
						} // synchronized subprocess1	
					} // for loop
				} // synhcronized process1
				
				// Partie concernant les hapax legomenon.
				synchronized(process1) {
				
					String nameToutFileHL, cat, line, line2 = "";
					char morphValue; int allOcc, i;
					File tmp; FileWriter writerHL;
					FileWriter ToutWriterHL; GreekStrong strong;
					final Object subprocess1 = new Object();
					
					synchronized(subprocess1) {
						nameToutFileHL = "";
						allOcc = classifyProcess.uniqueThematicWords.size(); //
					}
					synchronized(subprocess1) {
						final Object subprocess11 = new Object();
						if (allOcc > 0) {
							synchronized(subprocess11) {
								nameToutFileHL = createNameHLFile("6. Tout", allOcc);
							}
							synchronized(subprocess11) {
								newHLFile(nameToutFileHL);
							}
						}
					}
					synchronized(subprocess1) {
						final Object subprocess11 = new Object();
						for (HashMap.Entry<GreekStrong, Verse> entry : classifyProcess.uniqueThematicWords.entrySet()) {
							
							synchronized(subprocess11) {
								strong = entry.getKey();
								i = 0;
							}
							synchronized(subprocess11) {
								final Object subprocess111 = new Object();
								for (Float strongNmbr : entry.getValue().strongNumbers) {
									synchronized(subprocess111) {
										if (Math.abs(strong.strongNumber - strongNmbr) < 0.001) {
											final Object subprocess1111 = new Object();
											synchronized(subprocess1111) {            	
												morphValue = entry.getValue().morph.get(i).charAt(0);
											}
											synchronized(subprocess1111) {
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
											}
											synchronized(subprocess1111) {
												tmp = new File("../../View/" + cat + "/HapaxLegomenon.md");
											}
											synchronized(subprocess1111) {
												if (!tmp.exists()) {
													newHLFile("../../View/" + cat + "/HapaxLegomenon.md");
												}
											}
											synchronized(subprocess1111) {
												try {
													final Object subprocess11111 = new Object();
													synchronized(subprocess11111) {
														writerHL = new FileWriter("../../View/" + cat + "/HapaxLegomenon.md", true);
														line = writeInHL(entry.getKey(), entry.getValue());
													}
													synchronized(subprocess11111) {
														writerHL.append(line);
													}
													synchronized(subprocess11111) {
														writerHL.close();
													}
													final Object subprocess11112 = new Object();
													// Pour le "Tout".
													synchronized(subprocess11112) {
														ToutWriterHL = new FileWriter(nameToutFileHL, true);
														line2 = writeInHL(entry.getKey(), entry.getValue());
													}
													synchronized(subprocess11112) {
														ToutWriterHL.append(line2);
													}
													synchronized(subprocess11112) {
														ToutWriterHL.close();
													}
												}
												catch (IOException t) {}
											}
										 // How to do a safe break (one who follow sthe synchronization) to go out from the for loop ?
										}
									}
									synchronized(subprocess111) {
										i++;
									}
								}
							}
						}
					}
				}
			}
			
			catch (FileNotFoundException | UnsupportedEncodingException tt) {}
		}
		
		// on renomme des dossiers, des fichiers.
		synchronized(process) {
			final Object key = new Object();
			String morphs[] = {"1. Verbes", "2. Noms", "4. Adjectifs", "3. Adverbes", "5. Autres (pour débogage)", "6. Tout"};
			
			// Peut-être qu'on peut être plus précis sur quel bloc est à synchroniser.
			for (String morph : morphs) { 
				/* 
					Je synchronise ici, car on
					ne sait jamais : peut-être que la variable path 
					pourrait être supprimée (par la fin de la boucle for)
					avant que f2 ne soit crée n'est-ce pas? 
					Pareil pour la variable morph qui est modifiée à chaque
					tour de boucle.
					
					Toutes ces synchronisations dans ce bloc,
					rendent l'execution du code environ 2s plus long.
				*/
				synchronized(key) {
					if (!morph.equals("6. Tout")) {
						// compter le nombre d'hapax.
						final Object subprocess = new Object();
						// Synchronization to avoid null pointer exception.
						String path = null;
						synchronized(subprocess) {
							path = "../../View/" + morph + "/HapaxLegomenon.md";
						}
						File f1 = null;
						synchronized(subprocess) {
							f1 = new File(path);
						}
						// Est-ce qu'il y a un thread qui renomme les hapax ?
						synchronized(subprocess) {
							if(f1.exists()) {
								// createNameFile()
								// lineNumber ouvre le fichier à l'adresse path.
								final Object subprocess2 = new Object();
								File f2 = null;
								synchronized(subprocess2) {
									// ajouter des synchronisations.
									f2 = new File(createNameHLFile(morph, lineNumber(path) - 2));
								}
								synchronized(subprocess2) {
									f1.renameTo(f2);
								}
							}
						}
					}
				}	
				// Est-ce que si on ne renomme pas les variables ci-dessous ce n'est pas suffisant ?
				synchronized(key) {
					// renommer les dossiers et "ordonner" les fichiers.
					final Object subprocess = new Object();
					String path = null;
					synchronized(subprocess) {
						path = "../../View/" + morph;
					}
					File f1 = null;
					synchronized(subprocess) {
						f1 = new File(path);
					}
					synchronized(subprocess) {
						if(f1.isDirectory()) {
							
							// tout fichier caché n'est pas pris en compte.
							final Object subprocess2 = new Object();
							File root = null;
							synchronized(subprocess2) {
								root = new File(path + "/");
							}
							File[] files = null;
							synchronized(subprocess2) {
								files = root.listFiles(new FileFilter() {
									@Override
									public boolean accept(File file) {
										return !file.isHidden();
									}
								});
							}
							int nbrF = 0;
							synchronized(subprocess2) {
								nbrF = files.length - 1;  // -1 due to the hapax legomenon file.
							}
							// renommage
							File f4 = null;
							synchronized(subprocess2) {
								f4 = new File(path + " (" + nbrF + ")");
								// ordre (notamment pour l'affichage sur Github).
								Arrays.sort(files, Collections.reverseOrder());
							}
							synchronized(subprocess2) {
								final Object subprocess3 = new Object();
								File f7 = null;
								for(int p = 1; p < files.length; p++) {
									synchronized(subprocess3) {
										// ajouter des synchronisations.
										f7 = new File(path + "/" + String.format("%02d", p) + ". " + files[p].getName());
									}
									synchronized(subprocess3) {
										files[p].renameTo(f7);
									}
								}
							}
							synchronized(subprocess2) {
								f1.renameTo(f4);
							}
						}
					}
				}
			}
		
		}
		
	}

}
