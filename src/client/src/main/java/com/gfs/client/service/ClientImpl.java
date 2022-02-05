package com.gfs.client.service;

import com.gfs.client.model.MasterClientResponse;
import com.gfs.client.model.RequestType;
import com.gfs.client.model.Response;
import com.gfs.client.model.request.ClientMasterRequest;
import com.gfs.client.utils.FileHandlingService;
import com.gfs.client.utils.JsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientImpl implements CommandLineRunner {

    @Autowired
    ChunkserverConnectorServiceImpl chunkserverConnectorService;

    @Autowired
    MasterConnectorServiceImpl masterConnectorService;

    @Value("${chunkSize}")
    int chunkSize;

    @Override
    public void run(String... args) {
         //TODO : Make requests to the master then to chunkserver based on REST APIs
//        writeChunkData("file-1","Random data");
        readChunkData("file-1", 1);
    }


    public void readChunkData(String filename, int offset) {
        ClientMasterRequest clientMasterRequest = new ClientMasterRequest(filename, offset);
        Response<MasterClientResponse> masterClientResponseResponse = masterConnectorService.sendRequestToMaster(clientMasterRequest, RequestType.READ);
        chunkserverConnectorService.readChunkDataFromChunkServer(masterClientResponseResponse.getData());
    }

    public void writeChunkData(String filename, String data) {
        // implement breaking file into chunks
        int numberOfOffsets = FileHandlingService.splitFileToChunks(filename, chunkSize);
        for(int offset = 1;offset <= numberOfOffsets; offset++) {
            ClientMasterRequest clientMasterRequest = new ClientMasterRequest(filename, offset);
            Response<MasterClientResponse> masterClientResponseResponse = masterConnectorService.sendRequestToMaster(clientMasterRequest, RequestType.WRITE);
            MasterClientResponse masterClientResponse = JsonHandler.convertObjectToOtherObject(masterClientResponseResponse.getData(), MasterClientResponse.class);
            chunkserverConnectorService.writeChunkDataToChunkServer(masterClientResponse, data);
        }
    }
}
