package com.banking.online_banking.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class LogMessage {

    private String className;
    private String methodName;
    private String methodArgs;
    private Long elapsedTimeInMillis;
    private Object result;


    @Override
    public String toString()   {
        String message=null;
        try {
             message = "LogMessage{" +
                    "className='" + className + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", methodArgs='" + methodArgs + '\'' +
                    ", elapsedTimeInMillis=" + elapsedTimeInMillis +
                    ", result=" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this.result) +
                    '}';
        }
        catch (JsonProcessingException e) {
            log.error("JsonProcessingException occurred"+e.getMessage());
        }
        return message;
    }
}
