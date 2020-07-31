import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;		// HashMap<Reference, Verse> passagesTranslated
import java.util.Arrays;		// Arrays.fill(isFirst, Boolean.TRUE);
import java.util.ArrayList;		// ArrayList<Verse> verses
import java.text.Normalizer;	// Normalizer.normalize(tmp, Normalizer.Form.NFD)
import java.util.Collections;	// Arrays.sort(files, Collections.reverseOrder());
import java.io.BufferedReader;	// reader = new BufferedReader(new FileReader(path));
import java.io.FileFilter;		// files = root.listFiles(new FileFilter()
import java.io.FileReader;		// reader = new BufferedReader(new FileReader(path));
import java.util.Scanner;		// scanner = new Scanner(file);
import java.io.FileWriter;		// writer = new FileWriter(nameFile);
import java.io.FilenameFilter;

public class GenerateFile {
  
    Classify classifyProcess;
    String greekText;
    SyntacticRoles[] roleToKeep;
	
    public GenerateFile (Classify givenClassifyProcess, String givenGreekText, SyntacticRoles[] roles) {
        classifyProcess = givenClassifyProcess;
        greekText = givenGreekText;
        roleToKeep = roles;
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
				File fl = new File(nameFile);
				fl.getParentFile().mkdirs();
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
    	
			Object process1 = new Object();
			FileWriter writer;
			String tmp, header = "";
			
			synchronized(process1) {
				File fl = new File(fileName);
				fl.getParentFile().mkdirs();
				writer = new FileWriter(fileName);
			}
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
			return lines;
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
    void generateFiles(HashMap<Reference, Verse> passagesTranslated) throws FileNotFoundException, IOException{
    	
        Object process = new Object();
		synchronized(process) {
		
			try
			{
				final Object process1 = new Object();
				
				synchronized(process1) {
	
					GreekStrong strong;
					int allOcc = 0;
					String nameToutFile = "";
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
								nameToutFile = createNameFile((roleToKeep.length + 1) + ". Tout", strong, allOcc);
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
							boolean first = false; String cat = "", translation = "", nameFile = "", line = ""; char morphValue; int occ = 0;
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
															morphValue = temp.morph.get(i).charAt(0);
														//System.out.println("-----\nstrong: " + strongNmbr + "\ni: " + i + "\nunicode: " + strong.unicode);
													}
													synchronized(subprocess112) {
														final Object subprocess1121 = new Object();
														synchronized(subprocess1121) {
															boolean isFound = false;
															final Object subprocess11211 = new Object();
															synchronized(subprocess11211) {
																for(int t = 0; t < roleToKeep.length; t++) {
																	if(morphValue == roleToKeep[t].abbr.name().charAt(0)) {
																		if(isFirst[t]) {
																			first = true;
																			isFirst[t] = false;
																		}
																		cat = (t + 1) + ". " + roleToKeep[t].name + "s";
																		isFound = true;
																		break;
																	}
																}
															}
															synchronized(subprocess11211) {
																if(!isFound) {
																	i++;
																	continue;
																}
															}
														}
														synchronized(subprocess1121) {
															/* Partie je trouve à alléger
															 (faisable, il me semble, en utilisant PrintWriter à la place de FileWriter). */
															occ = nbrOccurence(entry.getValue(), strong.strongNumber, morphValue); // strong nécéssite subprocess 
														}
														synchronized(subprocess1121) {
															//System.out.println("Morph:" + morphValue + " - occ: " + occ + "cat: " + cat);
															nameFile = createNameFile(cat, strong, occ); // strong nécéssite subprocess 1
														}
														synchronized(subprocess1121) {
															// System.out.println("i: " + i.get() + " -> " + nameFile);
															if (/*first == false && */verseAlreadyAdded(nameFile, temp.ref.textFormat)) {
																i++;
																continue;
															}
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
				
					String nameToutFileHL, cat = "", line, line2 = "";
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
								nameToutFileHL = createNameHLFile((roleToKeep.length + 1) + ". Tout", allOcc);
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
												boolean isFound = false;
												final Object subprocess11111 = new Object();
												synchronized(subprocess11111) {
													for(int t = 0; t < roleToKeep.length; t++) {
														if(morphValue == roleToKeep[t].abbr.name().charAt(0)) {
															cat = (t + 1) + ". " + roleToKeep[t].name + "s";
															isFound = true;
															break;
														}
													}
												}
												synchronized(subprocess11111) {
													if(!isFound) {
														i++;
														continue;
													}
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
			
			catch (FileNotFoundException tt) {}		//  il y avait aussi avant UnsupportedEncodingException
		}
		
		// on renomme des dossiers, des fichiers.
		synchronized(process) {
			final Object key = new Object();
			final Object key2 = new Object();
			String morphs[] = new String[roleToKeep.length + 1];
			
			synchronized(key2) {
				for (int i = 0 ; i < morphs.length; i++) {
					if (i == roleToKeep.length) 
						morphs[i] = (i + 1) + ". Tout";
					else
						morphs[i] = (i + 1) + ". " + roleToKeep[i].name + "s";
				}
			}
			
			synchronized(key2) {
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
						if (!morph.equals((roleToKeep.length + 1) + ". Tout")) {
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
								int digitNumber;
								synchronized(subprocess2) {
									nbrF = files.length - 1;  // -1 due to the hapax legomenon file.
								}
								// renommage
								File f4 = null;
								synchronized(subprocess2) {
									f4 = new File(path + " (" + nbrF + ")");
									// ordre (notamment pour l'affichage sur Github).
									Arrays.sort(files, Collections.reverseOrder());
									digitNumber = String.valueOf(nbrF).length();
								}
								synchronized(subprocess2) {
									final Object subprocess3 = new Object();
									File f7 = null;
									for(int p = 1; p < files.length; p++) {
										synchronized(subprocess3) {
											// ajouter des synchronisations.
											f7 = new File(path + "/" + String.format("%0" + digitNumber + "d", p) + ". " + files[p].getName());
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

}
