package com.github.hiling.auth.service.impl;

import com.github.hiling.auth.mapper.RefreshTokenMapper;
import com.github.hiling.auth.model.RevokeToken;
import com.github.hiling.auth.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Author by hiling, Email admin@mn-soft.com, Date on 10/12/2018.
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Resource
    private RefreshTokenMapper refreshTokenMapper;

    @Override
    public List<String> getRevokeRefreshToken(List<RevokeToken> list){
        if (list.isEmpty()){
            return Arrays.asList();
        }
        return refreshTokenMapper.getRevokeRefreshToken(list);
    }

    @Override
   public void batchDeleteByRefreshToken(List<String> refreshTokenList){
        if (refreshTokenList.isEmpty()){
            return;
        }
        refreshTokenMapper.batchDeleteByRefreshToken(refreshTokenList);
    }

    @Override
    public void deleteExpiredRefreshToken(){
        refreshTokenMapper.deleteExpiredRefreshToken();
    }
}
