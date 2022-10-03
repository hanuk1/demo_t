package com.example.demo.service;

import com.example.demo.beans.AppError;
import com.example.demo.beans.Direction;
import com.example.demo.beans.Route;
import com.example.demo.beans.Stop;
import com.example.demo.exception.ApplicationException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MetroService {
    private static final String BASE_URL = "https://svc.metrotransit.org";
    private final WebClient webClient = WebClient.create();

    public List<Route> getRoutes() {
        return webClient.get().uri(BASE_URL.concat("/nextripv2/routes"))
                .retrieve()
                .onRawStatus(st -> st == 404, clientResponse -> Mono.empty())
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(
                        new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Routes. Please check your input or try again later.").build())
                ))
                .bodyToMono(new ParameterizedTypeReference<List<Route>>() {
                })
                .onErrorMap(ex -> new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Routes. Please check your input or try again later.").build()))
                .block();
    }

    public List<Direction> getDirections(String routeId) {
        return webClient.get().uri(BASE_URL.concat("/nextripv2/directions/").concat(routeId))
                .retrieve()
                .onRawStatus(st -> st == 404, clientResponse -> Mono.empty())
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(
                        new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Directions. Please check your input or try again later.").build())
                ))
                .bodyToMono(new ParameterizedTypeReference<List<Direction>>() {
                })
                .onErrorMap(ex -> new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Directions. Please check your input or try again later.").build()))
                .block();
    }

    public List<Stop> getStops(String routeId, String directionId) {
        return webClient.get().uri(BASE_URL.concat("/nextripv2/stops/").concat(routeId).concat("/").concat(directionId))
                .retrieve()
                .onRawStatus(st -> st == 404, clientResponse -> Mono.empty())
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(
                        new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Stops. Please check your input or try again later.").build())
                ))
                .bodyToMono(new ParameterizedTypeReference<List<Stop>>() {
                })
                .onErrorMap(ex -> new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting Stops. Please check your input or try again later.").build()))
                .block();
    }


    public String getTimeForStop(String routeId,
                                 String directionId,
                                 String placeCode
    ) {
        String url = BASE_URL.concat("/nextripv2/").concat(routeId).concat("/")
                .concat(directionId).concat("/").concat(placeCode);
        return webClient.get().uri(url)
                .retrieve()
                .onRawStatus(st -> st == 404, clientResponse -> Mono.empty())
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(
                        new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting next trip time. Please check your input or try again later.").build())
                ))
                .bodyToMono(String.class)
                .onErrorMap(ex -> new ApplicationException(AppError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Error while getting next trip time. Please check your input or try again later.").build()))
                .block();
    }
}
