package bigData.sp14.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapMaker {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        BufferedReader br = new BufferedReader( new FileReader("combined-results.csv"));
        
        Map<String, Double> health = new HashMap<String, Double>();
        Map<String, Double> jobs = new HashMap<String, Double>();
        
        String line = br.readLine();
        double maxHealthSeverity = Double.MIN_VALUE;
        double minHealthSeverity = Double.MAX_VALUE;
        double maxJobSeverity = Double.MIN_VALUE;
        double minJobSeverity = Double.MAX_VALUE;
        while(line != null){
            //Get the 0, 24
            String fields[] = line.split(",");
            
            

            if(!fields[0].equals("County")){
                double severity = Double.parseDouble(fields[18]);
                double unemployed = Double.parseDouble(fields[4]);
                
                health.put(fields[0].toLowerCase().replace(" ", "_"), severity);
                maxHealthSeverity = Math.max(maxHealthSeverity, severity);
                minHealthSeverity = Math.min(minHealthSeverity, severity);
                
                jobs.put(fields[0].toLowerCase().replace(" ", "_"), unemployed);
                maxJobSeverity = Math.max(maxJobSeverity, unemployed);
                minJobSeverity = Math.min(minJobSeverity, unemployed);
            }
            line = br.readLine();
        }

        br.close();
        
        BufferedWriter bw = new BufferedWriter( new FileWriter("map.txt"));

        for(String c : health.keySet()){
            
            bw.write(c);
            bw.write(", ");
            double sev = normalize(health.get(c), minHealthSeverity, maxHealthSeverity);
            double job = normalize(jobs.get(c), minJobSeverity, maxJobSeverity);

            bw.write(getColor(sev, job));
            bw.write("\n");
        }
        
        bw.close();
    }
    
    private static double normalize(double val, double min, double max){
        return (val - min) / (max - min);
    }
    
    private static String getColor(double val, double val2){
    
        int hexVal1 = (int)(val * 255.0);
        int hexVal2 = (int)(val2 * 255.0);
        
        return String.format("#%02X0000", hexVal1, hexVal2);
    }
}
