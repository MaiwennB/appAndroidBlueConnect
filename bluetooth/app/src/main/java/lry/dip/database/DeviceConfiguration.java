package lry.dip.database;

import android.content.Intent;

public class DeviceConfiguration {
    private String m_macAddress;
    private Intent  m_launchIntent;

    /**
     * Default constructor
     */
    public DeviceConfiguration(){
        this(null, null);
    }

    /**
     * Constructor with mac address
     * @param macAddress
     */
    public DeviceConfiguration(String macAddress){
        this(macAddress, null);
    }

    /**
     * Constructor with mac address and launchIntent values
     * @param macAddress
     * @param launchIntent
     */
    public DeviceConfiguration(String macAddress, Intent launchIntent){
        setMacAddress(macAddress);
        setLaunchIntent(launchIntent);
    }

    public String getMacAddress(){
        return m_macAddress;
    }

    public void setMacAddress(String macAddress){
        m_macAddress = macAddress;
    }

    public Intent getLaunchIntent(){
        return m_launchIntent;
    }

    public void setLaunchIntent(Intent launchIntent){
        m_launchIntent = launchIntent;
    }

    public String toString(){
        return String.format("%s : %s => %s", DeviceConfiguration.class, m_macAddress, m_launchIntent.getAction());
    }
}
