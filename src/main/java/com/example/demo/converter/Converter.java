package com.example.demo.converter;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Project;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class Converter<DTO, Entity> {

    public Entity convertToEntity(DTO dto) {
        TypeVariable[] typeVariables = getClass().getTypeParameters();
        for (TypeVariable typeVariable : typeVariables) {
//            typeVariable
        }
        return null;
    }

    public static void main(String[] args) {
        Converter<ProjectDto, Project> converter = new Converter();
        ProjectDto dto = new ProjectDto();
        converter.convertToEntity(dto);
    }

}
