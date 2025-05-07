package org.example.exeptions;

import lombok.Data;

@Data
public class AppError {
    private int status;
    private String message;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
