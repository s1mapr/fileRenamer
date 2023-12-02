package com.s1ma.filerenamer.controllers;

import com.s1ma.filerenamer.services.RenameFilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RenameFilesService renameFileService;

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @PostMapping("/")
    public void renameAllFiles(@RequestParam("pattern") String pattern,
                                       @RequestParam("files") MultipartFile[] files,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        String lineEnds = request.getParameter("lineEnds");
        String zipName = renameFileService.readFilesMethod(files, pattern, lineEnds);
        String uploadPath = "target/uploads";

        File zipFile = new File(uploadPath, zipName);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);

        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

            ZipEntry zipEntry = new ZipEntry(zipFile.getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            zos.closeEntry();
        }

        zipFile.delete();

        response.flushBuffer();
    }

}
