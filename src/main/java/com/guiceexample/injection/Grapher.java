package com.guiceexample.injection;

/**
 * Created by nernst on 6/16/17.
 */
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.grapher.graphviz.GraphvizGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Grapher {

    public Grapher() {
       try {
           graph("filename.dot", Guice.createInjector(new MyModule()));
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private void graph(String filename, Injector demoInjector) throws IOException {
        PrintWriter out = new PrintWriter(new File(filename), "UTF-8");

        Injector injector = Guice.createInjector(new GraphvizModule());
        GraphvizGrapher grapher = injector.getInstance(GraphvizGrapher.class);
        grapher.setOut(out);
        grapher.setRankdir("TB");
        grapher.graph(demoInjector);
    }
}