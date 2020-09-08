import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.text.DecimalFormat;

// To do: to implement BHP+ utilisation.

/* You can see several comments on the previous version 
of this file, these files are available on Github.
In the master's commits, the ones before the 7th of September.
*/

public class GetPassages {
    
    String locA = "/Users/gustavberloty/Documents/GitHub/";
    String locB = "nto-ranking/Model/Data/";
    String urlBase = locA + locB;
    HashMap<Reference, Verse[]> passages = new HashMap<Reference, Verse[]>();
    // The verse translated is passages.get(X)[0] where X is a key of passages.
    HashMap<Reference, Verse> passagesTranslated;
    
    public GetPassages(String filename, String sourceName) {
    	final Object process1 = new Object();
    	synchronized(process1) {
			getRef(urlBase + filename);
		}
		synchronized(process1) {
			final Object process2 = new Object();
			for (Reference ref : passages.keySet()) {
				synchronized(process1) {
					passages.get(ref)[0] = getVerse(ref, -1, sourceName);
				}
			}
		}
		synchronized(process1) {
			passagesTranslated = new HashMap<Reference, Verse>(new GetMartinTranslation(passages).passagesTranslated);
		}
    }

    void getRef(String givenFilename) {
        
        String csvFile = givenFilename;
        String line = "";
        String cvsSplitBy = ",";
        
        final Object process1 = new Object();
    	synchronized(process1) {
			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	
				String verseDivision = "";
				final Object process2 = new Object();
				
				for (int i = 0; (line = br.readLine()) != null; i++) {
					synchronized(process2) {
						// Comma used as separator.
						String finding = line.split(cvsSplitBy)[0];
						
						if (i == 0) {
							verseDivision = finding.substring(18);
							final Object process3 = new Object();
							synchronized(process3) {
								switch (verseDivision) {
									case "NA28" : case "Darby" :
										verseDivision = "NA28";
										System.out.println("The NA28 verse division is used in your list.");
										break;
									case "CNTR" : case "KJTR" : case "KJV" : case "Martin" : 
										 verseDivision  = "CNTR";
										 System.out.println("The Stephanus 1551 (CNTR) verse division is used in your list.");
										 break;
									default : 
										System.out.println("Error with the verse division provided in the verses references list.");
								}
							}
						}
						else {
							boolean contain = false;
							final Object process3 = new Object();
							synchronized(process3) {
								final Object process4 = new Object();
								for(Reference r : passages.keySet()) {
									synchronized(process4) {
										if (r.textFormat.equals(finding)) {
											contain = true;
										}
									}
								}
							}
							synchronized(process3) {
								if (!contain) {
									passages.put(new Reference(finding, verseDivision), new Verse[2]);
								}
							}
						}
					}
				}
	
			} catch (IOException e) {e.printStackTrace();}
		}
        
        synchronized(process1) {
			boolean NA28andCNTR_references = true;
			final Object process2 = new Object();
    		synchronized(process2) {
    			final Object process3 = new Object();
				for (Reference r : passages.keySet()) {
					synchronized(process3) {
						if (!r.NA28andCNTR_reference) {
							NA28andCNTR_references = false;
						}
					}
				}
			}
			synchronized(process2) {
				if (NA28andCNTR_references) {
					System.out.println("The references you gave through the list give the same passages in a \"NA28\" Bible and in a \"Textus Receptus\" Bible !");
				}
			}
        }
    }
        
    String getGoodDirectory() {
    	File file;
    	String[] directories;
		final Object process5 = new Object();
		synchronized(process5) {
        	file = new File(urlBase + "New Testament/");
        }
        synchronized(process5) {
        	directories = file.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
				}
			});
		}
		synchronized(process5) {
        	return directories[0];
        }
    }
    
    String getData(String dataID) {
        String dataFilename = dataID.substring(0, 5) + ".htm";
        String dataPath;
        final Object process6 = new Object();
        synchronized(process6) {
        	dataPath = urlBase + "New Testament/";
        }
		synchronized(process6) {
        	dataPath += getGoodDirectory() + "/" + dataFilename;
			try {
				return new String(Files.readAllBytes(Paths.get(dataPath)));
			}
			catch (IOException givenException) {
				return null;
			}
		}
    }
    
    String[] getDataIDs(Reference ref, int indice, String sourceName) {
        String verseNumber;
        DecimalFormat df = new DecimalFormat("00");
        final Object process7 = new Object();
        synchronized(process7) {
        	df = new DecimalFormat("00");
        }
        indice = (indice==-1)?ref.verse[0]:indice;
        synchronized(process7) {
        	verseNumber = ref.CNTRformatBeginning + df.format(indice);
        }
        String sourceNumber = "CT" + sourceName;
        String[] dataID = new String[3];
        final Object process8 = new Object();
        synchronized(process8) {
        	dataID[0] = sourceNumber;
        }
        synchronized(process8) {
        	dataID[1] = verseNumber;
        }
        synchronized(process8) {
        	dataID[2] = sourceName;
        }
        synchronized(process8) {
        	return dataID;
        }
    }
    
    Verse getVerse(Reference ref, int indice, String sourceName) {
        
        String text = "";
        ArrayList<Float> strong = new ArrayList<Float>();
        ArrayList<String> morph = new ArrayList<String>();
        String source = sourceName;
        Verse returnValue;
        
        final Object process11 = new Object();
        synchronized(process11) {
			if (ref.textFormat.contains("-") && indice == -1) {
				Verse[] tabVerse;
				final Object process9 = new Object();
				synchronized(process9) {
					tabVerse = new Verse[ref.verse.length];
				}
				for (int i = 0; i < ref.verse.length; i++) {
					synchronized(process9) {
						tabVerse[i] = getVerse(ref, ref.verse[i], sourceName);
					}
				}
				synchronized(process9) {
					returnValue = concatVerse(ref, tabVerse, sourceName);
				}
				synchronized(process9) {
					return returnValue;
				}
			}
			else {
				String[] dataIDs;
				String data;
				final Object process10 = new Object();
				synchronized(process10) {
					dataIDs = getDataIDs(ref, indice, sourceName);
				}
				synchronized(process10) {
					data = getData(dataIDs[1]);
				}
				ArrayList<ArrayList<String>> textWithInfos;
				synchronized(process10) {
					textWithInfos = getCodedText(dataIDs, data);
				}
				synchronized(process10) {
					text = textWithInfos.get(0).get(0);
					strong = getStrong(dataIDs[1], textWithInfos, data);
					morph = getMorph(dataIDs[1], textWithInfos, data);
				}
				synchronized(process10) {
					text = decodedText(text);
					source = dataIDs[2];
				}
			}
        }
        Verse returnValue2;
        synchronized(process11) {
        	returnValue2 = new Verse(ref, text, strong, morph, source);
        }
        synchronized(process11) {
        	return returnValue2;
        }
    }
    
    Verse concatVerse(Reference ref, Verse[] tab, String sourceName) {
        
        String text;
        ArrayList<Float> strong;
        ArrayList<String> morph;
        String source;
        Verse returnValue;
        
        final Object process12 = new Object();
        
        synchronized(process12) {
        	text = "";
        	strong = new ArrayList<Float>();
        	morph = new ArrayList<String>();
            source = sourceName;        
        }
        
        for (int i = 0; i < tab.length; i++) {
            synchronized(process12) {
				text += " " + tab[i].text;
				strong.addAll(tab[i].strongNumbers);
				morph.addAll(tab[i].morph);
			}
        }
        
        synchronized(process12) {
        	returnValue = new Verse(ref, text, strong, morph, source);
        }
        synchronized(process12) {
        	return returnValue;
        }
    }
    
    ArrayList<ArrayList<String>> getCodedText(String[] ids, String verse) {
        
        // 0 for text, 1 for strongs, 2 for morphs.
        ArrayList<ArrayList<String>> data;
        final Object process21 = new Object();
        synchronized(process21) {
			final Object process13 = new Object();
			synchronized(process13) {
				data = new ArrayList<ArrayList<String>>();
			}
			synchronized(process13) {
				data.add(new ArrayList<String>()); // text
			}
			synchronized(process13) {
				data.add(new ArrayList<String>()); // strongs
			}
			synchronized(process13) {
				data.add(new ArrayList<String>()); // morphs
			}
			synchronized(process13) {
				data.add(new ArrayList<String>()); // position of strong or morph variant.
			}
			final Object process14 = new Object();
			Pattern pattern; Matcher matcher;
			synchronized(process14) {
				pattern = Pattern.compile(regexForVerse(ids));
			}
			synchronized(process14) {
				matcher = pattern.matcher(verse);
			}
			synchronized(process14) {
				final Object process15 = new Object();
				while (matcher.find()) {
					synchronized(process15) {
						// text
						synchronized(process13) {
							final Object process16 = new Object();
							String stringReplace, toReplace, toAdd;
							
							synchronized(process16) {
								toReplace = matcher.group(1);
								stringReplace = regexForVariant();
							}
							synchronized(process16) {
								toAdd = toReplace.replaceAll(stringReplace, "$1");
							}
							synchronized(process16) {
								data.get(0).add(toAdd);
							}
						}
						
						Pattern pattern2;
						Matcher matcher2;
						synchronized(process13) {
							pattern2 = Pattern.compile(regexForVariant());
						}
						synchronized(process13) {
							matcher2 = pattern2.matcher(matcher.group(1));
						}
						synchronized(process13) {
							final Object process17 = new Object();
							while (matcher2.find()) {
								synchronized(process17) {
									data.get(1).add(matcher2.group(2)); // strongs
									// System.out.println("Strong: " + data.get(1).get(data.get(1).size()-1));
									data.get(2).add(matcher2.group(3)); // morphs
								}
							}
						}
						
						String regexPart1, regexPart2, finalRegex;
						synchronized(process13) {
							regexPart1 = "(<td>)*(" + regexForVariantGen();
							// )*\X*?((<\/span>)*<\/td>|<\/span>)
							regexPart2 = ")*\\X*?((<\\/span>)*<\\/td>|<\\/span>)";
						}
						synchronized(process13) {
							finalRegex =  regexPart1 + regexPart2;
						}
						
						Pattern pattern3; Matcher matcher3;
						synchronized(process13) {
							pattern3 = Pattern.compile(finalRegex);
						}
						synchronized(process13) {
							matcher3 = pattern3.matcher(matcher.group(1));
						}
						
						synchronized(process13) {
							final Object process18 = new Object();
							for (int position = 0; matcher3.find(); position++) {
								synchronized(process18) {
									Pattern pattern4; Matcher matcher4;
									final Object process19 = new Object();
									synchronized(process19) {
										pattern4 = Pattern.compile(regexForVariant());
									}
									synchronized(process19) {
										matcher4 = pattern4.matcher(matcher3.group(0));
									}
									synchronized(process19) {
										final Object process20 = new Object();
										while (matcher4.find()) {
											synchronized(process20) {
												data.get(3).add(Integer.toString(position));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		synchronized(process21) {
        	return data;
        }
    }
    
    String decodedText(String text) {
    	final Object process22 = new Object();
    	synchronized(process22) {
        	text = text.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        synchronized(process22) {
        	return text.replaceAll("", "");
        }
    }
    
    String regexForVerse(String[] ids) {
    	String aboutVerse[], result;
    	final Object process23 = new Object();
    	synchronized(process23) {
        	aboutVerse = new String[4];
        }
        // <a\s*?href=index\.htm\?
        synchronized(process23) {
			aboutVerse[0] = "<a\\s*?href=index\\.htm\\?";
			// CTST#52005018>ST
			aboutVerse[1] = ids[0] + "#" + ids[1] + ">" + ids[2];
			// <\/a><\/td><td>\X+?<\/td>\X*?
			aboutVerse[2] = "<\\/a><\\/td><td>\\X+?<\\/td>\\X*?";
			// <td>(\X+?)<\/td>\s*?(<tr|<\/tbody>)
			aboutVerse[3] = "<td>(\\X+?)<\\/td>\\s*?(<tr|<\\/tbody>)";
		}
		synchronized(process23) {
			result = concatStrings(aboutVerse);
		}
		synchronized(process23) {
        	return result;
        }
    }
    
    String regexForVariant() {
    	final Object process24 = new Object();
    	String tmp, result;
    	synchronized(process24) {
    		tmp = regexForVariantGen();
    	}
    	synchronized(process24) {
    		tmp += "<\\/span>";
    	}
    	synchronized(process24) {
    		return tmp;
    	}
    }
    
    String regexForVariantGen() {
        String aboutVariant[], result;
        final Object process25 = new Object();
        synchronized(process25) {
    		aboutVariant = new String[6];
    	}
    	synchronized(process25) {
			// <span\s*?class='r0-49 int'>&nbsp;<\/span>
			aboutVariant[0] = "<span\\s*?class='r0-49 int'>&nbsp;<\\/span>";
			// <span\s*?class=hover>(\X+?)<span\s*?class=popup>
			aboutVariant[1] = "<span\\s*?class=hover>(\\X+?)<span\\s*?class=popup>";
			// <span\s*?class=koine\s*?title=\X+?>\X+?<\/span>
			aboutVariant[2] = "<span\\s*?class=koine\\s*?title=\\X+?>\\X+?<\\/span>";
			// <br><a\s*?href=\X+?\s*?target=_blank>(\X+?)<\/a>
			aboutVariant[3] = "<br><a\\s*?href=\\X+?\\s*?target=_blank>(\\X+?)<\\/a>";
			// <br><span\s*?title='\X+?'>(\X+?)<\/span>
			aboutVariant[4] = "<br><span\\s*?title='\\X+?'>(\\X+?)<\\/span>";
			// <br>\X+?<\/span><\/span>
			aboutVariant[5] = "<br>\\X+?<\\/span>";
		}
		synchronized(process25) {
			result = concatStrings(aboutVariant);
		}
		synchronized(process25) {
        	return result;
        }
    }
    
    String concatStrings(String[] givenStrings) {
    	final Object process26 = new Object();
    	String concatened;
    	synchronized(process26) {
        	concatened = "";
        }
        for (int i = 0; i < givenStrings.length; i++) {
        	synchronized(process26) {
            	concatened += givenStrings[i];
            }
        }
        synchronized(process26) {
        	return concatened;
        }
    }
    
    String getCodedVerse(String id, String data) {
        final Object process27 = new Object();
    	String[] regex;
    	synchronized(process27) {
        	regex = new String[3];
        }
        synchronized(process27) {
			regex[0] = "(<a id=" + id + "><\\/a>";
			regex[1] = "<h2>\\X+?<\\/h2>\\s*?<table>";
			regex[2] = "\\X+?<\\/table>)";
		}
        Pattern pattern; Matcher matcher;
        synchronized(process27) {
        	pattern = Pattern.compile(concatStrings(regex));
        }
        synchronized(process27) {
        	matcher = pattern.matcher(data);
        }
        synchronized(process27) {
			while (matcher.find()) {
				return matcher.group(0);
			}
		}
        synchronized(process27) {
        	return null;
        }
    }
    
    ArrayList<Float> getStrong(String id, ArrayList<ArrayList<String>> textWithInfos, String data) {
    	
        ArrayList<Float> strongs;
        ArrayList<Integer> isToRemove;
        String regex, codedVerse, text;
        Pattern pattern; Matcher matcher;
        final Object process28 = new Object();
        
        synchronized(process28) {
        	strongs = new ArrayList<Float>();
			regex = regexForStrongs();
		}
		synchronized(process28) {
        	pattern = Pattern.compile(regex);
        }
        synchronized(process28) {
        	codedVerse = getCodedVerse(id, data);
        }
        synchronized(process28) {
       		matcher = pattern.matcher(codedVerse);
       	}
        synchronized(process28) {
        	text = textWithInfos.get(0).get(0);
        }
        synchronized(process28) {
        	isToRemove = whatSToRemove(text);
        }
        
        for (int i = 0; matcher.find(); i++) {
        	synchronized(process28) {
				if (!isToRemove.contains(i)) {
					if (!textWithInfos.get(3).contains(Integer.toString(i))) {
						final Object process29 = new Object();
						float toAdd;
						synchronized(process29) {
							toAdd = Float.parseFloat(matcher.group(1));
						}
						synchronized(process29) {
							strongs.add(toAdd);
						}
					}
					else {
						final Object process30 = new Object();
						int index; float toAdd2;
						String i2, toAdd1;
						synchronized(process30) {
							i2 = Integer.toString(i);
						}
						synchronized(process30) {
							index = textWithInfos.get(3).indexOf(i2);
						}
						synchronized(process30) {
							toAdd1 = textWithInfos.get(1).get(index);
						}
						synchronized(process30) {
							toAdd2 = Float.parseFloat(toAdd1);
						}
						synchronized(process30) {
							strongs.add(toAdd2);
						}
					}
				}
			}
        }
        
        synchronized(process28) {
        	return strongs;
        }
    }
    
    String regexForStrongs() {
        String aboutStrongs[], result;
        final Object process31 = new Object();

        synchronized(process31) {
        	aboutStrongs = new String[2];
        }
        synchronized(process31) {
        	aboutStrongs[0] = "<td \\X+?<br>\\X*?(\\d*\\.*\\d*)?";
        	aboutStrongs[1] = "(<\\/a>){0,1}<br>";
        }
        synchronized(process31) {
        	result = concatStrings(aboutStrongs);
        }
        synchronized(process31) {
        	return result;
        }
    }
    
    ArrayList<String> getMorph(String id, ArrayList<ArrayList<String>> textWithInfos, String data) {
        
        ArrayList<String> morphs = new ArrayList<String>();
        String regex, codedVerse, text;
        Pattern pattern; Matcher matcher;
        ArrayList<Integer> isToRemove;
        
        final Object process32 = new Object();
        final Object process33 = new Object();
        
        synchronized(process32) {
        	regex = regexForMorphs();
        }
        synchronized(process32) {
       		pattern = Pattern.compile(regex);
       	}
        synchronized(process32) {
        	codedVerse = getCodedVerse(id, data);
        }
        synchronized(process32) {
        	matcher = pattern.matcher(codedVerse);
        }
        
        synchronized(process33) {
        	text = textWithInfos.get(0).get(0);
        }
        synchronized(process33) {
        	isToRemove = whatSToRemove(text);
        }
        
        synchronized(process32) {
        	final Object process34 = new Object();
        	ArrayList<String> container; int index;
        	String iStr, newMorph; boolean condition;
			for (int i = 0; matcher.find(); i++) {
				synchronized(process33) {
					if (!isToRemove.contains(i)) {
						synchronized(process34) {
							iStr = Integer.toString(i);
							container = textWithInfos.get(3);
						}
						synchronized(process34) {
							condition = container.contains(iStr);
							newMorph = matcher.group(3);
							index = container.indexOf(iStr);
						}
						synchronized(process34) {
							if (!condition) {
								morphs.add(newMorph);
							}
							else {
								final Object process35 = new Object();
								synchronized(process34) {
									container = textWithInfos.get(2);
								}
								synchronized(process34) {
									newMorph = container.get(index);
								}
								synchronized(process34) {
									morphs.add(newMorph);
								}
							}
						}
					}
				}
			}
		}
        
        synchronized(process32) {
        synchronized(process33) {
        	return morphs;
        }}
    }
    
    String regexForMorphs() {
        // <span\X+?>(\w*-*\w*-*\w*-*\w*)<\/span>
        String regex[], part1, part2, part3;
        final Object process35 = new Object();
        synchronized(process35) {
        	regex = new String[2];
        }
        synchronized(process35) {
			regex[0] = "<span\\X+?>(\\w*-*\\w*-*";
			regex[1] = "\\w*-*\\w*)<\\/span>";
		}
		synchronized(process35) {
			part1 = regexForStrongs();
			part2 = concatStrings(regex);
		}
		synchronized(process35) {
			part3 = part1 + part2;
		}
		synchronized(process35) {
        	return part3;
        }
    }
    
    ArrayList<Integer> whatSToRemove(String text) {
    
        ArrayList<Integer> element;
        String regexA, regexB, regexC, theSign;
        Pattern pattern; Matcher matcher;
        final Object process36 = new Object();
                
        synchronized(process36) {
			element = new ArrayList<Integer>();
			regexA = "(^\\X*?)*(<\\/td><td>";
			regexB = ")+(\\W*$)?";
			theSign = "</td><td></td><td>";
		}
		synchronized(process36) {
        	regexC = regexA + regexB;
        }
        synchronized(process36) {
      		pattern = Pattern.compile(regexC);
        }
        synchronized(process36) {
        	matcher = pattern.matcher(text);
        }
        
        synchronized(process36) {
        	final Object process37 = new Object();
			for (int i = 1; matcher.find(); i++) {  // C'est bien i = 1 pas i = 0
				synchronized(process37) {
					if (matcher.group(1) != null && matcher.group(1).equals("")) {
						final Object process38 = new Object();
						synchronized(process38) {
							element.add(1);
						}
						synchronized(process38) {
							i++; // à vérifier cette instruction.
						}
					}
					else if (matcher.group(3) != null && matcher.group(3).equals("")) {
						String regex2;
						Pattern pattern2; Matcher matcher2;
						final Object process39 = new Object();
						synchronized(process39) {
							regex2 = "(<\\/td><td>)+?";
							pattern2 = Pattern.compile(regex2);
						}
						synchronized(process39) {
							matcher2 = pattern2.matcher(matcher.group(0));
						}
						synchronized(process39) {
							final Object process40 = new Object();
							int newValue;
							while(matcher2.find()){
								synchronized(process40) {
									i++;
								}
								synchronized(process40) {
									newValue = i;
								}
								synchronized(process40) {
									element.add(newValue);
								}
							}
						}
					}
					else if (matcher.group(0) != null && matcher.group(0).contains((theSign))) {
						String regex2;
						Pattern pattern2; Matcher matcher2;
						final Object process41 = new Object();
						synchronized(process41) {
							regex2 = "(<td><\\/td>)+?";
						}
						synchronized(process41) {
							pattern2 = Pattern.compile(regex2);
						}
						synchronized(process41) {
							matcher2 = pattern2.matcher(matcher.group(0));
						}
						synchronized(process41) {
							final Object process42 = new Object();
							int newValue;
							while(matcher2.find()){
								synchronized(process42) {
									i++;
								}
								synchronized(process42) {
									newValue = i;
								}
								synchronized(process42) {
									element.add(newValue);
								}

							}
						}
					}
				}
			}
		}
		synchronized(process36) {
        	return element;
       	}
    }

}
