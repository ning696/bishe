package com.gxyan.pojo.dto;

import com.gxyan.pojo.Car;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

import java.util.List;

@Data
public class StoreRequest {
    private Car car;
    private List<MultipartFile> files; // 使用 MultipartFile
}
