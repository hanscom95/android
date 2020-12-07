package artech.com.semi.utility;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Created by moon on 2018-07-03.
 */

public class AddressesByNameIntentService extends IntentService {

    private static final String IDENTIFIER = "AddressesByNameIS";
    private ResultReceiver addressResultReceiver;

    public AddressesByNameIntentService() {
        super(IDENTIFIER);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String msg = "";
        addressResultReceiver = intent.getParcelableExtra("address_receiver");

        if (addressResultReceiver == null) {
            Log.e(IDENTIFIER,
                    "No receiver in intent");
            return;
        }

        String addressName = intent.getStringExtra("address_name");

        if (addressName == null) {
            msg = "No name found";
            sendResultsToReceiver(0, msg, null, null, null);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.KOREAN);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(addressName, 5);
        } catch (Exception ioException) {
            Log.e("", "Error in getting addresses for the given name");
        }

        if (addresses == null || addresses.size()  == 0) {
            msg = "No address found for the address name";
            sendResultsToReceiver(1, msg, null, null, null);
        } else {
            Log.d(IDENTIFIER, "number of addresses received "+addresses.size());
            String[] addressList = new String[addresses.size()] ;
            int j =0;
            double[] lat = new double[addresses.size()], lon = new double[addresses.size()];
            for(Address address : addresses){
                ArrayList<String> addressInfo = new ArrayList<>();
                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressInfo.add(address.getAddressLine(i));
                }
                addressList[j] = TextUtils.join(System.getProperty("line.separator"),
                        addressInfo);
                lat[j] = address.getLatitude();
                lon[j] = address.getLongitude();
                j++;
            }
            sendResultsToReceiver(2,"", addressList, lat, lon);
        }
    }
    private void sendResultsToReceiver(int resultCode, String message, String[] addressList, double[] lat, double[] lon) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", message);
        bundle.putStringArray("addressList", addressList);
        bundle.putDoubleArray("lat", lat);
        bundle.putDoubleArray("lon", lon);
        addressResultReceiver.send(resultCode, bundle);
    }
}