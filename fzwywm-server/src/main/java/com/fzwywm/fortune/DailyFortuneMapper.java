package com.fzwywm.fortune;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface DailyFortuneMapper {

    @Select("SELECT `day`, content, created_at FROM daily_fortune WHERE `day` = #{day}")
    DailyFortune find(@Param("day") LocalDate day);

    @Insert("INSERT IGNORE INTO daily_fortune(`day`, content, created_at) VALUES(#{day}, #{content}, NOW())")
    int insertIgnore(@Param("day") LocalDate day, @Param("content") String content);
}
