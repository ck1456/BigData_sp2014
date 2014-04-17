package bigData.sp14.jobs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class ConformCounty extends EvalFunc<String> {

    public ConformCounty() {

        countySpellingMap.put("Allegany", "Allegheny");
        countySpellingMap.put("New York", "Manhattan");
        countySpellingMap.put("St. Lawrence", "St Lawrence");
    }

    // Maps certain counties to variant spellings to make sure they merge
    // with the other data set
    private Map<String, String> countySpellingMap =
            new HashMap<String, String>();

    public String exec(Tuple input) throws IOException {

        try {
            // Expect a county followed by a bag (that is sorted by year)
            String county = input.get(0).toString();
            county = county.replaceAll("(County|,|NY|\")", "").trim();
            // Do the county mapping lookup
            if (countySpellingMap.containsKey(county)) {
                county = countySpellingMap.get(county);
            }
            return county;
        } catch (Exception e) {
            throw new IOException("Cannot parse county", e);
        }

    }
}
