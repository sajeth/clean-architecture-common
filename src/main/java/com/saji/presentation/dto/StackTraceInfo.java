package com.saji.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter@Setter@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StackTraceInfo {

    private String className;
    private String methodName;
    private String fileName;
    private Integer lineNumber;
    private String location;


    public StackTraceInfo className(String className) { this.className = className; return this; }

    public StackTraceInfo methodName(String methodName) { this.methodName = methodName; return this; }

    public StackTraceInfo fileName(String fileName) { this.fileName = fileName; return this; }

    public StackTraceInfo lineNumber(Integer lineNumber) { this.lineNumber = lineNumber; return this; }

    public StackTraceInfo location(String location) { this.location = location; return this; }


}
