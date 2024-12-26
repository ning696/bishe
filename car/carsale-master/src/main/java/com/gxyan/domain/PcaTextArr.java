package com.gxyan.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PcaTextArr {
    private List children =new ArrayList<>();
    private String label;
    private String value;
}
