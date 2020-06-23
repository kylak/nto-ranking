import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;
import java.lang.InterruptedException;

public class Start {
    
    public static void main(String[] args) {
        
        long startTimer = System.currentTimeMillis();
        System.out.println("\n");
        /*
        enum CriticalTexts {
            WH, NA28, SBL, RP, KJTR, ST//, BHP
        }
        */
        final String greekText = "KJTR";
        
        // Obtaining        
        long start0 = System.currentTimeMillis();
		final GetPassages findings = new GetPassages("List_of_verses.csv", greekText);
		long time0 = System.currentTimeMillis() - start0;
		System.out.println("\nData obtained in approximatively " + (int)((float)(time0/1000.0) / 60) + "min" + (int)((float)(time0/1000.0) % 60.0) + "s.");
        
        // Classifiying
		long start = System.currentTimeMillis();
		final Classify classifiedVerses = new Classify(findings.passages);
		long time = System.currentTimeMillis() - start;
		System.out.println("Data classified in approximatively " + time + "ms.");
        
        
        // Generating
        GenerateFile gf = new GenerateFile(classifiedVerses, greekText);
		long start1 = System.currentTimeMillis();
		try {
			gf.generateFiles(findings.passagesTranslated);
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) {}
		long time1 = System.currentTimeMillis() - start1;
		double seconds1 = time1 / 1000.0;
		System.out.println("Classified data generated in approximatively " + (float)seconds1 + "s.");
		
		long endTimer = System.currentTimeMillis() - startTimer;
		System.out.println("Entire program executed in approximatively " + (int)((float)(endTimer/1000.0) / 60) + "min" + (int)((float)(endTimer/1000.0) % 60.0) + "s.\n");
	}
}
