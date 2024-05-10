package com.zxb.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public Result(T data) {
        this.data = data;
    }

    public static Result success() {
        Result result = new Result<>();
        result.setCode(Constants.CODE_200);
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode(Constants.CODE_200);
        result.setMsg("成功");
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

//    public static Result error() {
//        return new Result(Constants.CODE_500,"系统错误",null);
//    }



    public static String okGetString(String message){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message",message);
        String s = JSONObject.toJSONString(map);
        return s;
    }

    public static String okGetStringByData(String message,Object data){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message",message);
        map.put("data",data);
        String s = JSONObject.toJSONString(map);
        return s;
    }
    public static String okGetStringByDataT(String message,Object data,int total){
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message",message);
        map.put("data",data);
        map.put("total",total);
        String s = JSONObject.toJSONString(map);
        return s;
    }
}
