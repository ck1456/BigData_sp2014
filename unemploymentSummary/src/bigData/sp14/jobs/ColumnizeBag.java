package bigData.sp14.jobs;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class ColumnizeBag extends EvalFunc<Tuple>
{

    public Tuple exec(Tuple input) throws IOException {

        try {

            DataBag bag = (DataBag) (input.get(0));

            // The bag should have 4 tuples, each with 4 fields
            Tuple t = TupleFactory.getInstance().newTuple(16);

            int year = 0;
            for (Tuple yearTuple : bag) {
                for (int f = 0; f < 4; f++) {
                    t.set((year * 4) + f,
                            yearTuple.get(f + 2).toString()
                                    .replaceAll("[, \"]", ""));
                }
                year++;
            }

            return t;
        } catch (Exception e) {
            throw new IOException("Caught exception processing record", e);
        }
    }
    
    public Schema outputSchema(Schema input){
        Schema tupleSchema = new Schema();
        tupleSchema.add(new Schema.FieldSchema("Force_2009", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Employed_2009", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Unemployed_2009", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("UnemployedPercent_2009", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Force_2010", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Employed_2010", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Unemployed_2010", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("UnemployedPercent_2010", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Force_2011", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Employed_2011", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Unemployed_2011", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("UnemployedPercent_2011", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Force_2012", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Employed_2012", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("Unemployed_2012", DataType.DOUBLE));
        tupleSchema.add(new Schema.FieldSchema("UnemployedPercent_2012", DataType.DOUBLE));
        
        return tupleSchema;
    }
}
