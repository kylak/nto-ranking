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
        ExecutorService threadpool0 = Executors.newCachedThreadPool();
        long start0 = System.currentTimeMillis();
		Future<GetPassages> futureTask0 = threadpool0.submit(() -> new GetPassages("List_of_verses.csv", greekText));
		while (!futureTask0.isDone()) {/* wait task1 to be completed */}
		long time0 = System.currentTimeMillis() - start0;
		System.out.println("\nData obtained in approximatively " + (int)((float)(time0/1000.0) / 60) + "min" + (int)((float)(time0/1000.0) % 60.0) + "s.");
		GetPassages fds = null;
		try {
			fds = futureTask0.get(); 
		}
		catch (InterruptedException | ExecutionException e) {}
		final GetPassages findings = fds;
		threadpool0.shutdown();
        
        
        // Classifiying
        ExecutorService threadpool = Executors.newCachedThreadPool();
		long start = System.currentTimeMillis();
		Future<Classify> futureTask = threadpool.submit(() -> new Classify(findings.passages));
		while (!futureTask0.isDone()) {/* wait task1 to be completed */}
		long time = System.currentTimeMillis() - start;
		System.out.println("Data classified in approximatively " + time + "ms.");
		Classify cVerses = null;
		try {
			cVerses = futureTask.get(); 
		}
		catch (InterruptedException | ExecutionException e) {}
		final Classify classifiedVerses = cVerses;
		threadpool.shutdown();
        
        
        // Generating
        ExecutorService threadpool2 = Executors.newCachedThreadPool();
		Future<GenerateFile> futureTask2 = threadpool2.submit(() -> new GenerateFile(classifiedVerses, greekText));
		long start1 = System.currentTimeMillis();
		while (!futureTask0.isDone()) {/* wait task1 to be completed */}
		GenerateFile gf = null;
		try {
			gf = futureTask2.get();
			gf.generateFiles(findings.passagesTranslated);
		}
		catch (InterruptedException | ExecutionException | FileNotFoundException | UnsupportedEncodingException e) {}
		long time1 = System.currentTimeMillis() - start1;
		double seconds1 = time1 / 1000.0;
		System.out.println("Classified data generated in approximatively " + (float)seconds1 + "s.");
		threadpool2.shutdown();
		
		long endTimer = System.currentTimeMillis() - startTimer;
		System.out.println("Entire program executed in approximatively " + (int)((float)(endTimer/1000.0) / 60) + "min" + (int)((float)(endTimer/1000.0) % 60.0) + "s.\n");
	}
}
