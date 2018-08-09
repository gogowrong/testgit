package com.baizhi.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Administrator on 2018/8/9.
 */
public class LearnLucene {
    @Test
    public void TestIndexWriter() throws IOException {
        Directory  dir = FSDirectory.open(new File("D:/index"));
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_44, analyzer);
        IndexWriter indexWriter=new IndexWriter(dir,conf);

        Document document=new Document();
        document.add(new StringField("id","1", Field.Store.YES));
        document.add(new StringField("title","逍遥游", Field.Store.YES));
        document.add(new StringField("author","庄子", Field.Store.YES));
        document.add(new StringField("content","北冥有鱼，其名为鲲", Field.Store.NO));
        indexWriter.addDocument(document);
        indexWriter.close();

    }
    @Test
    public void TestIndexSearcher() throws IOException {
        Directory dir = FSDirectory.open(new File("D:/index"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);

        Query query=new TermQuery(new Term("author","庄子"));
        TopDocs search = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            int doc = scoreDoc.doc;
            float score = scoreDoc.score;
            Document doc1 = indexSearcher.doc(doc);
            System.out.println("=====================================");
            System.out.println(score);
            System.out.println("这是文章id:"+doc1.get("id"));
            System.out.println("这是文章title   "+doc1.get("title"));
            System.out.println("这是文章author   "+doc1.get("author"));
            System.out.println("这是文章content   "+doc1.get("content"));
            System.out.println("=====================================");

        }



    }
}
