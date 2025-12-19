package com.email.writer.app.DTO;

import lombok.Data;

@Data
public class EmailRequest {
    
    private String emailContent;
    private String tone;
}
