package com.fzwywm.blessing;

public record BlessingCreateResult(BlessingDto blessing,
                                   int fortune,
                                   int max,
                                   int awarded,
                                   boolean duplicate) {
}