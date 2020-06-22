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

public class GetPassages {
    
    String locA = "/Users/gustavberloty/Documents/GitHub/";
    String locB = "nto-ranking/Model/Data/";
    String urlBase = locA + locB;
    HashMap<Reference, Verse[]> passages = new HashMap<Reference, Verse[]>();
    // The verse translated is passages.get(X)[0] where X is a key of passages.
    HashMap<Reference, Verse> passagesTranslated;
    
    public GetPassages(String filename, String sourceName) {
					getRef(urlBase + filename);
					// System.out.println("entered");
					for (Reference ref : passages.keySet()) {
						passages.get(ref)[0] = getVerse(ref, -1, sourceName);
					}
					passagesTranslated = new HashMap<Reference, Verse>(new GetMartinTranslation(passages).passagesTranslated);
    }

    void getRef(String givenFilename) {
        
        String csvFile = givenFilename;
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			String verseDivision = "";
			
            for (int i = 0; (line = br.readLine()) != null; i++) {

                // Comma used as separator.
                String finding = line.split(cvsSplitBy)[0];
                
                // Get the verses division specified in the list.
                // It should be given as "verses_division_:_X,"
                // where X is the verse division (CNTR, NA28, …).
                if (i == 0) {
                	verseDivision = finding.substring(18);
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
               	else {
					boolean contain = false;
					for(Reference r : passages.keySet()) {
						if (r.textFormat.equals(finding)) {
							contain = true;
						}
					}
					if (!contain) {
						passages.put(new Reference(finding, verseDivision), new Verse[2]);
					}
				}
            }

        } catch (IOException e) {e.printStackTrace();}
        
        boolean NA28andCNTR_references = true;
        for (Reference r : passages.keySet()) {
        	if (!r.NA28andCNTR_reference) {
        		NA28andCNTR_references = false;
        	}
        }
        if (NA28andCNTR_references) {
        	System.out.println("The references you gave through the list give the same passages in a \"NA28\" Bible and in a \"Textus Receptus\" Bible !");
        }
    }
        
    String getGoodDirectory() {
        File file = new File(urlBase + "New Testament/");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
            }
        });
        return directories[0];
    }
    
    String getData(String dataID) {
        String dataFilename = dataID.substring(0, 5) + ".htm";
        String dataPath = urlBase + "New Testament/";
        dataPath += getGoodDirectory() + "/" + dataFilename;
        try {
            return new String(Files.readAllBytes(Paths.get(dataPath)));
        }
        catch (IOException givenException) {
            String error = "An error occured when we tried to read the file (";
            error += dataPath + ").";
            // System.out.println(error);
            return null;
        }
    }
    
    String[] getDataIDs(Reference ref, int indice, String sourceName) {
        String verseNumber;
        DecimalFormat df = new DecimalFormat("00");
        indice = (indice==-1)?ref.verse[0]:indice;
        verseNumber = ref.CNTRformatBeginning + df.format(indice);
        
        String sourceNumber = "CT" + sourceName;
        String[] dataID = new String[3];
        dataID[0] = sourceNumber;
        dataID[1] = verseNumber;
        dataID[2] = sourceName;
        return dataID;
    }
    
    Verse getVerse(Reference ref, int indice, String sourceName) {
        
        String text = "";
        ArrayList<Float> strong = new ArrayList<Float>();
        ArrayList<String> morph = new ArrayList<String>();
        String source = sourceName;
        
        if (ref.textFormat.contains("-") && indice == -1) {
            Verse[] tabVerse = new Verse[ref.verse.length];
            for (int i = 0; i < ref.verse.length; i++) {
                tabVerse[i] = getVerse(ref, ref.verse[i], sourceName);
            }
            return concatVerse(ref, tabVerse, sourceName);
        }
        else {
            String[] dataIDs = getDataIDs(ref, indice, sourceName);
            String data = getData(dataIDs[1]);
            /* for(String d : dataIDs) {
                System.out.println(d);
            } */
            ArrayList<ArrayList<String>> textWithInfos = getCodedText(dataIDs, data);
            text = textWithInfos.get(0).get(0);
            strong = getStrong(dataIDs[1], textWithInfos, data);
            morph = getMorph(dataIDs[1], textWithInfos, data);
            text = decodedText(text);
            source = dataIDs[2];
        }
        
        return new Verse(ref, text, strong, morph, source);
    }
    
    Verse concatVerse(Reference ref, Verse[] tab, String sourceName) {
        
        String text = "";
        ArrayList<Float> strong = new ArrayList<Float>();
        ArrayList<String> morph = new ArrayList<String>();
        String source = sourceName;
        
        for (int i = 0; i < tab.length; i++) {
            text += " " + tab[i].text;
            strong.addAll(tab[i].strongNumbers);
            morph.addAll(tab[i].morph);
        }
        
        return new Verse(ref, text, strong, morph, source);
    }
    
    ArrayList<ArrayList<String>> getCodedText(String[] ids, String verse) {
        
        // 0 for text, 1 for strongs, 2 for morphs.
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        data.add(new ArrayList<String>()); // text
        data.add(new ArrayList<String>()); // strongs
        data.add(new ArrayList<String>()); // morphs
        data.add(new ArrayList<String>()); // position of strong or morph variant.
        
        Pattern pattern = Pattern.compile(regexForVerse(ids));
        Matcher matcher = pattern.matcher(verse);
        while (matcher.find()) {
            // text
            data.get(0).add(matcher.group(1).replaceAll(regexForVariant(), "$1"));
            
            Pattern pattern2 = Pattern.compile(regexForVariant());
            Matcher matcher2 = pattern2.matcher(matcher.group(1));
            while (matcher2.find()) {
                data.get(1).add(matcher2.group(2)); // strongs
                // System.out.println("Strong: " + data.get(1).get(data.get(1).size()-1));
                data.get(2).add(matcher2.group(3)); // morphs
            }
            
            String regexPart1 = "(<td>)*(" + regexForVariantGen();
            // )*\X*?((<\/span>)*<\/td>|<\/span>)
            String regexPart2 = ")*\\X*?((<\\/span>)*<\\/td>|<\\/span>)";
            String finalRegex =  regexPart1 + regexPart2;
            
            Pattern pattern3 = Pattern.compile(finalRegex);
            Matcher matcher3 = pattern3.matcher(matcher.group(1));
            
            for (int position = 0; matcher3.find(); position++) {
                
                Pattern pattern4 = Pattern.compile(regexForVariant());
                Matcher matcher4 = pattern4.matcher(matcher3.group(0));
                // System.out.println(regexForVariant());
                // System.out.println(matcher3.group(0) + "\n");
                while (matcher4.find()) {
                    //System.out.println("entered: " + matcher3.group(0));
                    //System.out.println("text verse: " + matcher.group(1).replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
                    data.get(3).add(Integer.toString(position)); // position
                    // System.out.println("position: " + position);
                    // System.out.println("Strong: " + data.get(1).get(data.get(3).indexOf(data.get(3).get(data.get(3).size()-1))));
                }
            }
        }
        return data;
    }
    
    String decodedText(String text) {
        text = text.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        return text.replaceAll("", "");
    }
    
    String regexForVerse(String[] ids) {
        String aboutVerse[] = new String[4];
        // <a\s*?href=index\.htm\?
        aboutVerse[0] = "<a\\s*?href=index\\.htm\\?";
        // CTST#52005018>ST
        aboutVerse[1] = ids[0] + "#" + ids[1] + ">" + ids[2];
        // <\/a><\/td><td>\X+?<\/td>\X*?
        aboutVerse[2] = "<\\/a><\\/td><td>\\X+?<\\/td>\\X*?";
        // <td>(\X+?)<\/td>\s*?(<tr|<\/tbody>)
        aboutVerse[3] = "<td>(\\X+?)<\\/td>\\s*?(<tr|<\\/tbody>)";
        return concatStrings(aboutVerse);
    }
    
    String regexForVariant() {
        return regexForVariantGen() + "<\\/span>";
    }
    
    String regexForVariantGen() {
        String[] aboutVariant = new String[6];
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
        return concatStrings(aboutVariant);
    }
    
    String concatStrings(String[] givenStrings) {
        String concatened = "";
        for (int i = 0; i < givenStrings.length; i++) {
            concatened += givenStrings[i];
        }
        return concatened;
    }
    
    String getCodedVerse(String id, String data) {
        
        // (<a id=59005015><\/a><h2>\X+?<\/h2>\s*?<table>\X+?<\/table>)
        String[] regex = new String[3];
        regex[0] = "(<a id=" + id + "><\\/a>";
        regex[1] = "<h2>\\X+?<\\/h2>\\s*?<table>";
        regex[2] = "\\X+?<\\/table>)";
        
        Pattern pattern = Pattern.compile(concatStrings(regex));
        Matcher matcher = pattern.matcher(data);
        
        while (matcher.find()) {
            return matcher.group(0);
        }
        
        return null;
    }
    
    ArrayList<Float> getStrong(String id, ArrayList<ArrayList<String>> textWithInfos, String data) {
        // System.out.println("GET STRONG");
        ArrayList<Float> strongs = new ArrayList<Float>();
        
        String regex = regexForStrongs();
        Pattern pattern = Pattern.compile(regex);
        
        String codedVerse = getCodedVerse(id, data);
        Matcher matcher = pattern.matcher(codedVerse);
        
        String text = textWithInfos.get(0).get(0);
        ArrayList<Integer> isToRemove = whatSToRemove(text);
        
        for (int i = 0; matcher.find(); i++) {
            if (!isToRemove.contains(i)) {
                if (!textWithInfos.get(3).contains(Integer.toString(i))) {
                    strongs.add(Float.parseFloat(matcher.group(1)));
                }
                else {
                    int index = textWithInfos.get(3).indexOf(Integer.toString(i));
                    strongs.add(Float.parseFloat(textWithInfos.get(1).get(index)));
                    // System.out.println("text: " + textWithInfos.get(0).get(0).replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
                    // System.out.println("Strong que l'on prend: " + textWithInfos.get(1).get(index) + " - au lieu de : " + matcher.group(1));
                }
            }
        }
        /*
        String str = "";
        for (Float s : strongs) {
            str += Float.toString(s) + " ";
        }
        System.out.println("text: " + text + "\nstrong: " + str + "\n\n"); */
        // System.out.println("givenVerse.strongNumbers.size(): " + strongs.size());
        return strongs;
    }
    
    String regexForStrongs() {
        String[] aboutStrongs = new String[2];
        // <td \X+?https:\/\/biblehub\.com\/greek
        // <td \X+?<br>\X*?(\d*\.*\d*)?(<\/a>){0,1}<br>
        aboutStrongs[0] = "<td \\X+?<br>\\X*?(\\d*\\.*\\d*)?";
        aboutStrongs[1] = "(<\\/a>){0,1}<br>";
        return concatStrings(aboutStrongs);
    }
    
    ArrayList<String> getMorph(String id, ArrayList<ArrayList<String>> textWithInfos, String data) {
        
        ArrayList<String> morphs = new ArrayList<String>();
        
        String regex = regexForMorphs();
        Pattern pattern = Pattern.compile(regex);
        
        String codedVerse = getCodedVerse(id, data);
        Matcher matcher = pattern.matcher(codedVerse);
        
        String text = textWithInfos.get(0).get(0);
        ArrayList<Integer> isToRemove = whatSToRemove(text);
        
        for (int i = 0; matcher.find(); i++) {
            if (!isToRemove.contains(i)) {
                if (!textWithInfos.get(3).contains(Integer.toString(i))) {
                    morphs.add(matcher.group(3));
                }
                else {
                    int index = textWithInfos.get(3).indexOf(Integer.toString(i));
                    morphs.add(textWithInfos.get(2).get(index));
                }
            }
        }
        
        /* String tmpe = decodedText(text);
        String[] tmp = tmpe.split(" ");
        if (tmp.length != morphs.size()) {
            System.out.println("text: " + text);
        } */
        /*String str = "";
        for (Float s : strongs) {
            str += Float.toString(s) + " ";
        }
        System.out.println("text: " + text + "\nstrong: " + str + "\n\n"); */
        return morphs;
    }
    
    String regexForMorphs() {
        // <span\X+?>(\w*-*\w*-*\w*-*\w*)<\/span>
        String[] regex = new String[2];
        regex[0] = "<span\\X+?>(\\w*-*\\w*-*";
        regex[1] = "\\w*-*\\w*)<\\/span>";
        return (regexForStrongs() + concatStrings(regex));
    }
    
    ArrayList<Integer> whatSToRemove(String text) {
        ArrayList<Integer> element = new ArrayList<Integer>();
        
        // (^\X*?)*(<\/td><td>)+(\W*$)?
        String regexA = "(^\\X*?)*(<\\/td><td>";
        String regexB = ")+(\\W*$)?";
        String regexC = regexA + regexB;
        
        Pattern pattern = Pattern.compile(regexC);
        Matcher matcher = pattern.matcher(text);
        
        String theSign = "</td><td></td><td>";
        
        for (int i = 1; matcher.find(); i++) {  // C'est bien i = 1 pas i = 0
            if (matcher.group(1) != null && matcher.group(1).equals("")) {
                element.add(1);
                i++; // à vérifier cette instruction.
            }
            else if (matcher.group(3) != null && matcher.group(3).equals("")) {
                String regex2 = "(<\\/td><td>)+?";
                Pattern pattern2 = Pattern.compile(regex2);
                Matcher matcher2 = pattern2.matcher(matcher.group(0));
                while(matcher2.find()){
                    element.add((i++));
                }
            }
            else if (matcher.group(0) != null && matcher.group(0).contains((theSign))) {
                String regex2 = "(<td><\\/td>)+?";
                Pattern pattern2 = Pattern.compile(regex2);
                Matcher matcher2 = pattern2.matcher(matcher.group(0));
                while(matcher2.find()){
                    element.add((i++));
                }
            }
        }
        return element;
    }
    
/*
    Verse getVerse(Reference ref, String source) { // Concernant les sources, remplacer le String
        
        // Quel fichier ?
        
        // <a\s+?id=40005001><\/a>\s*?<h2>\X+?<\/h2>\s*?<table>\s*?<tr\s+?class=rule>\X*?<td>(\X{1,2})<\/td>\s*?<\/tr>
        String numberOfWord = "<a\\s+?id=" + verseNumber + "><\\/a>\\s*?<h2>\\X+?<\\/h2>\\s*?<table>\\s*?<tr\\s+?class=rule>\\X*?<td>(\\X{1,2})<\\/td>\\s*?<\\/tr>";
        int numberOfWord; // matcher.group(x);
        for(int i = 0; i < ; i++) {
            text += "un_mot";
        }
        
        // 1. avoir le numéro du verset
        // <a\s*?href=index\.htm\?GA20001#40005001>GA 01<\/a><\/td><td>\X+?<\/td>\X*?<td>(\X+?)<\/td>\s*?(<tr|<\/tbody>)
        String regex = "<a\\s*?href=index\\.htm\\?" + sourceNumber + "#" + verseNumber + ">" + sourceName + "<\\/a><\\/td><td>\\X+?<\\/td>\\X*?<td>(\\X+?)<\\/td>\\s*?<tr(<tr|<\\/tbody>)"
        // remplacer dans la chaîne retournée chaque </td><td> par un espace.
        
        // On gère les variations (variantes, corrections, suppressions, etc…)
        // On récupère un tableau de <td> si dans le tableau on a un
        // Pour gérer les simples variantes, en un <td> : <span class=hover>(\X+?)<
        
        final ArrayList<Integer> strongNumbers;  // The strong number for each verse's word.
        final ArrayList<String> morph;           // The morphology code for each verse's word.
        //
        return null;
        // return new Verse(ref, text, strongNumbers, morph, source);
     }

    /*
     
     Pour utiliser les manuscrits :
     
     // Pour gérer les corrections : <span class=corr>(\X+?)<
     // Pour gérer les suppressions : <span class=edit>(\X+?)<
     // Quand une variation est mélangé avec une correction ou suppression, cette variation est enfant de la correction ou suppresion.
     // Nous n'avons pas à nous occuper des ajouts ou des suppressions.
     // Pour prendre les "metadatas" (les lettres bleues claires) (à faire après avoir supprimer ce que l'on ne veut pas prendre) : String strippedText = htmlText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
     // Pour ne pas prendre les "metadatas" :
     // Pour prendre les lettres en noires (à faire après avoir supprimer ce que l'on ne veut pas prendre) : String strippedText = htmlText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
     // Pour ne pas prendre les lettres en noires :
     // <td><span class=dam>υ</span>μεισ</td>
     // gérer les nominasacras et les *.
     
     */

}
