package com.fzwywm.visitor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Visitor {

    private String token;
    private Integer fortune;
    private LocalDate fortuneDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastSeenAt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getFortune() {
        return fortune;
    }

    public void setFortune(Integer fortune) {
        this.fortune = fortune;
    }

    public LocalDate getFortuneDate() {
        return fortuneDate;
    }

    public void setFortuneDate(LocalDate fortuneDate) {
        this.fortuneDate = fortuneDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(LocalDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }
}
