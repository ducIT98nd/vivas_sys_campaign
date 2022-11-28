package com.vivas.campaignxsync.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    @Autowired
    private CurrentParticipantProperty participantProperty;

    @Autowired
    private TelcoIdProperty telcoId;

    public int convertTelcoId(String currentParticipant) throws NullPointerException {
        if (currentParticipant.equals(participantProperty.getVnp()))
            return telcoId.getVnp();
        else if (currentParticipant.equals(participantProperty.getVtl()))
            return telcoId.getVtl();
        else if (currentParticipant.equals(participantProperty.getVms()))
            return telcoId.getVms();
        else if (currentParticipant.equals(participantProperty.getVnm()))
            return telcoId.getVnm();
        else if (currentParticipant.equals(participantProperty.getGtel()))
            return telcoId.getGtel();
        else
            throw new NullPointerException();
    }
}
