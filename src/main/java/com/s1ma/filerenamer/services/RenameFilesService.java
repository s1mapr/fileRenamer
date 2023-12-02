package com.s1ma.filerenamer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RenameFilesService {
    public String readFilesMethod(MultipartFile[] files, String pattern, String lineEnds) {

        String[] array = parseString(lineEnds);
        List<String> fileNames = new ArrayList<>();
        if (files != null && files.length == array.length) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String[] parsedFileName = file.getOriginalFilename().split("\\.");
                String fileType = "." + parsedFileName[parsedFileName.length-1];
                String newName = pattern + array[i] + fileType;
                fileNames.add(newName);
                saveFile(file, newName);
            }
        }

        String zipFileName = createZipFile(fileNames);
        deleteFiles(fileNames);
        return zipFileName;
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

    private void saveFile(MultipartFile file, String newFileName) {
        try {
            byte[] bytes = file.getBytes();
            String uploadPath = "target/uploads";

            File newFile = new File(uploadPath + File.separator + newFileName);
            try (FileOutputStream stream = new FileOutputStream(newFile)) {
                stream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("something went wrong");
        }
    }

    private String createZipFile(List<String> fileNames) {
        String zipFileName = "archive.zip";
        String uploadPath = "target/uploads";

        try (FileOutputStream fos = new FileOutputStream(uploadPath + File.separator + zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String fileName : fileNames) {
                File file = new File(uploadPath + File.separator + fileName);
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                try (FileInputStream fis = new FileInputStream(file)) {
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
                zos.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return zipFileName;
    }

    private void deleteFiles(List<String> fileNames) {
        String uploadPath = "target/uploads";

        for (String fileName : fileNames) {
            File file = new File(uploadPath + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
