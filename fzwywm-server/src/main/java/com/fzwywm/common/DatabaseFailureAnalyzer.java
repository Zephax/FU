package com.fzwywm.common;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

public class DatabaseFailureAnalyzer extends AbstractFailureAnalyzer<CannotGetJdbcConnectionException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, CannotGetJdbcConnectionException cause) {
        return new FailureAnalysis(
                "无法连接 MySQL 数据库，后端没有启动。",
                "请先双击 fzwywm-server\\init-db.bat 并输入 MySQL root 密码，"
                        + "并确认 MySQL80 服务正在运行。默认连接：127.0.0.1:3306/fzwywm，账号 fzwywm。",
                cause
        );
    }
}
