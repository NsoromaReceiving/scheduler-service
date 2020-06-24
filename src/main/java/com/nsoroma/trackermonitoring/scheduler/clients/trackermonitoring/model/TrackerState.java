
package com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.model;


public class TrackerState {

    private String label;
    private String customerId;
    private String trackerId;
    private String imei;
    private String model;
    private String phoneNumber;
    private String connectionStatus;
    private String tariffEndDate;
    private String customerName;
    private String lastGpsUpdate;
    private String lastGsmUpdate;
    private String lastGpsSignalLevel;
    private String lastGpsLatitude;
    private String lastGpsLongitude;
    private String lastBatteryLevel;
    private String gsmSignalLevel;
    private String gsmNetworkName;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getTrackerId() { return trackerId; }
    public void setTrackerId(String trackerId) { this.trackerId = trackerId; }

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }

    public String getTariffEndDate() { return tariffEndDate; }
    public void setTariffEndDate(String tariffEndDate) { this.tariffEndDate = tariffEndDate; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getLastGpsUpdate() { return lastGpsUpdate; }
    public void setLastGpsUpdate(String lastGpsUpdate) { this.lastGpsUpdate = lastGpsUpdate; }

    public String getLastGpsSignalLevel() { return lastGpsSignalLevel; }
    public void setLastGpsSignalLevel(String lastGpsSignalLevel) { this.lastGpsSignalLevel = lastGpsSignalLevel; }

    public String getLastGpsLatitude() { return lastGpsLatitude; }
    public void setLastGpsLatitude(String lastGpsLatitude) { this.lastGpsLatitude = lastGpsLatitude; }

    public String getLastGpsLongitude() { return lastGpsLongitude; }
    public void setLastGpsLongitude(String lastGpsLongitude) { this.lastGpsLongitude = lastGpsLongitude; }

    public String getLastBatteryLevel() { return lastBatteryLevel; }
    public void setLastBatteryLevel(String lastBatteryLevel) { this.lastBatteryLevel = lastBatteryLevel; }

    public String getGsmSignalLevel() { return gsmSignalLevel; }
    public void setGsmSignalLevel(String gsmSignalLevel) { this.gsmSignalLevel = gsmSignalLevel; }

    public String getGsmNetworkName() { return gsmNetworkName; }
    public void setGsmNetworkName(String gsmNetworkName) { this.gsmNetworkName = gsmNetworkName; }

    public String getLastGsmUpdate() { return lastGsmUpdate; }

    public void setLastGsmUpdate(String lastGsmUpdate) { this.lastGsmUpdate = lastGsmUpdate; }
}
