package com.mateus.ana.modules;

import com.mateus.ana.modules.system.OpenAplicationModule;
import com.mateus.ana.modules.web.WebSearchModule;

import java.util.List;

public class ModuleHandle {

    public ModuleHandle() {}

    public static List<CoreModule<?>> getModules() {
        return  List.of(
                new WebSearchModule(),
                new OpenAplicationModule()
        );
    }
}
