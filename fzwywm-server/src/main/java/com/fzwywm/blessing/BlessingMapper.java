package com.fzwywm.blessing;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BlessingMapper {

    @Select("SELECT id, nickname, content, status, visitor_token, ip, created_at "
            + "FROM blessing WHERE status = 1 ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Blessing> listVisible(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM blessing WHERE status = 1")
    long countVisible();

    @Select("SELECT COUNT(*) FROM blessing WHERE visitor_token = #{token} AND created_at >= #{since}")
    int countByVisitorSince(@Param("token") String token, @Param("since") LocalDateTime since);

    @Select("SELECT created_at FROM blessing WHERE visitor_token = #{token} ORDER BY id DESC LIMIT 1")
    LocalDateTime lastCreatedAt(@Param("token") String token);

    @Insert("INSERT INTO blessing(nickname, content, status, visitor_token, ip, created_at) "
            + "VALUES(#{nickname}, #{content}, #{status}, #{visitorToken}, #{ip}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Blessing blessing);
}
