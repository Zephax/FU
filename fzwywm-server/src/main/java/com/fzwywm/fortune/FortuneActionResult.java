package com.fzwywm.fortune;

public record FortuneActionResult(int fortune,
                                  int max,
                                  int awarded,
                                  boolean duplicate,
                                  String text) {
}
