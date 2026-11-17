package com.fzwywm.fortune;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyFortune {

    private LocalDate day;
    private String content;
    private LocalDateTime createdAt;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
