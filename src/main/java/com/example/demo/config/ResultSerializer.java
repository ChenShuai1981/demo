package com.example.demo.config;

import com.example.demo.dto.Result;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Optional;

public class ResultSerializer extends JsonSerializer<Result> {
    @Override
    public void serialize(Result result, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Optional<Result> resultOpt = Optional.of(result);
        if (resultOpt.isPresent()) {
            jsonGenerator.writeObject(resultOpt.get().toMap());
        } else {
            jsonGenerator.writeString("");
        }
    }
}
