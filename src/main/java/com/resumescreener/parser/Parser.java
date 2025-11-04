package com.resumescreener.parser;

import java.io.File;

public abstract class Parser {
    protected File file;

    // Constructor
    public Parser(File file) {
        this.file = file;
    }

    // Abstract method to parse file
    public abstract String parseText() throws Exception;
}
