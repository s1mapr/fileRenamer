package com.s1ma.filerenamer.controllers;

import com.s1ma.filerenamer.services.RenameFilesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RenameFilesService renameFileService;

    @GetMapping("/")
    public String getMainPage(){
        return "index";
    }

    @PostMapping
    public String renameAllFiles(@RequestParam("pattern") String pattern,
                                 @RequestParam("filesPath") String filePath,
                                 HttpServletRequest request){
        String lineEnds = request.getParameter("lineEnds");
        renameFileService.readFilesMethod(filePath, pattern, lineEnds);
        return "redirect:/";
    }

}
