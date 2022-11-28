package com.vivas.campaignxsync.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "properties.telco-id")
public class TelcoIdProperty {
    private int vnp;
    private int vtl;
    private int vms;
    private int vnm;
    private int gtel;

    public int getVnp() {
        return vnp;
    }

    public void setVnp(int vnp) {
        this.vnp = vnp;
    }

    public int getVtl() {
        return vtl;
    }

    public void setVtl(int vtl) {
        this.vtl = vtl;
    }

    public int getVms() {
        return vms;
    }

    public void setVms(int vms) {
        this.vms = vms;
    }

    public int getVnm() {
        return vnm;
    }

    public void setVnm(int vnm) {
        this.vnm = vnm;
    }

    public int getGtel() {
        return gtel;
    }

    public void setGtel(int gtel) {
        this.gtel = gtel;
    }
}
