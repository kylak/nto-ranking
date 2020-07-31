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
import java.io.IOException;

public class Start {
    
    public static void main(String[] args) {
    
    	final Object process1 = new Object();
    	long startTimer;
    	String greekText;
    	SyntacticRoles[] roleToKeep;
    	GetPassages findings;
    	Classify classifiedVerses;
    	
        synchronized(process1) {
			startTimer = System.currentTimeMillis();
			System.out.println("\n");
			/*
			enum CriticalTexts {
				WH, NA28, SBL, RP, KJTR, ST//, BHP
			}
			*/
			greekText = "KJTR";
		}
		
		synchronized(process1) {
			roleToKeep = new SyntacticRoles[]
					{
						new SyntacticRoles(SyntacticRoles.sign.N), // Nouns
						new SyntacticRoles(SyntacticRoles.sign.A), // Adjectives
						new SyntacticRoles(SyntacticRoles.sign.V), // Verbs
						new SyntacticRoles(SyntacticRoles.sign.D) // Adverbs
					};
		}
        
        // Obtaining
        synchronized(process1) {
        	long start0, time0;
        	final Object process11 = new Object();
            synchronized(process11) {
				start0 = System.currentTimeMillis();
				findings = new GetPassages("List_of_verses.csv", greekText);
			}
			synchronized(process11) {
				time0 = System.currentTimeMillis() - start0;
			}
			synchronized(process11) {
				System.out.println("\nData obtained in approximatively " + (int)((float)(time0/1000.0) / 60) + "min" + (int)((float)(time0/1000.0) % 60.0) + "s.");
			}
		}
		
        // Classifiying
        synchronized(process1) {
			long start, time;
			final Object process11 = new Object();
			synchronized(process11) {
				start = System.currentTimeMillis();
				classifiedVerses = new Classify(findings.passages, roleToKeep);
			}
			synchronized(process11) {
				time = System.currentTimeMillis() - start;
			}
			synchronized(process11) {
				System.out.println("Data classified in approximatively " + time + "ms.");
			}
		}

        // Generating
        synchronized(process1) {
        
			long start1, time1, endTimer;
			double seconds1;
			GenerateFile gf;
			final Object process11 = new Object();
			
			synchronized(process11) {
				gf = new GenerateFile(classifiedVerses, greekText, roleToKeep);
				start1 = System.currentTimeMillis();
			}
			synchronized(process11) {
				try {
					gf.generateFiles(findings.passagesTranslated);
				} catch (IOException t) {}
				time1 = System.currentTimeMillis() - start1;
			}
			synchronized(process11) {
				seconds1 = time1 / 1000.0;
			}
			synchronized(process11) {
				System.out.println("Classified data generated in approximatively " + (float)seconds1 + "s.");
			}
			
			synchronized(process11) {
				endTimer = System.currentTimeMillis() - startTimer;
			}
			synchronized(process11) {
				System.out.println("Entire program executed in approximatively " + (int)((float)(endTimer/1000.0) / 60) + "min" + (int)((float)(endTimer/1000.0) % 60.0) + "s.\n");
			}
		}
	}
}
