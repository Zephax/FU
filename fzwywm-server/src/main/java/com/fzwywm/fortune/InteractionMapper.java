package com.fzwywm.fortune;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InteractionMapper {

    @Insert("INSERT IGNORE INTO interaction_log(visitor_token, `action`, `ref`, points, action_date, created_at) "
            + "VALUES(#{token}, #{action}, #{ref}, #{points}, #{day}, NOW())")
    int insertIgnore(@Param("token") String token,
                     @Param("action") String action,
                     @Param("ref") String ref,
                     @Param("points") int points,
                     @Param("day") LocalDate day);

    @Select("SELECT `ref` FROM interaction_log "
            + "WHERE visitor_token = #{token} AND `action` = #{action} AND action_date = #{day} ORDER BY id")
    List<String> refs(@Param("token") String token,
                      @Param("action") String action,
                      @Param("day") LocalDate day);

    @Select("SELECT COUNT(*) FROM interaction_log "
            + "WHERE visitor_token = #{token} AND `action` = #{action} AND `ref` = #{ref} AND action_date = #{day}")
    int exists(@Param("token") String token,
               @Param("action") String action,
               @Param("ref") String ref,
               @Param("day") LocalDate day);

    @Select("SELECT COUNT(*) FROM interaction_log WHERE `action` = #{action}")
    long countByAction(@Param("action") String action);
}
