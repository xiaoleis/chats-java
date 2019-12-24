package com.im.chats.common.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class SqlDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryForList(String sql ){
        return jdbcTemplate.queryForList(sql);
    }

    public Map queryForMap(String sql){
        return jdbcTemplate.queryForMap(sql);
    }


    public List<Map<String, Object>> queryForList(String sql,@Nullable Object[] args){
        return jdbcTemplate.queryForList(sql,args);
    }

    public Map queryForMap(String sql,@Nullable Object[] args){
        return jdbcTemplate.queryForMap(sql,args);
    }

    public void update(String sql,@Nullable Object[] args){
         jdbcTemplate.update(sql,args);
    }

    public void update(String sql){
        jdbcTemplate.update(sql);
    }


}
