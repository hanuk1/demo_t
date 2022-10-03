package com.example.demo.service;

import com.example.demo.beans.AppError;
import com.example.demo.beans.Direction;
import com.example.demo.beans.FileBean;
import com.example.demo.beans.Route;
import com.example.demo.beans.Stop;
import com.example.demo.exception.ApplicationException;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemoService {
    @Autowired
    MetroService metroService;

    public List<Route> getRoutes() {
        return metroService.getRoutes();
    }

    public List<Direction> getDirections(String routeId) {
        return metroService.getDirections(routeId);
    }

    public List<Stop> getStops(String routeId, String directionId) {
        return metroService.getStops(routeId, directionId);
    }

    public Route getRouteByName(String name) {
        List<Route> routes = getRoutes().stream().filter(route -> route.getRouteLabel().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        if (routes.size() == 0) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("No Matches Found for route").build());
        } else if (routes.size() > 1) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("Multiple Routes found. Please refine your search").build());
        }
        return routes.get(0);
    }

    public Direction getDirectionByName(String routeId, String directionName) {
        List<Direction> directions = getDirections(routeId).stream().filter(d -> d.getDirectionName().toLowerCase().contains(directionName.toLowerCase()))
                .collect(Collectors.toList());
        if (directions.size() == 0) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("No Matches Found for direction").build());
        } else if (directions.size() > 1) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("Multiple Directions found. Please refine your search").build());
        }
        return directions.get(0);
    }


    public Stop getStopByName(String routeId, String directionId, String stopName) {
        List<Stop> stops
                = getStops(routeId, directionId).stream().filter(s -> s.getDescription().toLowerCase().contains(stopName.toLowerCase()))
                .collect(Collectors.toList());
        if (stops.size() == 0) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("No Matches Found for stop").build());
        } else if (stops.size() > 1) {
            throw new ApplicationException(AppError.builder().httpStatus(HttpStatus.BAD_REQUEST).errorMessage("Multiple Stops found. Please refine your search").build());
        }
        return stops.get(0);
    }


    public String getTimeForStop(String routeName,
                                 String directionName,
                                 String stopName
    ) {
        Route route = getRouteByName(routeName);
        Direction direction = getDirectionByName(route.getRouteId(), directionName);
        String directionId = String.valueOf(direction.getDirectionId());
        Stop stop = getStopByName(route.getRouteId(), String.valueOf(direction.getDirectionId()), stopName);
        String placeScheduleJson = metroService.getTimeForStop(
                route.getRouteId(), directionId, stop.getPlaceCode());
        JSONArray jsonArray =
                JsonPath.read(placeScheduleJson, "$.departures[*].departure_time");
        long earliest = Long.MAX_VALUE;
        long currentTime = Instant.now().toEpochMilli() / 1000;
        for (int i = 0; i < jsonArray.size(); i++) {
            long departureTimeSecs = Long.valueOf((jsonArray.get(i).toString()));
            if (departureTimeSecs > currentTime && departureTimeSecs < earliest) {
                earliest = departureTimeSecs;
            }
        }
        if (earliest == Long.MAX_VALUE) {
            return "No trip found for the given stop";
        }
        return Instant.ofEpochSecond(earliest).atZone(ZoneId.of("America/Los_Angeles")).toString();

    }

    public void getFilesForPath(String absolutePath, List<FileBean> filesList) {
        File file = new File(absolutePath);
        if (! file.exists()) {
            return;
        }
        if(! file.isDirectory()) {
            filesList.add(FileBean.builder().name(file.getAbsolutePath()).size(file.length()).build());
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    getFilesForPath(f.getAbsolutePath(), filesList);
                } else {
                    filesList.add(FileBean.builder().name(f.getAbsolutePath()).size(f.length()).build());
                }
            }
        }
    }

}
