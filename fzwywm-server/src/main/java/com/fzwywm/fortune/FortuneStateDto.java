package com.fzwywm.fortune;

import java.util.List;

public record FortuneStateDto(int fortune,
                              int max,
                              List<String> lamps,
                              List<String> cards,
                              boolean drawn,
                              boolean blessed,
                              DailyFortuneDto daily) {
}
