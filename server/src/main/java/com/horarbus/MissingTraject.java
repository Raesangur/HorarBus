package com.horarbus;

import java.sql.Timestamp;
import com.google.maps.model.TravelMode;

public class MissingTraject {
    private String startPlaceId;
    private String endPlaceId;
    private TravelMode transport;
    private Timestamp arrivalTime;

    public MissingTraject(String startPlaceId, String endPlaceId, TravelMode transport,
            Timestamp arrivalTime) {
        this.startPlaceId = startPlaceId;
        this.endPlaceId = endPlaceId;
        this.transport = transport;
        this.arrivalTime = arrivalTime;
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
                ? baseComparison && arrivalTime.equals(missing.arrivalTime)
                : baseComparison;
    }

    @Override
    public int hashCode() {
        return (transport.toString() + startPlaceId + endPlaceId).hashCode();
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

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public String getFilename() {
        return startPlaceId + endPlaceId + transport.toString()
                + (arrivalTime != null ? Long.toString(arrivalTime.getTime()) : "");
    }

    @Override
    public String toString() {
        return "MissingTraject: [" + startPlaceId + ", " + endPlaceId + ", " + transport + ", "
                + arrivalTime + "]";
    }
}
