package com.example.demo.service;

import com.example.demo.beans.Route;
import com.example.demo.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DemoServiceTest {
    @Mock
    MetroService metroService;
    @InjectMocks
    DemoService demoService;

    @Test
    void getRoutes() {
        Route route = Route.builder().routeId("1").routeLabel("one").build();
        when(metroService.getRoutes()).thenReturn(Collections.singletonList(route));
        List<Route> returnedRoutes = demoService.getRoutes();
        assertEquals(1, returnedRoutes.size());
        assertEquals("1", returnedRoutes.get(0).getRouteId());
    }

    @Test
    void getRoutesByName() {
        String routeName  = "Paseo Padre Route";
        Route route = Route.builder().routeId("1").routeLabel(routeName).build();
        when(metroService.getRoutes()).thenReturn(Collections.singletonList(route));
        Route matchedRoute = demoService.getRouteByName("padre");
        assertEquals(routeName, matchedRoute.getRouteLabel());
    }

    @Test
    void getRoutesByNameNoMatch() {
        Route route = Route.builder().routeId("1").routeLabel("one").build();
        when(metroService.getRoutes()).thenReturn(Collections.singletonList(route));
        assertThrows(ApplicationException.class, () -> demoService.getRouteByName("hello"));
    }




}
