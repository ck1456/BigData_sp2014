#!/usr/bin/env python

"""
Reads in severity to unemployment correlation and writes output in sorter order

This script is used with certain designated files that have been generated
in preceeding parts of the research as output from Matlab:

sev_unemp_map_blue.txt
sev_unemp_map_green.txt

Outputs counties and severity correlations in sorted order.

"""

import csv
from operator import itemgetter

__author__ = "Chen Price"
__copyright__ = "Copyright 2014"
__credits__ = ["Chris Keitel", "George Dagher"]
__email__ = "cp1425@nyu.edu"

# global
DEBUG = 1
  
def getCounties(infile):
	"""
	Reads from csv file (format: 'county', 'hex rgb') into list
	Gets green counties, sorts by 'green' intensity and returns the sorted list

	Keyword arguments:
	infile -- the csv file to read from
	"""
	counties = []
	with open(infile, 'rb') as csvfile:
		data = csv.reader(csvfile, delimiter=',')
		
		for row in data:
			newItem = row
			counties.append(newItem)

		counties.sort(key=itemgetter(1), reverse=True)

	if DEBUG:
		for c in counties:
			print c

	return counties 

def save2csv(filename, sevCorr):
	"""
	writes csv data to a file

	Keyword arguments:
	filename -- the csv file to write to
	sevCorr -- the data to write
	"""
	with open(filename, 'w') as fp:
	    writer = csv.writer(fp, delimiter=',')
	    writer.writerow(["County", "Correlation"])
	    writer.writerows(sevCorr)
	

# execute
def main():
    # parse command line options
    try:
    	print "\n\nProcessing green counties"
        print "----------------------"
        greenCounties = getCounties('sev_unemp_map_green.txt')

        print "\n\nProcessing blue counties"
        print "----------------------"
        blueCounties = getCounties('sev_unemp_map_blue.txt')
        
        
        save2csv('green_sev_corr.csv',greenCounties)
        save2csv('blue_sev_corr.csv',blueCounties)
    except getopt.error, msg:
		print msg
		sys.exit(2)

if __name__ == "__main__":
	main()




