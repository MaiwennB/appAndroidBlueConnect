package lry.dip.database;

public class DeviceConfiguration {
    private String m_macAdress;
    private String m_launchIntent;

    public String getMacAdress(){
        return m_macAdress;
    }

    public void setMacAdress(String macAdress){
        m_macAdress = macAdress;
    }

    public String getLaunchIntent(){
        return m_launchIntent;
    }

    public void setLaunchIntent(String launchIntent){
        m_launchIntent = launchIntent;
    }
}
