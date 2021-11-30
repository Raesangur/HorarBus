package com.horarbus;

import com.google.maps.model.TravelMode;

public class MissingTraject {
    private String startPlaceId;
    private String endPlaceId;
    private TravelMode transport;
    private long arrivalTime;

    public MissingTraject(String startPlaceId, String endPlaceId, TravelMode transport) {
        this(startPlaceId, endPlaceId, transport, 0);
    }

    public MissingTraject(String startPlaceId, String endPlaceId, TravelMode transport,
            long arrivalTime) {
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
        return startPlaceId.equals(missing.startPlaceId) && endPlaceId.equals(missing.endPlaceId)
                && transport.equals(missing.transport) && arrivalTime == missing.arrivalTime;
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

    public long getArrivalTime() {
        return arrivalTime;
    }

    public String getFilename() {
        return startPlaceId + endPlaceId + transport.toString()
                + Long.toString(arrivalTime % 86400);
    }

    @Override
    public String toString() {
        return "MissingTraject: [" + startPlaceId + ", " + endPlaceId + ", " + transport + ", "
                + arrivalTime + "]";
    }
}
