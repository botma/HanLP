/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/26 15:45</create-date>
 *
 * <copyright file="TestDAT.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.test.algorithm;

import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.BiGramDictionary;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author hankcs
 */
public class TestDAT extends TestCase
{
    public void testSaveWithLessDisk() throws Exception
    {
        // 希望在保存的时候尽量少用点硬盘
        System.out.println(BiGramDictionary.getBiFrequency("经济@建设"));
    }

    public void testTransmit() throws Exception
    {
        DoubleArrayTrie<CoreDictionary.Attribute> dat = CustomDictionary.dat;
        int index = dat.transition("龙", 1);
        System.out.println(dat.output(index));
        index = dat.transition("窝", index);
        System.out.println(dat.output(index));
    }

    public void testCombine() throws Exception
    {
        DoubleArrayTrie<CoreDictionary.Attribute> dat = CustomDictionary.dat;
        String[] wordNet = new String[]
                {
                        "他",
                        "一",
                        "丁",
                        "不",
                        "识",
                        "一",
                        "丁",
                        "呀",
                };
        for (int i = 0; i < wordNet.length; ++i)
        {
            int state = 1;
            state = dat.transition(wordNet[i], state);
            if (state > 0)
            {
                int start = i;
                int to = i + 1;
                int end = - 1;
                CoreDictionary.Attribute value = null;
                for (; to < wordNet.length; ++to)
                {
                    state = dat.transition(wordNet[to], state);
                    if (state < 0) break;
                    CoreDictionary.Attribute output = dat.output(state);
                    if (output != null)
                    {
                        value = output;
                        end = to + 1;
                    }
                }
                if (value != null)
                {
                    StringBuilder sbTerm = new StringBuilder();
                    for (int j = start; j < end; ++j)
                    {
                        sbTerm.append(wordNet[j]);
                    }
                    System.out.println(sbTerm.toString() + "/" + value);
                    i = end - 1;
                }
            }
        }
    }

    public void testHandleEmptyString() throws Exception
    {
        String emptyString = "";
        DoubleArrayTrie<String> dat = new DoubleArrayTrie<String>();
        TreeMap<String, String> dictionary = new TreeMap<String, String>();
        dictionary.put("bug", "问题");
        dat.build(dictionary);
        DoubleArrayTrie<String>.Searcher searcher = dat.getSearcher(emptyString, 0);
        while (searcher.next())
        {
        }
    }


    public void testDat() throws IOException {
        DoubleArrayTrie<CoreDictionary.Attribute> trie = new DoubleArrayTrie<CoreDictionary.Attribute>();

        TreeMap<String, CoreDictionary.Attribute> map = new TreeMap<String, CoreDictionary.Attribute>();
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(TestDAT.class.getResourceAsStream("/testdict.properties"), "UTF-8"));
        String line;
        int MAX_FREQUENCY = 0;
        long start = System.currentTimeMillis();
        while ((line = br.readLine()) != null) {
            String param[] = line.split("\\s");
            int natureCount = (param.length - 1) / 2;
            CoreDictionary.Attribute attribute = new CoreDictionary.Attribute(natureCount);
            for (int i = 0; i < natureCount; ++i) {
                attribute.nature[i] = Enum.valueOf(Nature.class, param[1 + 2 * i]);
                attribute.frequency[i] = Integer.parseInt(param[2 + 2 * i]);
                attribute.totalFrequency += attribute.frequency[i];
            }
            map.put(param[0], attribute);
            MAX_FREQUENCY += attribute.totalFrequency;
        }
        logger.info(
            "核心词典读入词条" + map.size() + " 全部频次" + MAX_FREQUENCY + "，耗时" + (System.currentTimeMillis()
                - start) + "ms");
        br.close();
        trie.build(map);

        DoubleArrayTrie.Searcher searcher = trie.getSearcher("2001",0);
        while(searcher.next()){
            System.out.println(searcher.begin+":"+searcher.length+":"+ searcher.index+":"+ searcher.value);
        }
    }

    public void testChar(){
        for(int i = 0; i< 128; i++)
            System.out.println(i+"\t"+((char)i));
    }
}
