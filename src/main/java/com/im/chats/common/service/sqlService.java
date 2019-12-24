package com.im.chats.common.service;

import com.im.chats.common.dao.SqlDao;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class sqlService{

    @Resource
    private SqlDao sqlDao;

    public List<Map<String, Object>> queryForList(String sql ){
        return sqlDao.queryForList(sql);
    }

    public Map queryForMap(String sql){
        return sqlDao.queryForMap(sql);
    }


    public List<Map<String, Object>> queryForList(String sql,@Nullable Object[] args){
        return sqlDao.queryForList(sql,args);
    }

    public Map queryForMap(String sql,@Nullable Object[] args){
        return sqlDao.queryForMap(sql,args);
    }


    public void update(String sql,@Nullable Object[] args){
        sqlDao.update(sql,args);
    }

    public void update(String sql){
        sqlDao.update(sql);
    }


}
