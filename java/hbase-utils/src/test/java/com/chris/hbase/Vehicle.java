package com.chris.hbase;

import java.sql.Timestamp;

/**
 * Create by Chris Chen
 * Create on 2019-03-11 09:46 星期一
 * This class be use for:
 */
public class Vehicle {
    public String tenantId;
    public String vin;
    public String batteryPackCode;
    public String vehicleStatus;
    public String chargeStatus;
    public Timestamp time;
    public Double voltage;

    public Vehicle() {
    }

    public Vehicle(String tenantId, String vin, String batteryPackCode, String vehicleStatus, String chargeStatus, Timestamp time, Double voltage) {
        this.tenantId = tenantId;
        this.vin = vin;
        this.batteryPackCode = batteryPackCode;
        this.vehicleStatus = vehicleStatus;
        this.chargeStatus = chargeStatus;
        this.time = time;
        this.voltage = voltage;
    }

    public static Vehicle create(String tenantId, String vin, String batteryPackCode, String vehicleStatus, String chargeStatus, Timestamp time, Double voltage) {
        return new Vehicle(tenantId, vin, batteryPackCode, vehicleStatus, chargeStatus, time, voltage);
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBatteryPackCode() {
        return batteryPackCode;
    }

    public void setBatteryPackCode(String batteryPackCode) {
        this.batteryPackCode = batteryPackCode;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "tenantId='" + tenantId + '\'' +
                ", vin='" + vin + '\'' +
                ", batteryPackCode='" + batteryPackCode + '\'' +
                ", vehicleStatus='" + vehicleStatus + '\'' +
                ", chargeStatus='" + chargeStatus + '\'' +
                ", time='" + time.getTime() + '\'' +
                ", voltage=" + voltage +
                '}';
    }
}
