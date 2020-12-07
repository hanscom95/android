package com.standingegg.band.util;

public class Tools {
	public static final String IS_FALSE = "false";
	public static final String IS_TRUE = "true";
	public static final String DEVICE_ADDRESS = "deviceAddress";
	public static final String DISCONNECT = "disconnect";
    private Tools(){
    	
    };
 
    
    
    public static boolean isSupportUniversalAPI()
	{
		char[] universalVersion = {'4','.','3','.','0'};
		char[] currentVersion = getOSVersionNumber().toCharArray();
		if (universalVersion[0] > currentVersion[0]) 
		{
			return false;
		}
		if (universalVersion[0] < currentVersion[0]) 
		{
			return true;
		}
		if (universalVersion[0] == currentVersion[0]) {
			if (universalVersion[2] > currentVersion[2]) {
				return false;
			}else {
				return true;
			}
		}
		return false;
	}
    
    public static String getOSVersionNumber() {
		return android.os.Build.VERSION.RELEASE;
	}
    
    public static String getPhoneBrand() {
		return android.os.Build.BRAND;
	}
    public static String getPhoneManufacturers() {
		return android.os.Build.MANUFACTURER;
	}
    
    public static boolean isSupportSpecificAPI(String specificBrand)
	{
		if (getPhoneBrand().toLowerCase().indexOf(specificBrand.toLowerCase()) != -1 || 
				getPhoneManufacturers().toLowerCase().indexOf(specificBrand.toLowerCase()) != -1) 
		{
			return true;
		}else {
			return false;
		}
	}
}
