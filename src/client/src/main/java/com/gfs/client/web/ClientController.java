package com.gfs.client.web;

import com.gfs.client.model.Response;
import com.gfs.client.model.request.WebRequest;
import com.gfs.client.service.ClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ClientController {

    @Autowired
    ClientImpl client;

    @GetMapping("/readfile")
    public ResponseEntity<Response> readFile(@RequestBody WebRequest webRequest) {
        return ResponseEntity.ok(client.readChunkData(webRequest.getFilename(), webRequest.getOffset()));
    }

    @PostMapping("/writefile")
    public ResponseEntity<Response> writeFile(@RequestBody WebRequest webRequest) throws IOException {
        client.writeChunkData(webRequest.getFilename(), webRequest.getData());
        return ResponseEntity.noContent().build();
    }
}
