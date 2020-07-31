import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;

public class ManageViewFiles {

	static String urlBase = "/Users/gustavberloty/Documents/GitHub/nto-ranking/";

	public static void main(String[] args) {
		try {
			final Object process1 = new Object();
			File myObj, file;
			FileWriter myWriter;
			String url, aCommand;
			String[] directories;
			
			synchronized(process1) {
				myObj = new File("../../Controller/.managingFolders");
			}
			synchronized(process1) {
				myObj.createNewFile();
			}
			synchronized(process1) {
				myWriter = new FileWriter("../../Controller/.managingFolders");
				url = urlBase + "View/";
			}
			synchronized(process1) {
				file = new File(url);
			}
			
			synchronized(process1) {	  
				directories = file.list(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});
			}
			
			synchronized(process1) {
				if (directories.length > 0) {
					final Object process11 = new Object();
					synchronized(process11) {
						aCommand = "cd " + url;
					}
					synchronized(process11) {
						myWriter.write("\n" + aCommand);
					}
					synchronized(process11) {
						aCommand = "\nzip -r \"../Model/Data/Previous View/" + "`date +%d:%m:%Y` à";
					}
					synchronized(process11) {
						aCommand += " `date +%H`h`date +%M`min`date +%Ss` (date de la compression).zip";
					}
					synchronized(process11) {
						aCommand +=  "\" " + "*/ List_of_verses.csv Readme.md" + "\n";
					}
					synchronized(process11) {
						myWriter.write("\ncp " + urlBase + "Model/Data/List_of_verses.csv " + urlBase + "View/List_of_verses.csv");
					}
					synchronized(process11) {
						myWriter.write(aCommand);
					}
					synchronized(process11) {
						myWriter.write("rm -r */ List_of_verses.csv");
					}
				}
			}
			synchronized(process1) {
				myWriter.close();
			}
		} catch (IOException e) {
			  System.out.println("An error occurred when we tried to write the file.");
			  e.printStackTrace();
		}
    }

	
}
