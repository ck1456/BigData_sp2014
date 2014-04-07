package bigData.sp14.health;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LookupBuilder {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        
        Locator l = new Locator("zip_code_database.csv", "NY");
        
        List<String> countyList = l.getCounties();
        
        // build a lookup from zip3 to county
        Map<String, String> zip3Lookup = new HashMap<String, String>();
        
        int i = 0;
        int zips = 0;
        int conflicts = 0;
        for(String c : countyList){
            System.out.println(String.format("[%2d] %s", ++i, c));
            for(String z : l.getZipcodesByCounty(c)){
                String zip3 = z.substring(0, 3);
                System.out.println(String.format("\t%s --> %s", z, zip3));
                zips++;
                if(zip3Lookup.containsKey(zip3)){
                    // Assert that this matches the county that we would otherwise add
                    if(!zip3Lookup.get(zip3).equals(c)){
                        System.err.println(String.format("Bad zip code mapping: %s [%s/%s]", zip3, zip3Lookup.get(zip3), c));
                        conflicts++;
                    }
                } else {
                    zip3Lookup.put(zip3, c);
                }                
            }
        }
        
        System.out.println("Zip3 Lookup:");
        for(String zip3 : zip3Lookup.keySet()){
            System.out.println(String.format("%s --> %s", zip3, zip3Lookup.get(zip3)));
        }
        
        System.out.println(String.format("Zips: %d", zips));
        System.out.println(String.format("Conflicts: %d", conflicts));
        
    }

}
