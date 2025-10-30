package com.example.spring_batch.partitioner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RangePartitioner implements Partitioner {

    private final String inputFile;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        try {
            long totalLines = countLines();
            System.out.println("totalLinesssssssssssssssssssssssssssssssssssssssssss" + totalLines);
            System.out.println("22222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
            long linesPerPartition = totalLines / gridSize;
            long remainder = totalLines % gridSize;

            long currentLine = 1; // Skip header

            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();

                long fromLine = currentLine;
                long toLine = currentLine + linesPerPartition + (i < remainder ? 1 : 0);

                context.putLong("fromLine", fromLine);
                context.putLong("toLine", toLine);
                context.putString("partitionName", "partition" + i);

                partitions.put("partition" + i, context);

                log.info("🔹 Partition {}: lines {} to {} ({} lines)",
                        i, fromLine, toLine, toLine - fromLine);

                currentLine = toLine;
            }

        } catch (IOException e) {
            log.error("Error counting lines in file", e);
            throw new RuntimeException("Failed to partition file", e);
        }

        return partitions;
    }

    private long countLines() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.lines().count() - 1; // Trừ header
        }
    }
}