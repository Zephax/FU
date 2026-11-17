package com.fzwywm.blessing;

import java.util.List;

public record BlessingDto(Long id, String username, String content, String createTime) {

    public static BlessingDto from(Blessing blessing) {
        String date = blessing.getCreatedAt() == null
                ? ""
                : blessing.getCreatedAt().toLocalDate().toString();
        return new BlessingDto(blessing.getId(), blessing.getNickname(), blessing.getContent(), date);
    }

    public static List<BlessingDto> from(List<Blessing> list) {
        return list.stream().map(BlessingDto::from).toList();
    }
}
