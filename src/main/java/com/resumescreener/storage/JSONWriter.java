package com.resumescreener.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.resumescreener.model.Candidate;

import java.io.FileWriter;
import java.util.List;

public class JSONWriter {

    public void writeToFile(List<Candidate> candidates, String path) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(path);
        gson.toJson(candidates, writer);
        writer.flush();
        writer.close();
    }
}
