package com.resumescreener.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class PDFParser extends Parser {

    public PDFParser(File file) {
        super(file);
    }

    @Override
    public String parseText() throws Exception {
        PDDocument document = Loader.loadPDF(file);

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);   // keep natural reading order
        stripper.setWordSeparator(" ");     // avoid words sticking together
        stripper.setLineSeparator("\n");    // ensure proper line breaks
        stripper.setAddMoreFormatting(true);// preserve indentation + bullets

        String text = stripper.getText(document);
        document.close();

        return text.trim();
    }
}

