/*
 * author: Khen Price
 */

-- This script uses labor unemployment statistics data, and outputs this data by year and county,given a state.
-- Register the JAR files
REGISTER ./myudf.jar;

-- Load data from tsv file
A = LOAD './input/laucnty09-12.tsv' USING PigStorage('\t') AS (LAUS_Code, State_FIPS_Code, County_FIPS_Code, County_State,Year,EMPTY,Force,Employed,Unemployed, UnemploymentPercentage);

-- Discard unused fields
B = FOREACH A GENERATE County_State,Year,EMPTY,Force,Employed,Unemployed, UnemploymentPercentage;

-- group by year and county
C = GROUP B by (Year, County_State);

-- Keep only the tuples that are relevant for New York state
RAWFILTERED = FOREACH C GENERATE FLATTEN(myudf.UnemploymentFilter(B)) AS  (County_State:chararray,Year:int,Force,Employed,Unemployed, UnemploymentPercentage);

-- Sort by year and county
SORTED = ORDER RAWFILTERED BY Year, County_State;
dump SORTED;

-- This is to check if we have data for all counties (should be 248 = 62 counties * 4 years)
ROW_COUNT = FOREACH (GROUP RAWFILTERED ALL) GENERATE COUNT(RAWFILTERED);

-- Store in output file
STORE SORTED INTO './unemployment.tsv' USING PigStorage('\t');



