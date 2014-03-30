package bigData.sp14.health;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.ArrayListTextWritable;
import org.apache.hadoop.mapreduce.lib.input.CSVLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.CSVNLineInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class InpatientAnalyzer {

    
    public static void main(String[] args) throws Exception {
        
        if(args.length < 2){
            System.err.println("Usage: InpatientAnalyzer <input path> <output path>");
            System.exit(-1);
        }
        
        Configuration conf = new Configuration();
        conf.set(CSVLineRecordReader.FORMAT_DELIMITER, "\"");
        conf.set(CSVLineRecordReader.FORMAT_SEPARATOR, ",");
        conf.setInt(CSVNLineInputFormat.LINES_PER_MAP, 40000);
        conf.setBoolean(CSVLineRecordReader.IS_ZIPFILE, false);
        
        conf.set("mapred.textoutputformat.separator", ",");
        
        Job job = Job.getInstance(conf, "inpatient_analysis");
        job.setJarByClass(InpatientAnalyzer.class);
        
       
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setMapperClass(InpatientMapper.class);
        job.setReducerClass(InpatientReducer.class);
       
        job.setInputFormatClass(CSVNLineInputFormat.class);
        
        job.setMapOutputValueClass(ArrayListTextWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
    
}
