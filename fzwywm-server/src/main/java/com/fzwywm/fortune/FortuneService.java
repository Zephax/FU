package com.fzwywm.fortune;

import com.fzwywm.common.ApiException;
import com.fzwywm.visitor.Visitor;
import com.fzwywm.visitor.VisitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class FortuneService {

    private static final int MAX_FORTUNE = 100;
    private static final Set<String> LAMP_REFS = Set.of("1", "2", "3", "4", "5");
    private static final Set<String> FLIP_REFS = Set.of("1", "2", "3");

    private final VisitorService visitorService;
    private final InteractionMapper interactionMapper;
    private final DailyFortuneService dailyFortuneService;

    public FortuneService(VisitorService visitorService,
                          InteractionMapper interactionMapper,
                          DailyFortuneService dailyFortuneService) {
        this.visitorService = visitorService;
        this.interactionMapper = interactionMapper;
        this.dailyFortuneService = dailyFortuneService;
    }

    public FortuneStateDto state(String token) {
        LocalDate today = LocalDate.now();
        Visitor visitor = visitorService.require(token);
        DailyFortune daily = dailyFortuneService.getOrCreate(today);

        return new FortuneStateDto(
                visitor.getFortune(),
                MAX_FORTUNE,
                interactionMapper.refs(token, "LAMP", today),
                interactionMapper.refs(token, "FLIP", today),
                interactionMapper.exists(token, "DRAW", "daily", today) > 0,
                interactionMapper.exists(token, "BLESS", "daily", today) > 0,
                new DailyFortuneDto(daily.getDay().toString(), daily.getContent())
        );
    }

    public DailyFortuneDto daily() {
        DailyFortune daily = dailyFortuneService.getOrCreate(LocalDate.now());
        return new DailyFortuneDto(daily.getDay().toString(), daily.getContent());
    }

    @Transactional
    public FortuneActionResult act(String token, FortuneActionRequest request) {
        if (request == null || request.action() == null) {
            throw ApiException.badRequest("缺少互动类型");
        }

        String action = request.action().trim().toUpperCase(Locale.ROOT);
        String ref = request.ref() == null ? "" : request.ref().trim();
        int points;

        switch (action) {
            case "DRAW" -> {
                ref = "daily";
                points = 8;
            }
            case "LAMP" -> {
                if (!LAMP_REFS.contains(ref)) {
                    throw ApiException.badRequest("福灯编号不正确");
                }
                points = 4;
            }
            case "FLIP" -> {
                if (!FLIP_REFS.contains(ref)) {
                    throw ApiException.badRequest("福俗卡编号不正确");
                }
                points = 6;
            }
            case "BLESS" -> {
                ref = "daily";
                points = 10;
            }
            default -> throw ApiException.badRequest("未知的互动类型");
        }

        LocalDate today = LocalDate.now();
        int inserted = interactionMapper.insertIgnore(token, action, ref, points, today);
        if (inserted > 0) {
            visitorService.addFortune(token, points);
        }

        Visitor visitor = visitorService.require(token);
        String text = "DRAW".equals(action)
                ? dailyFortuneService.getOrCreate(today).getContent()
                : null;

        return new FortuneActionResult(
                visitor.getFortune(),
                MAX_FORTUNE,
                inserted > 0 ? points : 0,
                inserted == 0,
                text
        );
    }
}
