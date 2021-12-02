package com.horarbus;

import java.sql.Timestamp;
import com.google.maps.model.TravelMode;

public class MissingTraject {
    private String startPlaceId;
    private String endPlaceId;
    private TravelMode transport;
    private Timestamp definingTime;
    private boolean isArriving;

    public MissingTraject(String startPlaceId, String endPlaceId, TravelMode transport,
            Timestamp definingTime, boolean isArriving) {
        this.startPlaceId = startPlaceId;
        this.endPlaceId = endPlaceId;
        this.transport = transport;
        this.definingTime = definingTime;
        this.isArriving = isArriving;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MissingTraject missing = (MissingTraject) obj;
        boolean baseComparison = startPlaceId.equals(missing.startPlaceId)
                && endPlaceId.equals(missing.endPlaceId) && transport.equals(missing.transport);
        return transport == TravelMode.TRANSIT
                ? baseComparison && definingTime.toLocalDateTime().toLocalTime()
                        .equals(missing.definingTime.toLocalDateTime().toLocalTime())
                : baseComparison;
    }

    @Override
    public int hashCode() {
        return (transport.toString() + "start_" + startPlaceId + "end_ " + endPlaceId).hashCode();
    }

    public String getStartPlaceId() {
        return startPlaceId;
    }

    public String getEndPlaceId() {
        return endPlaceId;
    }

    public TravelMode getTravelMode() {
        return transport;
    }

    public Timestamp getDefiningTime() {
        return definingTime;
    }

    public boolean getIsArriving() {
        return isArriving;
    }

    public String getFilename() {
        String dateStr = definingTime.toLocalDateTime().toLocalTime().toString().replace(":", "_");
        return startPlaceId + endPlaceId + transport.toString()
                + (definingTime != null ? dateStr : "");
    }


    @Override
    public String toString() {
        return "MissingTraject: [" + startPlaceId + ", " + endPlaceId + ", " + transport + ", "
                + definingTime + "]";
    }
}
