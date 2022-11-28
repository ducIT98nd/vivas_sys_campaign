package com.vivas.campaignxsync.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "properties.current-participant")
public class CurrentParticipantProperty {
    private String vnp;
    private String vtl;
    private String vms;
    private String vnm;
    private String gtel;

    public String getVnp() {
        return vnp;
    }

    public void setVnp(String vnp) {
        this.vnp = vnp;
    }

    public String getVtl() {
        return vtl;
    }

    public void setVtl(String vtl) {
        this.vtl = vtl;
    }

    public String getVms() {
        return vms;
    }

    public void setVms(String vms) {
        this.vms = vms;
    }

    public String getVnm() {
        return vnm;
    }

    public void setVnm(String vnm) {
        this.vnm = vnm;
    }

    public String getGtel() {
        return gtel;
    }

    public void setGtel(String gtel) {
        this.gtel = gtel;
    }
}
