package com.resumescreener;

import com.resumescreener.extractor.DataExtractor;
import com.resumescreener.model.Candidate;
import com.resumescreener.parser.PDFParser;
import com.resumescreener.parser.DocParser;
import com.resumescreener.parser.Parser;
import com.resumescreener.storage.JSONWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            File folder = new File("src/main/java/resources/sample_resumes/");
            File[] files = folder.listFiles((dir, name) -> 
                name.endsWith(".pdf") || name.endsWith(".docx"));

            if (files == null || files.length == 0) {
                System.out.println("No resume files found in src/main/java/resources/sample_resumes/");
                return;
            }

            List<Candidate> candidates = new ArrayList<>();
            DataExtractor extractor = new DataExtractor();

            for (File file : files) {
                try {
                    Parser parser;
                    String fileName = file.getName().toLowerCase();
                    
                    if (fileName.endsWith(".pdf")) {
                        parser = new PDFParser(file);
                    } else if (fileName.endsWith(".docx")) {
                        parser = new DocParser(file);
                    } else {
                        continue; // Skip unsupported files
                    }
                    
                    String text = parser.parseText();
                    Candidate candidate = extractor.extract(text);
                    candidates.add(candidate);
                    System.out.println("Processed: " + file.getName());
                    
                } catch (Exception e) {
                    System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
                }
            }

            JSONWriter writer = new JSONWriter();
            writer.writeToFile(candidates, "output/resumes.json");

            System.out.println("Successfully processed " + candidates.size() + " resume(s) and generated JSON file!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}