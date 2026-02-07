package com.saji.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Detailed exception information for debugging
 *
 * @author sajethperli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetail {

    /**
     * Exception class name
     */
    private String exceptionClass;

    /**
     * Exception message
     */
    private String message;

    /**
     * Stack trace elements with file names and line numbers
     */
    private List<StackTraceInfo> stackTrace;

    /**
     * Cause exception details (if exists)
     */
    private ExceptionDetail cause;

    /**
     * Individual stack trace element information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StackTraceInfo {

        /**
         * Class name where the exception occurred
         */
        private String className;

        /**
         * Method name where the exception occurred
         */
        private String methodName;

        /**
         * File name where the exception occurred
         */
        private String fileName;

        /**
         * Line number where the exception occurred
         */
        private Integer lineNumber;

        /**
         * Full formatted stack trace line
         */
        private String location;

        public static StackTraceInfo from(StackTraceElement element) {
            return StackTraceInfo.builder()
                    .className(element.getClassName())
                    .methodName(element.getMethodName())
                    .fileName(element.getFileName())
                    .lineNumber(element.getLineNumber())
                    .location(String.format("%s.%s(%s:%d)",
                            element.getClassName(),
                            element.getMethodName(),
                            element.getFileName() != null ? element.getFileName() : "Unknown",
                            element.getLineNumber()))
                    .build();
        }
    }
}

