package com.example.demo.config;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class MarkdownViewResolver extends AbstractTemplateViewResolver {

    public MarkdownViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override
    protected String getContentType() {
        return "text/html;charset=UTF-8";
    }

    @Override
    protected Class<?> requiredViewClass() {
        return MarkdownView.class;
    }

}