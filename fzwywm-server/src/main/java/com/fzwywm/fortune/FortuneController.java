package com.fzwywm.fortune;

import com.fzwywm.visitor.VisitorInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    public FortuneController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @GetMapping
    public FortuneStateDto state(@RequestAttribute(VisitorInterceptor.ATTR_TOKEN) String token) {
        return fortuneService.state(token);
    }

    @GetMapping("/daily")
    public DailyFortuneDto daily() {
        return fortuneService.daily();
    }

    @PostMapping("/actions")
    public FortuneActionResult act(@RequestAttribute(VisitorInterceptor.ATTR_TOKEN) String token,
                                   @RequestBody FortuneActionRequest request) {
        return fortuneService.act(token, request);
    }
}
