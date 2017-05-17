package com.hankcs.hanlp.corpus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by linjiang on 2017/5/17.
 */
public class JarIOAdapter implements IIOAdapter {

    public InputStream open(String path) throws IOException {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }


    public OutputStream create(String path) throws IOException {
        return new OutputStream() {public void write(int b) throws IOException {}};
    }
}
