package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            File f = new File(filePath);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            if (!f.exists()) f.createNewFile();
        } catch (Exception e) {
            System.out.println("Error creating data file: " + e.getMessage());
        }
    }

    public List<String> loadLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.trim().isEmpty()) lines.add(l);
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
        return lines;
    }

    public void saveLines(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
