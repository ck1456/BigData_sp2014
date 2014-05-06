Health and Unemployent Analysis

Realtime and Big Data Analytics
Spring 2014 - Final Project
Professor Suzanne McIntosh

May 7, 2014

Submitted by:
Chris Keitel (ck1456@nyu.edu)
George Dagher (gd793@nyu.edu)
Khen Price (chenprice.music@gmail.com)

Overview:
The included code contains a variety of technologies used to filter, summarize, analyze and visualize several freely available datasets during our investigation of health and unemployment in New York State.

Our code is structured as a series of independent tools each of which is used as processing stage in a pipeline for undertanding the available data. Each stage is discussed below:

Stage 1 [Hadoop MapReduce]:

This stage takes detailed hospital inpatient data and summarizes it by county.  This code was tested and run on Cloudera's QuickStart VM i pseudodistributed mode (1 node) but should run on any Hadoop cluster supporting v2 with minor modifications.  This code makes use of an open source CSV FileInputFormat library included in source code form and taken entirely from:
https://github.com/mvallebr/CSVInputFormat

All of the code is included as an Eclipse project with an associated Ant build file that can help facilitate the process of testing:
> cd InpatientAnalyzer
> ant -projecthelp

The Ant task expects input data to be located in the "~/project/" directory and writes output to "~/output/health".

Start by loading the SPARC CSV data files into hdfs:
> hadoop fs -mkdir project
> hadoop fs -put SPARC_2009.csv project/
> hadoop fs -put SPARC_2010.csv project/
> hadoop fs -put SPARC_2011.csv project/
> hadoop fs -put SPARC_2012.csv project/

Run the default Ant target  to compile the InpatientAnalyzer into an appropriate jar:
> ant

Then run the default MapReduce analysis by using the hadoop ant target:
> ant hadoop

Alternately, you can run hadoop directly with the following command:
> hadoop jar InpatientAnalyzer.jar bigDatasp14.health.InpatientAnalyzer project output/health

This process takes about 1 hour on a decently provisioned Windows laptop running the QuickStart VM in VMWare Player 6.

After the map reduce is complete, you can review and retrieve the results like this:
> hadoop fs -cat output/health/part*

For the next stage, you are going to want to retrieve the summary and store it locally:
> hadoop fs -get output/health/part-r-00000 results/health_summary.csv

Stage 2 [Pig]:

This stage uses Pig to filter and consolidate unemployment data.  It consists of a pig script and a few custom UDFs included in an Eclipse project with associated Ant script for useful automation:
> cd unemploymentSummary
> ant -projecthelp

The default Ant target will build the UDFs:
> ant

Make sure the unemployment data is located in "input"
> cp laucnty09-12.tsv input/

Because this dataset is not particularly big we can run pig in local mode and save on the startup time of going all the way through HDFS and MapReduce, but still get the advantages of PigLatin.  The getUnemploymentStats.pig script depends on UnemploymentUDF.jar and expects input in "./input/launcty09-12.tsv".  The script writes output to "./output/"

Run the jobs task to filter national unemployment data down to just New York:
> ant jobs

Alternately, you can run pig directly like this:
> pig -x local getUnemploymentStats.pig

Copy the resulting output to the summary file that will be the input to the next step:
> cp output/part-r-00000 input/jobs_summary.csv

Now you can combine the unemployment data with the health data by running:
> ant combine

This depends on "input/health_summary.csv" from the previous stage and "input/jobs_summary.csv" from the previous step.  In addition, it depends on "fieldNames.csv" which is healpful for analysis in later stages to make sure  we know what attributes are actually showing a useful correlation.

This combine process generates output in "output-combined".  You should copy this data file for further analysis:
> cp output-combined/part-r-00000 results/combined-results.csv


Stage 3 [Matlab/Python]:

This is one of several processes we tried to analyze the data.  Matlab code is located in the Correlate directory.  Within Matlab, you can execute:
>> correlate4year

This operates on the combined-results.csv file from the previous stage. This does some data cleanup (remapping names for certain counties) and formats the output so that it can be visualized in the next step.

This produces several map-coloring files which display interesting correlations:
stay_unemp_map.txt
sev_unemp_map.txt
charges_unemp_map.txt
charges_unemp_percent_map.txt

You can also run a python script used for additional sorting and analysis:
> python sort_sev_enemp.py

Stage 4 [d3 Visualization]:

In the last stage we generated some map visualizations by running the HTML and JavaScript located in the "HTML Map" directory:
> cd "HTML Map"
> python -m SimpleHTTPServer 8008 &

Then navigate to http://localhost:8008 in a browser.  This web page will allow you to browse to a map file which will display the counties colored appropriately according to the selected data file.  You can choose any of the map data files produced in the previous stage:

stay_unemp_map.txt
sev_unemp_map.txt
charges_unemp_map.txt
charges_unemp_percent_map.txt


Additional Code:
In our exploration of this data we rpoduced several other small programs, tools and snippets which were ultimatley not incorporated into our analysis and final findgins, but wwe used along the way:
Other/gscripts
Other/Locator
Other/Mapgenerator
Other/ZipLookup


