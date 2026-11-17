package com.fzwywm.blessing;

import com.fzwywm.common.ApiException;
import com.fzwywm.fortune.FortuneActionResult;
import com.fzwywm.fortune.FortuneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class BlessingService {

    private static final int MAX_NICKNAME = 20;
    private static final int MAX_CONTENT = 80;
    private static final int DAILY_LIMIT = 20;
    private static final int MIN_SECONDS_BETWEEN_POSTS = 5;
    private static final int MAX_PAGE = 10000;

    private static final Set<String> BLOCKED_WORDS = Set.of(
            "赌博", "博彩", "色情", "毒品", "枪支", "代写", "刷单",
            "贷款", "加微信", "加qq", "http://", "https://", "www."
    );

    private final BlessingMapper blessingMapper;
    private final FortuneService fortuneService;

    public BlessingService(BlessingMapper blessingMapper, FortuneService fortuneService) {
        this.blessingMapper = blessingMapper;
        this.fortuneService = fortuneService;
    }

    public List<BlessingDto> list(int page, int size) {
        int safeSize = Math.max(1, Math.min(50, size));
        int safePage = Math.max(1, Math.min(MAX_PAGE, page));
        return BlessingDto.from(blessingMapper.listVisible(safeSize, (safePage - 1) * safeSize));
    }

    @Transactional
    public BlessingCreateResult create(String token, BlessingRequest request, String ip) {
        if (request == null) {
            throw ApiException.badRequest("昵称和祝福内容都要写上哦");
        }

        String nickname = normalize(request.nickname());
        String content = normalize(request.content());
        LocalDate today = LocalDate.now();

        if (nickname.isEmpty() || content.isEmpty()) {
            throw ApiException.badRequest("昵称和祝福内容都要写上哦");
        }
        if (nickname.length() > MAX_NICKNAME) {
            throw ApiException.badRequest("昵称不能超过 " + MAX_NICKNAME + " 个字");
        }
        if (content.length() > MAX_CONTENT) {
            throw ApiException.badRequest("祝福不能超过 " + MAX_CONTENT + " 个字");
        }
        if (containsBlockedWord(nickname + " " + content)) {
            throw ApiException.badRequest("内容里有不适合展示的词汇，换一句试试吧");
        }
        if (blessingMapper.countByVisitorSince(token, today.atStartOfDay()) >= DAILY_LIMIT) {
            throw ApiException.tooManyRequests("今天已经贴了很多祝福，明天再来吧");
        }

        LocalDateTime lastCreatedAt = blessingMapper.lastCreatedAt(token);
        if (lastCreatedAt != null
                && lastCreatedAt.isAfter(LocalDateTime.now().minusSeconds(MIN_SECONDS_BETWEEN_POSTS))) {
            throw ApiException.tooManyRequests("发得太快了，歇 " + MIN_SECONDS_BETWEEN_POSTS + " 秒再贴");
        }

        Blessing blessing = new Blessing();
        blessing.setNickname(nickname);
        blessing.setContent(content);
        blessing.setStatus(1);
        blessing.setVisitorToken(token);
        blessing.setIp(ip);
        blessing.setCreatedAt(LocalDateTime.now());
        blessingMapper.insert(blessing);
        FortuneActionResult award = fortuneService.awardBlessing(token, today);

        return new BlessingCreateResult(
                BlessingDto.from(blessing),
                award.fortune(),
                award.max(),
                award.awarded(),
                award.duplicate()
        );
    }

    private boolean containsBlockedWord(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        for (String word : BLOCKED_WORDS) {
            if (lower.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replaceAll("\\p{Cntrl}", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
