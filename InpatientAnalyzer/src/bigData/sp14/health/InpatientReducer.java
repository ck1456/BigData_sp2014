package bigData.sp14.health;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.ArrayListTextWritable;

public class InpatientReducer extends
        Reducer<Text, ArrayListTextWritable, Text, Text>
{

    public static enum INPATIENT_COUNTER {
        BAD_RECORD
    }

    @Override
    public void reduce(Text key, Iterable<ArrayListTextWritable> values,
            Context context)
            throws IOException, InterruptedException {

        Map<Integer, AreaYearStats> areaStats =
                new TreeMap<Integer, AreaYearStats>();

        // Records like: Year,LengthOfStay,Severity,Charges
        for (ArrayListTextWritable t : values) {
            try {
                Integer year = Integer.parseInt(t.get(0).toString());
                if (!areaStats.containsKey(year)) {
                    AreaYearStats newStats = new AreaYearStats();
                    newStats.year = year;
                    areaStats.put(year, newStats);
                }
                AreaYearStats stats = areaStats.get(year);

                stats.totalStayDays += Integer.parseInt(t.get(1).toString());

                int severity = Integer.parseInt(t.get(2).toString());
                stats.updateSeverityCount(severity);

                // parse dollar amounts as doubles
                String charges = t.get(3).toString().replaceAll("\\$", "");
                stats.totalCharges += Double.parseDouble(charges);

                stats.recordCount++;
            } catch (Exception ex) {
                // Badly formatted record
                context.getCounter(INPATIENT_COUNTER.BAD_RECORD).increment(1);
            }
        }

        // for each year, output the stats
        // for (AreaYearStats stats : areaStats.values()) {
        // context.write(key, new Text(stats.toString()));
        // }

        // Output the year over year changes
        context.write(key, new Text(buildSummary(areaStats)));

    }

    /**
     * Builds a year over year summary of the increase/decrease is hospital stay
     * days
     */
    private static String buildSummary(Map<Integer, AreaYearStats> stats) {
        StringBuilder sb = new StringBuilder();

        int lastValue = 0;
        int currentValue = 0;
        int[] years = { 2009, 2010, 2011, 2012 };
        for (int y : years) {
            // sb.append(y).append(",");
            if (stats.containsKey(y)) {
                AreaYearStats a = stats.get(y);
                currentValue = stats.get(y).totalStayDays;
                sb.append(a).append(",");
            } else {
                AreaYearStats a = new AreaYearStats(); // Empty stats
                a.year = y;
                sb.append(a).append(",");
            }
            double deltaPercent = 0.0;
            if (lastValue != 0) {
                deltaPercent = (double) (currentValue - lastValue) / lastValue;
            }

            sb.append(String.format("%.2f", deltaPercent)).append(",");
            lastValue = currentValue;
        }

        return sb.toString();
    }

    /**
     * Keeps track of important metrics for a given area and year
     */
    private static class AreaYearStats {
        public int year;
        public int totalStayDays;
        public int recordCount;
        public int severity1Count;
        public int severity2Count;
        public int severity3Count;
        public int severity4Count;
        public int severityOtherCount;
        public double totalCharges;

        public void updateSeverityCount(int severity) {
            switch (severity) {
            case 1:
                severity1Count++;
                return;
            case 2:
                severity2Count++;
                return;
            case 3:
                severity3Count++;
                return;
            case 4:
                severity4Count++;
                return;
            default:
                severityOtherCount++;
                return;

            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            // Year
            sb.append(year);
            sb.append(",");
            // Total stay days
            sb.append(totalStayDays);
            sb.append(",");
            // Average length of stay
            if (recordCount > 0) {
                sb.append(((double) totalStayDays) / recordCount);
            } else {
                sb.append(0.0);
            }
            sb.append(",");
            // Severity 1
            sb.append(severity1Count);
            sb.append(",");
            // Severity 2
            sb.append(severity2Count);
            sb.append(",");
            // Severity 3
            sb.append(severity3Count);
            sb.append(",");
            // Severity 4
            sb.append(severity4Count);
            sb.append(",");
            // Severity other
            sb.append(severityOtherCount);
            sb.append(",");
            // Average severity
            if (recordCount > 0) {
                sb.append(((double) (severity1Count + (severity2Count * 2)
                        + (severity3Count * 3) + (severity4Count * 4)))
                        / recordCount);
            } else {
                sb.append(0.0);
            }
            sb.append(",");
            // Total charges
            sb.append(totalCharges);
            sb.append(",");
            // Average charges
            if (recordCount > 0) {
                sb.append((totalCharges) / recordCount);
            } else {
                sb.append(0.0);
            }
            return sb.toString();
        }
    }

}
