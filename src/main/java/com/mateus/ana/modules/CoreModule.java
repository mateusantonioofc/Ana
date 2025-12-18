package com.mateus.ana.modules;

public interface CoreModule<TypeReturn> {

    boolean canHandle(String input);
    TypeReturn execute(String input);

}
