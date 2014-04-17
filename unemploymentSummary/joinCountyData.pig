/*
 * author: Chris Keitel
 */

-- This script takes health data and jobs data in a well-known format and
-- joins them so they can be analyzed

-- Load both CSV files
JOBS = LOAD './input/jobs_summary.csv' USING PigStorage(',')
	AS (County,
	Force_2009, Employed_2009, Unemployed_2009, UnemployedPercent_2009,
	Force_2010, Employed_2010, Unemployed_2010, UnemployedPercent_2010,
	Force_2011, Employed_2011, Unemployed_2011, UnemployedPercent_2011,
	Force_2012, Employed_2012, Unemployed_2012, UnemployedPercent_2012);

HEALTH = LOAD './input/health_summary.csv' USING PigStorage(',')
	AS (County,
	Year_2009, StayDays_2009, AvgStay_2009,
	Sev1_2009, Sev2_2009, Sev3_2009, Sev4_2009, Sev0_2009, AvgSev_2009,
	Charges_2009, AvgCharges_2009,
	Year_2010, StayDays_2010, AvgStay_2010,
	Sev1_2010, Sev2_2010, Sev3_2010, Sev4_2010, Sev0_2010, AvgSev_2010,
	Charges_2010, AvgCharges_2010,
	Year_2011, StayDays_2011, AvgStay_2011,
	Sev1_2011, Sev2_2011, Sev3_2011, Sev4_2011, Sev0_2011, AvgSev_2011,
	Charges_2011, AvgCharges_2011,
	Year_2012, StayDays_2012, AvgStay_2012,
	Sev1_2012, Sev2_2012, Sev3_2012, Sev4_2012, Sev0_2012, AvgSev_2012,
	Charges_2012, AvgCharges_2012);

-- Join on the County name which we have already conformed
-- This will drop the counties that we don't have complete data for
COMBINED = JOIN JOBS BY County, HEALTH BY County;

SLICED = FOREACH COMBINED GENERATE JOBS::County AS County,
	Force_2009, Employed_2009, Unemployed_2009, UnemployedPercent_2009,
	Force_2010, Employed_2010, Unemployed_2010, UnemployedPercent_2010,
	Force_2011, Employed_2011, Unemployed_2011, UnemployedPercent_2011,
	Force_2012, Employed_2012, Unemployed_2012, UnemployedPercent_2012,
	StayDays_2009, AvgStay_2009,
	Sev1_2009, Sev2_2009, Sev3_2009, Sev4_2009, Sev0_2009, AvgSev_2009,
	Charges_2009, AvgCharges_2009,
	Year_2010, StayDays_2010, AvgStay_2010,
	Sev1_2010, Sev2_2010, Sev3_2010, Sev4_2010, Sev0_2010, AvgSev_2010,
	Charges_2010, AvgCharges_2010,
	Year_2011, StayDays_2011, AvgStay_2011,
	Sev1_2011, Sev2_2011, Sev3_2011, Sev4_2011, Sev0_2011, AvgSev_2011,
	Charges_2011, AvgCharges_2011,
	Year_2012, StayDays_2012, AvgStay_2012,
	Sev1_2012, Sev2_2012, Sev3_2012, Sev4_2012, Sev0_2012, AvgSev_2012,
	Charges_2012, AvgCharges_2012;

SORTED = ORDER SLICED BY County;

-- Helpful to see the fields we ended up with 
describe SORTED;

STORE SORTED INTO './output-combined' USING PigStorage(',');

