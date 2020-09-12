package com.example.logcollect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import java.io.File;
import java.util.Set;

@RestController
@RequestMapping("/mammoth")
public class mammothHandler {
    @GetMapping("/queryHtml")
    public String queryHtml(){
        String html = "";
        try{
            DocumentConverter converter = new DocumentConverter();
            Result<String> result = converter.convertToHtml(new File("doc/demo.docx"));
            html = result.getValue(); // The generated HTML
            Set<String> warnings = result.getWarnings(); // Any warnings during conversion
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return html;
    }
}