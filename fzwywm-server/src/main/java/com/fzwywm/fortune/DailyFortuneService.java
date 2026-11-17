package com.fzwywm.fortune;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DailyFortuneService {

    private static final String[] FORTUNES = {
            "福气满满，学业顺遂，万事皆可期。",
            "平安喜乐，前程似锦，岁岁常欢愉。",
            "春风得意，少年有志，步步皆荣光。",
            "福满身心，万事和顺，所行皆坦途。",
            "时来运转，喜乐常伴，多喜乐长安宁。",
            "闽都纳福，校园逐梦，理想皆可达。",
            "福如东海，寿比南山，吉祥常伴左右。",
            "校园勤学，榕城纳福，梦想终会开花。"
    };

    private final DailyFortuneMapper dailyFortuneMapper;

    public DailyFortuneService(DailyFortuneMapper dailyFortuneMapper) {
        this.dailyFortuneMapper = dailyFortuneMapper;
    }

    @Transactional
    public DailyFortune getOrCreate(LocalDate day) {
        DailyFortune existing = dailyFortuneMapper.find(day);
        if (existing != null) {
            return existing;
        }
        // 前端 js/home.js 的 dailyIndex() 使用同一套取模规则（epochDay % 条数），
        // 两边内容与顺序必须保持一致，否则降级展示的签文会和接口返回的不一样。
        int index = (int) Math.floorMod(day.toEpochDay(), FORTUNES.length);
        dailyFortuneMapper.insertIgnore(day, FORTUNES[index]);
        return dailyFortuneMapper.find(day);
    }
}
