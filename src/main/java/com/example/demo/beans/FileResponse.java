package com.example.demo.beans;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class FileResponse {
    private List<FileBean> files;
}
