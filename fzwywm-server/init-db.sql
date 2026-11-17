CREATE DATABASE IF NOT EXISTS fzwywm
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'fzwywm'@'localhost' IDENTIFIED BY 'Fzwywm@2026';
CREATE USER IF NOT EXISTS 'fzwywm'@'127.0.0.1' IDENTIFIED BY 'Fzwywm@2026';
ALTER USER 'fzwywm'@'localhost' IDENTIFIED BY 'Fzwywm@2026';
ALTER USER 'fzwywm'@'127.0.0.1' IDENTIFIED BY 'Fzwywm@2026';
GRANT ALL PRIVILEGES ON fzwywm.* TO 'fzwywm'@'localhost';
GRANT ALL PRIVILEGES ON fzwywm.* TO 'fzwywm'@'127.0.0.1';
FLUSH PRIVILEGES;

SELECT 'fzwywm database and user are ready' AS result;
