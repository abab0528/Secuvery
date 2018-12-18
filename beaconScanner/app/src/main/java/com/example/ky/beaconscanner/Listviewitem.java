package com.example.ky.beaconscanner;

public class Listviewitem {
    public Listviewitem(String name,String phone,String company,String product,String meter) {
        this.name=name;
        this.phone=phone;
        this.company=company;
        this.product=product;
        this.meter=meter;
    }

    public Boolean getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public String getProduct() {
        return product;
    }

    public String getMeter() {
        return meter;
    }

    private String name;
    private String company;
    private String phone;
    private String product;
    private String meter;
    private boolean flag;

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getUUID() {

        return UUID;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getMinor() {
        return Minor;
    }

    public void setMinor(String minor) {
        Minor = minor;
    }

    private String UUID;
    private String Major;
    private String Minor;

}
