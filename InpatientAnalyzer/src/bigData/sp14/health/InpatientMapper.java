package bigData.sp14.health;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.ArrayListTextWritable;

public class InpatientMapper extends
        Mapper<LongWritable, List<Text>, Text, ArrayListTextWritable> {

    public static enum INPATIENT_COUNTER {
        BAD_RECORD,
        NO_COUNTY
    }

    @Override
    public void map(LongWritable key, List<Text> value, Context context)
            throws IOException, InterruptedException {

        // Parse out the fields that we care about:
        // County - 1
        // Zip3 - 6
        // Length of Stay - 10
        // Year - 14
        // Severity - 24
        // Charges - 37

        try {
            Text county = value.get(1);
            if(county.toString().trim().isEmpty()){
                context.getCounter(INPATIENT_COUNTER.NO_COUNTY).increment(1);
                return; // no area location to count
            }
            ArrayListTextWritable mappedValues = new ArrayListTextWritable();
            mappedValues.add(value.get(14));
            mappedValues.add(value.get(10));
            mappedValues.add(value.get(24));
            mappedValues.add(value.get(37));

            // Key: county
            // Value: Year,LengthOfStay,Severity,Charges
            context.write(county, mappedValues);

        } catch (Exception ex) {
            // Badly formatted record
            // Keep track so that we can be sure that it is a small percentage
            // of the total records that get ignored
            context.getCounter(INPATIENT_COUNTER.BAD_RECORD).increment(1);
        }

    }

}
