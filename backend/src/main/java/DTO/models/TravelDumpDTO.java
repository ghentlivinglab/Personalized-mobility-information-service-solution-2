package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TravelDumpDTO {
    private TravelDTO travel;
    private List<RouteDTO> routes;

    public TravelDumpDTO() {
    }

    public TravelDumpDTO(TravelDTO travel, List<RouteDTO> routes) {
        this.travel = travel;
        this.routes = routes;
    }

    @JsonProperty("travel")
    public TravelDTO getTravel() {
        return travel;
    }

    public void setTravel(TravelDTO travel) {
        this.travel = travel;
    }

    @JsonProperty("routes")
    public List<RouteDTO> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteDTO> routes) {
        this.routes = routes;
    }
    
}
