CREATE TABLE IF NOT EXISTS blessing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(20) NOT NULL,
    content VARCHAR(80) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    visitor_token VARCHAR(64) NULL,
    ip VARCHAR(45) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_blessing_status_time (status, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS visitor (
    token VARCHAR(64) PRIMARY KEY,
    fortune INT NOT NULL DEFAULT 0,
    fortune_date DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS interaction_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    visitor_token VARCHAR(64) NOT NULL,
    `action` VARCHAR(16) NOT NULL,
    `ref` VARCHAR(16) NOT NULL DEFAULT '',
    points INT NOT NULL,
    action_date DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_visitor_action_ref_day (visitor_token, `action`, `ref`, action_date),
    KEY idx_interaction_action_date (`action`, action_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS daily_fortune (
    `day` DATE PRIMARY KEY,
    content VARCHAR(120) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
