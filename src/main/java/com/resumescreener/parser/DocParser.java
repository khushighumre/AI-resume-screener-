package com.resumescreener.parser;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.File;
import java.io.FileInputStream;

public class DocParser extends Parser {

    public DocParser(File file) {
        super(file);
    }

    @Override
    public String parseText() throws Exception {
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".docx")) {
            return parseDocx();
        } else if (fileName.endsWith(".doc")) {
            throw new UnsupportedOperationException("Legacy .doc files are not supported. Please convert to .docx format.");
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }
    
    private String parseDocx() throws Exception {
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        extractor.close();
        document.close();
        fis.close();
        return text;
    }
}
