package org.example.dao;


import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;

public class messages {
    private String content;
    private String role;

    public messages(String content, String role) {
        this.content = content;
        this.role = role;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.set("content", this.content);
        json.set("role", this.role);
        return json;
    }
}
