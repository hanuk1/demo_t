package com.example.demo.rest;

import com.example.demo.beans.FileBean;
import com.example.demo.beans.FileResponse;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class MountApi {

    @Autowired
    DemoService demoService;

    @RequestMapping(path = "/files")
    public FileResponse getTimeForStop(@RequestParam("absolutePath") String absolutePath
    ) {
        List<FileBean> filesList = new ArrayList<>();
        demoService.getFilesForPath(absolutePath, filesList);
        Collections.sort(filesList, Comparator.comparingLong(FileBean::getSize).reversed());
        return FileResponse.builder().files(filesList).build();
    }
}
