package com.gfs.client.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebRequest {
    String filename;
    int offset;
    String data;
}
