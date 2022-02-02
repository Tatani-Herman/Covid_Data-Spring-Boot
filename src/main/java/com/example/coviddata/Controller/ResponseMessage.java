package com.example.coviddata.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ResponseMessage {
    private String message;
    private String fileDownloadUri;
}
