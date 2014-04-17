package bigData.sp14.jobs;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultBagFactory;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class UnemploymentFilter extends EvalFunc<DataBag>
{
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0)
            return null;
        try {
            DataBag output = DefaultBagFactory.getInstance().newDefaultBag();
            String str =
                    input.get(0).toString().replaceAll("[{}]", "")
                            .replaceAll("[()]", "");
            String[] strSplit = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            Tuple t = TupleFactory.getInstance().newTuple(6);
            for (String w : strSplit) {
                if (w != null && w.length() > 0) {

                    // keep data if NY
                    if (w.indexOf("NY") > 0) {
                        t.set(0, strSplit[0]);
                        t.set(1, strSplit[1]);
                        t.set(2, strSplit[3]);
                        t.set(3, strSplit[4]);
                        t.set(4, strSplit[5]);
                        t.set(5, strSplit[6]);
                        output.add(t);
                    }
                }
            }
            return output;
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}
