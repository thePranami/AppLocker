package com.webrication.smartapplocker.other;



import com.orhanobut.hawk.Hawk;


public class PrefrancesUtils
{
    public static void setPattern(String pattern){
        Hawk.put("pattern",pattern);
    }

    public static String getPattern(){
        return  Hawk.get("pattern",null);
    }

    public static void setPatternfirst(String pattern){
        Hawk.put("confpattern",pattern);
    }

    public static String getPatternfirst(){
        return  Hawk.get("confpattern",null);

    }
    public static void setNickName(String nick){
        Hawk.put("NickName",nick);
    }

    public static String getNickName() {
        return Hawk.get("NickName", null);
    }

    public static void setPIN(String pin){
        Hawk.put("pin",pin);
    }

    public static String getPIN(){
        return  Hawk.get("pin",null);
    }

    public static void setLockType(String type){
        Hawk.put("type",type);
    }

    public static String getLockType(){
        return  Hawk.get("type",null);

    }
    public static void setgesture(String gesture){
        Hawk.put("gesture",gesture);
    }

    public static String getgesture(){
        return  Hawk.get("gesture",null);

    }

    public static void setQuestin(String question){
        Hawk.put("question",question);
    }




    public static String getQuestion(){
        return  Hawk.get("question",null);

    }


    public static void setAnswer(String answer){
        Hawk.put("answer",answer);
    }

    public static String getAnswer(){
        return  Hawk.get("answer",null);

    }



}
