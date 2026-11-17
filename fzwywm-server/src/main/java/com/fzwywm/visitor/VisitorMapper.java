package com.fzwywm.visitor;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

@Mapper
public interface VisitorMapper {

    @Select("SELECT token, fortune, fortune_date, created_at, last_seen_at FROM visitor WHERE token = #{token}")
    Visitor find(@Param("token") String token);

    @Select("SELECT token, fortune, fortune_date, created_at, last_seen_at FROM visitor WHERE token = #{token} FOR UPDATE")
    Visitor findForUpdate(@Param("token") String token);

    @Insert("INSERT IGNORE INTO visitor(token, fortune, fortune_date, created_at, last_seen_at) "
            + "VALUES(#{token}, 0, #{day}, NOW(), NOW())")
    int insert(@Param("token") String token, @Param("day") LocalDate day);

    @Update("UPDATE visitor SET fortune = 0, fortune_date = #{day}, last_seen_at = NOW() WHERE token = #{token}")
    int resetFortune(@Param("token") String token, @Param("day") LocalDate day);

    @Update("UPDATE visitor SET last_seen_at = NOW() WHERE token = #{token}")
    int touch(@Param("token") String token);

    @Update("UPDATE visitor SET fortune = LEAST(100, fortune + #{points}), last_seen_at = NOW() WHERE token = #{token}")
    int addFortune(@Param("token") String token, @Param("points") int points);

    @Select("SELECT COUNT(*) FROM visitor")
    long countAll();
}
