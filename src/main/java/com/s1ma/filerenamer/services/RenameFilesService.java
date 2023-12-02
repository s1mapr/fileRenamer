package com.s1ma.filerenamer.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RenameFilesService {
    public void readFilesMethod(String folderPath, String pattern, String lineEnds) {

        File folder = new File(folderPath);

        File[] files = folder.listFiles();

        String[] array = parseString(lineEnds);

        if (files != null && files.length == array.length) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String[] parsedFileName = file.getName().split("\\.");
                String fileType = "." + parsedFileName[parsedFileName.length-1];
                String newName = pattern + array[i] + fileType;
                if (file.renameTo(new File(folder, newName))) {
                    System.out.println("Файл успешно переименован: " + newName);
                } else {
                    System.out.println("Не удалось переименовать файл: " + file.getName());
                }
            }
        } else {
            System.out.println("Папка пуста или произошла ошибка при чтении файлов.");
        }

    }

    private String[] parseString(String input) {
        List<String> elements = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\s*([^,]+)\\s*,?");

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            elements.add(matcher.group(1));
        }

        return elements.toArray(new String[0]);
    }
}
