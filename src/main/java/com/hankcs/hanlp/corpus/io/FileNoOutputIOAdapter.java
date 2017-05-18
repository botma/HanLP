package com.hankcs.hanlp.corpus.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by linjiang on 2017/5/18.
 */
public class FileNoOutputIOAdapter implements IIOAdapter{
    @Override
    public InputStream open(String path) throws FileNotFoundException
    {
        return new FileInputStream(path);
    }

    @Override
    public OutputStream create(String path) throws FileNotFoundException
    {
        return new OutputStream() {public void write(int b) throws IOException {}};
    }
}
