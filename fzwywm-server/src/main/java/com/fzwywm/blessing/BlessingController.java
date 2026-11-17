package com.fzwywm.blessing;

import com.fzwywm.visitor.VisitorInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blessings")
public class BlessingController {

    private final BlessingService blessingService;

    public BlessingController(BlessingService blessingService) {
        this.blessingService = blessingService;
    }

    @GetMapping
    public List<BlessingDto> list(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        return blessingService.list(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlessingCreateResult create(@RequestAttribute(VisitorInterceptor.ATTR_TOKEN) String token,
                                       @RequestBody BlessingRequest request,
                                       HttpServletRequest httpRequest) {
        return blessingService.create(token, request, httpRequest.getRemoteAddr());
    }
}
