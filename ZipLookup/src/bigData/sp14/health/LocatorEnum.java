package bigData.sp14.health;

/**
 * Enums that adhere to the structure of the csv file containing zipcode data.
 * File attainable from http://www.unitedstateszipcodes.org/zip-code-database/
 * 
 * @author Khen price
 *
 */
public enum LocatorEnum{
    ZIP(0), TYPE(1), PRIMARY_CITY(2), ACCEPTABLE_CITIES(3), 
    UNACCEPTABLE_CITIES(4), STATE(5), COUNTY(6), TIMEZONE(7), 
    AREA_CODES(8), LATITUDE(9), LONGITUDE(10), WORLD_REGION(11), 
    COUNTRY(12), DECOMMISSIONED(13), ESTIMATED_POPULATION(14), NOTES(15);
    
    private final int value;
    private LocatorEnum(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
}
