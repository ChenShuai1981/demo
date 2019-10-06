package com.example.demo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerHelloWorld {

    public static void main(String[] args) {

        // Configurae Freemarker
        Configuration cfg = new Configuration();
        try {
            // Load the template
            Template template = cfg.getTemplate("src/main/resources/template.ftl");

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("message", "Hello World!");

            // Language list
            List<String> language = new ArrayList<String>();
            language.add("Java");
            language.add("C++");
            language.add("Ceylon");
            language.add("Cobol");

            data.put("languages", language);

            Writer out = new OutputStreamWriter(System.out);
            template.process(data, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}