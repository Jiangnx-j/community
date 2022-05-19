package com.jiangnx.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串，用于user salt和user激活码等
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //将字符串加密
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    public static String getJSONString(Integer code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
       if (code != null){
           json.put("code",code);
       }
       if (msg!=null){
           json.put("msg",msg);
       }
       if (map!=null){
        for (String key:map.keySet()){
            json.put(key,map.get(key));
        }
       }
       return json.toJSONString();
    }
}
