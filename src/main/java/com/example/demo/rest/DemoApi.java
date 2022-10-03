package com.example.demo.rest;

import com.example.demo.beans.Direction;
import com.example.demo.beans.Route;
import com.example.demo.beans.Stop;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoApi {

    @Autowired
    DemoService demoService;

    @RequestMapping(path = "/routes")
    public List<Route> routes() {
        List<Route> routes = demoService.getRoutes();
        return routes;
    }

    @RequestMapping(path = "/directions/{routeId}")
    public List<Direction> directions(@PathVariable("routeId") String routeId) {
        List<Direction> directions = demoService.getDirections(routeId);
        return directions;
    }

    @RequestMapping(path = "/stops/{routeId}/{directionId}")
    public List<Stop> stops(@PathVariable("routeId") String routeId,
    @PathVariable("directionId") String directionId
    ) {
        List<Stop> stops = demoService.getStops(routeId, directionId);
        return stops;
    }


    @RequestMapping(path = "/routeByName")
    public Route getRouteByName(@RequestParam("name") String routeName) {
        Route route = demoService.getRouteByName(routeName);
        return route;
    }


    @RequestMapping(path = "/nextDepartureTime")
    public String getTimeForStop(@RequestParam("routeName") String routeName,
    @RequestParam("directionName") String directionName,
            @RequestParam("stopName") String stopName
    ) {
        return "Next Departure Time: " + demoService.getTimeForStop(routeName, directionName, stopName);
    }

}
