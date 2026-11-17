package com.fzwywm.visitor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class VisitorService {

    private final VisitorMapper visitorMapper;

    public VisitorService(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    @Transactional
    public void touch(String token) {
        LocalDate today = LocalDate.now();
        Visitor visitor = visitorMapper.find(token);
        if (visitor == null) {
            visitorMapper.insert(token, today);
            return;
        }
        if (!today.equals(visitor.getFortuneDate())) {
            visitorMapper.resetFortune(token, today);
        } else {
            visitorMapper.touch(token);
        }
    }

    public Visitor require(String token) {
        Visitor visitor = visitorMapper.find(token);
        if (visitor == null) {
            touch(token);
            visitor = visitorMapper.find(token);
        }
        return visitor;
    }

    public void addFortune(String token, int points) {
        visitorMapper.addFortune(token, points);
    }
}
