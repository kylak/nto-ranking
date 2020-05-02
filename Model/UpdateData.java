
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateData {
    
    /*
     mkdir `date +%Y-%m-%d` &&
     for i in {1..9}; do curl https://greekcntr.org/collation/data/4000${i}.htm > `date +%Y-%m-%d`/4000${i}.htm; done;
     for i in {10..28}; do curl https://greekcntr.org/collation/data/400${i}.htm > `date +%Y-%m-%d`/400${i}.htm; done;
     */
    
    public UpdateData() {
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        String[] command = new String[11];
        for (int i = 0; i < command.length; i++) {
            command[i] = "";
        }
        String url = "/Users/gustavberloty/Documents/GitHub/greekNTO-classificationProgram/Model/Data/New\\ Testament/";
        command[0] += "\ncd " + url + " && mkdir `date +%Y-%m-%d` && ";
        
        for (int book = 40; book <= 66; book++) {
            switch (book) {
                case 40 : case 44 :
                    command[0] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\nfor i in {10..28}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
                case 41 : case 45 : case 46 :
                    command[1] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\nfor i in {10..16}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
                case 42 :
                    command[2] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm;done;\nfor i in {10..24}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
                case 43 :
                    command[3] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\nfor i in {10..21}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
                case 47 : case 58 :
                    command[4] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\nfor i in {10..13}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
                case 48 : case 49 : case 54 :
                    command[5] += "for i in {1..6}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\n";
                break;
                case 50 : case 51 : case 55 :
                    command[6] += "for i in {1..4}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\n";
                break;
                case 52 : case 59 : case 60 : case 62 :
                    command[7] += "for i in {1..5}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\n";
                break;
                case 53 : case 56 : case 61 :
                    command[8] += "for i in {1..3}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\n";
                break;
                case 57 : case 63 : case 64 : case 65 :
                    command[9] += "curl https://greekcntr.org/collation/data/" + book + "001.htm >" + url + "`date +%Y-%m-%d`/" + book + "001.htm;\n";
                break;
                case 66 :
                    command[10] += "for i in {1..9}; do curl https://greekcntr.org/collation/data/" + book + "00${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "00${i}.htm; done;\nfor i in {10..22}; do curl https://greekcntr.org/collation/data/" + book + "0${i}.htm >" + url + "`date +%Y-%m-%d`/" + book + "0${i}.htm; done;\n";
                break;
            }
        }

        
        for(int i=0; i < 11; i++) {
            System.out.println(command[i]);
            /*processBuilder.command("script.sh");
            try {

                Process process = processBuilder.start();

                StringBuilder output = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }

                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    System.out.println("Success!");
                    System.out.println(output);
                    //System.exit(0);
                } else {
                    //abnormal...
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
    
    public static void main(String[] args) {
    	new UpdateData();
    }
}
    
