
import java.util.regex.*;
import java.io.BufferedReader;
import java.text.DecimalFormat;

public class Reference {

    /*
    At the moment, only one book and one chapter per Reference.
     Here is the sign that separate book reference from chapter number : ":". e.g : Mt 7:1
     Here is the sign that separate two verses : ",". e.g : Mt 7:1,3
     Here is the sign for a range of verse : "-". e.g : Mt 7:1-3
    */
    
    int numberOfTheBook; // e.g "1 Tim 5:3" gives "1".
    String bookName;
    int chapter;
    int[] verse; // List all verses included in the reference.
    String textFormat;
    String CNTRformatBeginning; // Why "Beginning" ? Because it's just for book and chapter. For verse, see verse[].
    boolean NA28andCNTR_reference;
    
    public Reference(String givenReference, String givenVerseDivision) {
        textFormat = givenReference;
        
        // (\d*)\s*(\w+)\s*(\d+)\s*:(\d{1,2})(,|-)?(\d{1,2})?
        String regex = "(\\d*)\\s*(\\w+)\\s*(\\d+)\\s*:\\s*?(\\d{1,2})\\s*(,|-)?(\\d{1,2})?";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(textFormat);
        while (matcher.find()) {
            if(!matcher.group(1).equals("")) {
                numberOfTheBook = Integer.parseInt(matcher.group(1));
            }
            bookName = matcher.group(2);
            if (!isBookGivenRecognized(bookName)) {
                System.out.println("Book name not recognized.");
                return;
            }
            chapter = Integer.parseInt(matcher.group(3));
            if (matcher.groupCount() >= 5 && matcher.group(5) != null) {
                switch(matcher.group(5)) {
                    case "," :
                        if (matcher.group(6) != null) {
                            verse = new int[Integer.parseInt(matcher.group(6))-Integer.parseInt(matcher.group(4))+1];
                            verse[0] = Integer.parseInt(matcher.group(4));
                            for (int i=1; i < verse.length; i++) {
                                verse[i] = Integer.parseInt(matcher.group(4)) + i;
                            }
                        };
                        break;
                    case "-" :
                        if (matcher.group(6) != null) {
                            verse = new int[Integer.parseInt(matcher.group(6))-Integer.parseInt(matcher.group(4))+1];
                            verse[0] = Integer.parseInt(matcher.group(4));
                            for (int i=1; i < verse.length; i++) {
                                verse[i] = Integer.parseInt(matcher.group(4)) + i;
                            }
                        };
                        break;
                    default :
                        verse = new int[1];
                        verse[0] = Integer.parseInt(matcher.group(4));
                }
            }
            else {
                verse = new int[1];
                verse[0] = Integer.parseInt(matcher.group(4));
            }
        }
        toCNTRformatBeginning();
        toCNTRverseDivision(givenVerseDivision);
    }
    
    private boolean isBookGivenRecognized(String givenBookName) {
        for (BookName book : BookName.values()) {
            if (book.name().equals(givenBookName)) {
                return true;
            }
        }
        return false;
    }
    
    enum BookName {
        Mt, Mat, Matthew, Matthieu,
        Mk, Mc, Mark, Marc,
        Lk, Lc, Luke, Luc,
        Jn, Jhn, John, Jean,
        Ac, Acts, Actes,
        Rm, Rom, Romans, Romains,
        Co, Cor, Corinthians, Corinthiens,
        Ga, Gal, Galatians, Galates,
        Ep, Eph, Ephesians, Ephesiens,
        Ph, Philippians, Philippiens,
        Col, Colossians, Colossiens,
        Th, Thes, Thess, Thessalonicians, Thessaloniciens,
        Ti, Tm, Tim, Timothy, Timothee,
        Tt, Titus, Tite,
        Phm, Philemon,
        He, Hé, Hb, Heb, Hebrews, Hebreux,
        Jm, Jc, James, Jacques,
        P, Pi , Peter, Pierre,
        Jd, Jude,
        Rv, Ap, Apoc, Revelation, Apocalypse,
    }
    
    void toCNTRformatBeginning() {
        String ref = convertRefTo(RefFormat.CNTR);
        CNTRformatBeginning = "" + ref;
        DecimalFormat df = new DecimalFormat("00");
        CNTRformatBeginning += "0" + df.format(chapter) + "0";
    }
    
    String convertRefTo(String[] givenBookName) {
        switch(bookName) {
            case "Mt" : case "Mat" : case "Matthew" : case "Matthieu" :
                return givenBookName[0];
            case "Mk": case "Mc": case "Mark": case "Marc":
                return givenBookName[1];
            case "Lk": case "Lc": case "Luke": case "Luc":
                return givenBookName[2];
            case "Jn": case "Jhn": case "John": case "Jean":
                switch (numberOfTheBook) {
                    case 1 :
                        return givenBookName[3];
                    case 2:
                        return givenBookName[4];
                    case 3:
                        return givenBookName[5];
                    default : // For the gospel according to John.
                        return givenBookName[6];
                }
            case "Ac": case "Acts": case "Actes":
                return givenBookName[7];
            case "Rm": case "Rom": case "Romans": case "Romains":
                return givenBookName[8];
            case "Co": case "Cor": case "Corinthians": case "Corinthiens":
                switch (numberOfTheBook) {
                    case 1 :
                        return givenBookName[9];
                    case 2:
                        return givenBookName[10];
                    default :
                        System.out.println("Error with a Corinthians epistle reference.");
                        return "Corinthians";
                }
            case "Ga": case "Gal": case "Galatians": case "Galates":
                return givenBookName[11];
            case "Ep": case "Eph": case "Ephesians": case "Ephesiens":
                return givenBookName[12];
            case "Ph": case "Philippians": case "Philippiens":
                return givenBookName[13];
            case "Col": case "Colossians": case "Colossiens":
                return givenBookName[14];
            case "Th": case "Thes": case "Thess": case "Thessalonicians": case "Thessaloniciens":
                switch (numberOfTheBook) {
                    case 1 :
                        return givenBookName[15];
                    case 2:
                        return givenBookName[16];
                    default :
                        System.out.println("Error with a Thessalonicians epistle reference.");
                        return "Thessalonicians";
                }
            case "Tm": case "Ti": case "Tim": case "Timothy": case "Timothee":
                switch (numberOfTheBook) {
                    case 1 :
                        return givenBookName[17];
                    case 2:
                        return givenBookName[18];
                    default :
                        System.out.println("Error with a Timothy epistle reference.");
                        return "Timothy";
                }
            case "Tt": case "Titus": case "Tite":
                return givenBookName[19];
            case "Phm": case "Philemon":
                return givenBookName[20];
            case "He": case "Hé": case "Hb": case "Heb": case "Hebrews": case "Hebreux":
                return givenBookName[21];
            case "Jm": case "Jc": case "James": case "Jacques":
                return givenBookName[22];
            case "P": case "Pi": case "Peter": case "Pierre":
                switch (numberOfTheBook) {
                    case 1 :
                        return givenBookName[23];
                    case 2:
                        return givenBookName[24];
                    default :
                        System.out.println("Error with a Peter epistle reference.");
                        return "Peter";
                }
            case "Jd": case "Jude":
                return givenBookName[25];
            case "Rv": case "Ap": case "Apoc": case "Revelation": case "Apocalypse":
                return givenBookName[26];
        }
        return null;
    }
    
    void toCNTRverseDivision(String givenVerseDivision) {
    
       	String verseNumber;
    	switch (givenVerseDivision) {
    	
    		case "NA28" : 
    			int checkingPoint = 0;
    			for (int i = 0; i < verse.length; i++) {
    				DecimalFormat df = new DecimalFormat("00");
        			verseNumber = CNTRformatBeginning + df.format(verse[i]);
    				switch (verseNumber) {
    					// Here, we write the differences between the NA28 verse division and the CNTR verse division.
    					case "50001016" :
    						verse[i] = 17;
    						textFormat = "Ph 1:17";
    						System.out.println("You declared a NA28 verse division and specify in the list the Ph 1:16 reference. This reference in the NA28 verse division corresponds to the Ph 1:17 reference in the CNTR verse division.");
    						break;
    						
    					case "50001017" :
    						verse[i] = 16;
    						textFormat = "Ph 1:16";
    						System.out.println("You declared a NA28 verse division and specify in the list the Ph 1:17 reference. This reference in the NA28 verse division corresponds to the Ph 1:16 reference in the CNTR verse division.");
    						break;
    						
    					case "49005013" : case "49005014" : case "46007033" : case "46007034" : case "44002047" : case "42022045" : /*case "66006001" : case "42022045" : case "40012049" : ? */
    						System.out.println("Caution: the reference " + verseNumber + " included in the list, is to be checked according to the NA28 and CNTR verses division.");
    						break;	
    						
    					default : checkingPoint++;
    				}
    			}
    			if (checkingPoint == verse.length) {
    				NA28andCNTR_reference = true;
    			}
    			else { NA28andCNTR_reference = false; }
    			break;
    			
    		case "CNTR" : break;
    	}
    }
}
