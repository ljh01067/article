package org.koreait.util;

import java.util.HashMap;

public class Member {
    private static HashMap<String,String> log = new HashMap<>();
    public static void islogined(String loginId, String loginPw){
        log.put(loginId,loginPw);
    }
    public static boolean islog(){
        if(log.isEmpty()){
            return false;
        }
        return true;
    }
    public static void islogouted(){
        log.clear();
    }
}
