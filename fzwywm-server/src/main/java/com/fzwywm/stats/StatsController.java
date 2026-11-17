package com.fzwywm.stats;

import com.fzwywm.blessing.BlessingMapper;
import com.fzwywm.fortune.InteractionMapper;
import com.fzwywm.visitor.VisitorMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final BlessingMapper blessingMapper;
    private final InteractionMapper interactionMapper;
    private final VisitorMapper visitorMapper;

    public StatsController(BlessingMapper blessingMapper,
                           InteractionMapper interactionMapper,
                           VisitorMapper visitorMapper) {
        this.blessingMapper = blessingMapper;
        this.interactionMapper = interactionMapper;
        this.visitorMapper = visitorMapper;
    }

    @GetMapping("/api/stats")
    public StatsDto stats() {
        return new StatsDto(
                blessingMapper.countVisible(),
                interactionMapper.countByAction("LAMP"),
                interactionMapper.countByAction("DRAW"),
                visitorMapper.countAll()
        );
    }
}
