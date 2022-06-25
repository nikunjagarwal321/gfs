package com.gfs.client.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service class to handle File Operations
 */
@Service
@AllArgsConstructor
@Slf4j
public class FileHandlingService {


    private final static String basePath = "./files";

    /**
     * Split a file into multiples files.
     *
     * @param fileName   Name of file to be split.
     * @param chunkSize maximum number of MB per file.
     * @throws IOException
     */
    public static long splitFileToChunks(final String fileName, final int chunkSize) throws IOException {

        final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1024L * 1024L * chunkSize;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, position, fileName);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, (int)numSplits, fileName);
                return numSplits + 1;
            }
        }
        return numSplits;
    }

    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel, int offset, String filePath) throws IOException {
        Path fileName = Paths.get(basePath, filePath + ":" + offset);
        try (RandomAccessFile toFile = new RandomAccessFile(fileName.toFile(), "rw");
             FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
    }

    public static void appendDataToFile(String fileName, String data) throws IOException{
        try(RandomAccessFile sourceFile = new RandomAccessFile(fileName, "rw")) {
            sourceFile.seek(sourceFile.length());
            sourceFile.writeUTF(data);
        }
    }
}
