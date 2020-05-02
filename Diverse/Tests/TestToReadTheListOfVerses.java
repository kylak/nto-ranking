
public class TestToReadTheListOfVerses {
    
    public static void main(String[] args) {
        GetPassages testFindings = new GetPassages("/Users/gustavberloty/Documents/GitHub/greekNTO-classificationProgram/Model/Data/List_of_verses.csv", "ST");
        for ( Reference ref : testFindings.passages.keySet()) {
            System.out.println(ref.textFormat + " : " + testFindings.passages.get(ref)[0].text);
        }
    }
}
