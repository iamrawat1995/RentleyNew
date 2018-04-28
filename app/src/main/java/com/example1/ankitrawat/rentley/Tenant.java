package com.example1.ankitrawat.rentley;

class Tenant
{
 private String TenantName,TenantMobile,TenantAdress;


    public Tenant()
    {

    }

    public Tenant(String tenantName, String tenantMobile, String tenantAdress) {
        TenantName = tenantName;
        TenantMobile = tenantMobile;
        TenantAdress = tenantAdress;
    }

    public String getTenantName() {
        return TenantName;
    }

    public void setTenantName(String tenantName) {
        TenantName = tenantName;
    }

    public String getTenantMobile() {
        return TenantMobile;
    }

    public void setTenantMobile(String tenantMobile) {
        TenantMobile = tenantMobile;
    }

    public String getTenantAdress() {
        return TenantAdress;
    }

    public void setTenantAdress(String tenantAdress) {
        TenantAdress = tenantAdress;
    }
}
