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
    
    public Classify (HashMap<Reference, Verse[]> givenPassages, SyntacticRoles[] role) {
        final Object process1 = new Object();
    	synchronized(process1) {
    		passages = givenPassages;
        	filter = new Filter(role);
        }
        synchronized(process1) {
        	applyFilter();
        }
        synchronized(process1) {
        	classify();
        }
    }   
    
    void applyFilter() {
        for (Reference i : passages.keySet()) {
			final Object process1 = new Object();
			Verse[] versesSet = new Verse[2];
			String str = "";
			synchronized(process1) {
            	versesSet[0] = passages.get(i)[0];  // On considère que le texte des versets à déjà été ajouté.
            }
            synchronized(process1) {
            	final Object process2 = new Object();
				for (Float s : versesSet[0].strongNumbers) {
					synchronized(process2) {
						str += Float.toString(s) + " ";
					}
				}
			}
			/*
			synchronized(process1) {
				String mrph = "";
				for (String s : versesSet[0].morph) {
					mrph += s + " ";
				}
			}
            System.out.println(versesSet[0].ref.textFormat + " - " + versesSet[0].text + "\nstrong: " + str + "\nmorph: " + mrph + "\n"); */
            synchronized(process1) {
            	versesSet[1] = filter.applyOn(versesSet[0]);
            }
            // System.out.println(i.textFormat + " - versesSet[1]: " + versesSet[1].text);
            synchronized(process1) {
            	passages.replace(i, versesSet);
            }
        }
    }
    
    void classify() {
        HashMap<Reference, Verse[]> passagesForLoop;
        final Object process3 = new Object();
        Verse tmp0;
        int i_for;
        
        synchronized(process3) {
        	passagesForLoop = new HashMap<Reference, Verse[]>(passages);
        }
        
        synchronized(process3) {
        	final Object process4 = new Object();
        	final Object process5 = new Object();
        	final Object process6 = new Object();
        	final Object process7 = new Object();
        	final Object process8 = new Object();
        	final Object process9 = new Object();
        	final Object process10 = new Object();
        	final Object process11 = new Object();
        	final Object process12 = new Object();
        	final Object process13 = new Object();
        	final Object process14 = new Object();
			for (Reference r : passagesForLoop.keySet()) {
				
				// System.out.println("passagesForLoop.get(r)[1]: " + passagesForLoop.get(r)[1].text);
				
				synchronized(process4) {
					// We loop on the filtered verse's words.
					
					synchronized(process14) {
						for (int i = 0; i < passagesForLoop.get(r)[1].strongNumbers.size(); i++) {
						
							synchronized(process5) {
								Float StrongToSearch;
								boolean enterNowAlgo; // An algorithmic variable to speed up the algorithm.
								int nbreOccurenceTotal, p, tmp6;
								// System.out.println("StrongToSearch: " + StrongToSearch);
								ArrayList<Verse> versesHavingTheStrong;
								Verse tmp, tmp2, tmp5;
								Float tmp3, tmp4;
								String tmp7, tmp8;
								boolean tmp9, tmp10;
								
								synchronized(process6) {
									nbreOccurenceTotal = 0;
									p = 0;
									enterNowAlgo = false;
									versesHavingTheStrong = new ArrayList<Verse>();
									tmp = passagesForLoop.get(r)[1];
								}
								
								synchronized(process6) {
									StrongToSearch = tmp.strongNumbers.get(i);
								}
								/* We go through the verses, searching for all
								 passagesForLoop.get(r)[1].strongNumbers.get(i) Strong number occurences. */
								synchronized(process6) {
									for (Reference seeThisRef : passagesForLoop.keySet()) {
										// System.out.println("nouvelle référence de cheché: " + seeThisRef.textFormat);
										/* We begin to go through the verses from the one at
										 the indice 'r' (from passagesForLoop.get(r)[1]) */
										synchronized(process7) {
											tmp7 = seeThisRef.textFormat;
											tmp8 = r.textFormat;
										}
										synchronized(process7) {
											tmp9 = (tmp7).equals(tmp8);
										}
										synchronized(process7) {
											if (enterNowAlgo || tmp9) { // this condition implied that we don't look back to the reference we previously checked.
												synchronized(process8) {
													// We go through every verse's word.
													for (int j = 0; j < passagesForLoop.get(seeThisRef)[1].strongNumbers.size() ; j++, p++) {
														// System.out.println("Pour (" + StrongToSearch + ") - de " + seeThisRef.textFormat + ": " + passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j) + " - " + toString(Math.abs(StrongToSearch - passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j)) < 0.001));
														// We check if the verse's word we are going through is that Strong number.
														synchronized(process9) {
														
															synchronized(process11) {
																tmp2 = passagesForLoop.get(seeThisRef)[1];
															}
															synchronized(process11) {
																tmp3 = tmp2.strongNumbers.get(j);
															}
															synchronized(process11) {
																tmp4 = Math.abs(StrongToSearch - tmp3);
															}
															synchronized(process11) {
																if (tmp3 != null && tmp4 < 0.001 ) {
																	// We add this verse to the list of verse containing that Strong number.
																	// So versesHavingTheWord should at least contain passagesForLoop.get(r)[0].
																	/* System.out.println("strong: " + passagesForLoop.get(seeThisRef)[1].strongNumbers.get(j));
																	System.out.println("les strongs: " + passagesForLoop.get(seeThisRef)[1].strongNumbers);
																	System.out.println(seeThisRef.textFormat + " - passagesForLoop.get(seeThisRef)[1].text: " + passagesForLoop.get(seeThisRef)[1].text); */
																	synchronized(process10) {
																		synchronized(process12) {
																			tmp5 = passagesForLoop.get(seeThisRef)[0];
																		}
																		synchronized(process12) {
																			tmp6 = versesHavingTheStrong.indexOf(tmp5);
																		}
																		synchronized(process12) {
																			if(tmp6 == -1) {
																				versesHavingTheStrong.add(tmp5);
																			}
																		}
																	}
																	
																	synchronized(process10) {
																		/* We delete this Strong number occurence from that verse.
																		 This instruction normally speeds up the classify() algorithm.
																		 This instruction works with the enterNowAlgo variable. */
																		passagesForLoop.get(seeThisRef)[1].strongNumbers.remove(j); 
																	}
																	synchronized(process10) {
																		j--;
																	}
																	synchronized(process10) {
																		if (seeThisRef == r && i > 0) {i--;}
																	}
																	synchronized(process10) {
																		nbreOccurenceTotal++;
																	}
																}
															}
														}
													}
												}
												synchronized(process8) {
													// System.out.println("passagesForLoop.get(seeThisRef)[1].text: " + passagesForLoop.get(seeThisRef)[1].text);
													// System.out.println("nombre de mot checké: " + p);
													enterNowAlgo = true;
												}
											}
										}
									}
								}
								synchronized(process6) {
									if (nbreOccurenceTotal == 1) { // Si on ne veut pas compter le nombre de fois que le strong est dans le verset de base : versesHavingTheStrong.size() == 1.	
										GreekStrong gS;
										Verse v;
										synchronized(process13) {
											gS = new GreekStrong(StrongToSearch);
											v = versesHavingTheStrong.get(0);
										}
										synchronized(process13) {
											uniqueThematicWords.put(gS, v);
										}
									}
									else {
										GreekStrong gS;
										ArrayList<Verse> v;
										synchronized(process13) {
											gS = new GreekStrong(StrongToSearch);
											v = new ArrayList<Verse>(versesHavingTheStrong);
										}
										interestingClassifiedVerses.put(gS, v);
									}
								}
							}
						}
					}
				}
			}
		}
		
		synchronized(process3) {
			final Object process20 = new Object();
			for (HashMap.Entry<GreekStrong, ArrayList<Verse>> entry : interestingClassifiedVerses.entrySet()) {
				synchronized(process20) {
					GreekStrong strong = entry.getKey();
					// System.out.println("strong.strongNumbers: " + strong.strongNumber);
				}
			}
		}
    }
    
    public String toString(boolean value) {
        return value ? "true" : "false";
    }
           
 }
