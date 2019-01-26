package com.codeosseum.miles.communication.http.controller;

import com.google.gson.Gson;

public abstract class JsonHttpController {
    protected final Gson gson;

    public JsonHttpController(Gson gson) {
        this.gson = gson;
    }

    public abstract void attach();
}
