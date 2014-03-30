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
        BAD_RECORD
    }

    @Override
    public void map(LongWritable key, List<Text> value, Context context)
            throws IOException, InterruptedException {

        // Parse out the fields that we care about:
        // Zip3 - 6
        // Length of Stay - 10
        // Year - 14
        // Severity - 24
        // Charges - 37

        try {
            Text zip3 = value.get(6);
            
            ArrayListTextWritable mappedValues = new ArrayListTextWritable();
            mappedValues.add(value.get(14));
            mappedValues.add(value.get(10));
            mappedValues.add(value.get(24));
            mappedValues.add(value.get(37));

            // Key: 3 code zip
            // Value: Year,LengthOfStay,Severity,Charges
            context.write(zip3, mappedValues);

        } catch (Exception ex) {
            // Badly formatted record
            context.getCounter(INPATIENT_COUNTER.BAD_RECORD).increment(1);
            throw new RuntimeException(ex);
        }

    }

}
