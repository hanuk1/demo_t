package com.example.demo.beans;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class FileBean {
    private String name;
    private long size;
}
