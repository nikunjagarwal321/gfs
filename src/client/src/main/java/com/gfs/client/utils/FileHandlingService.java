package com.gfs.client.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class to handle File Operations
 */
@Service
@AllArgsConstructor
@Slf4j
public class FileHandlingService {


    private final static String basePath = "./files";

    /**
     * Function to split a given file to multiple chunks
     * @param FilePath Name of the file to split
     * @param chunksize size of individual chunks
     * @return number of chunks of size 'chunksize'
     */
    public static int splitFileToChunks(String FilePath, int chunksize) {
        FilePath = basePath + FilePath;
        int currentChunk = 0;
        int offset = 1, data;
        try {
            File filename = new File(FilePath);
            InputStream infile = new BufferedInputStream(new FileInputStream(filename));
            data = infile.read();
            while (data != -1) {
                filename = new File(FilePath +":" + offset);
                OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
                while (data != -1 && currentChunk < chunksize) {
                    outfile.write(data);
                    currentChunk++;
                    data = infile.read();
                }
                currentChunk = 0;
                outfile.close();
                offset++;
            }

        } catch (Exception e) {
            log.error("Exception in Client FileHandlingService :: splitFiletoChunks");
        }
        return offset;
    }
}
