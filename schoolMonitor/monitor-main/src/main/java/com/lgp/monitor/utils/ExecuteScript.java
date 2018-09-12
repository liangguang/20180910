package com.lgp.monitor.utils;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ExecuteScript {
    public static void main(String[] args) {
        //创建一个脚本引擎管理器
        ScriptEngineManager manager = new ScriptEngineManager();
        //获取一个指定的名称的脚本引擎
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            //获取当前类的所在目录的路径
            String path = ExecuteScript.class.getResource("").getPath();
            System.out.println(path);
            // FileReader的参数为所要执行的js文件的路径
            engine.eval(new FileReader(path + "dysignate.js"));
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //执行指定的js方法
                System.out.println(invocable.invokeFunction("docWrite", ""));//
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}