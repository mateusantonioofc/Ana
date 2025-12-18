package com.mateus.ana.core;

import com.mateus.ana.modules.CoreModule;
import com.mateus.ana.modules.ModuleHandle;

import java.util.List;

public class Core {

    private final List<CoreModule<?>> modules;

    public Core() {
        this.modules = ModuleHandle.getModules();
    }

    public Object handle(String input) {
        for (CoreModule<?> module : modules) {
            if (module.canHandle(input)) {
                try {
                    return module.execute(input);
                } catch (Exception e) {
                    return e;
                }
            }
        }

        return "Comando não encontrado";
    }


}
