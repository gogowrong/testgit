package com.baizhi.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document document=new Document();
        document.add(new StringField("id","3", Field.Store.YES));
        document.add(new StringField("title","逍遥游", Field.Store.YES));
        document.add(new StringField("author","庄子", Field.Store.YES));
        document.add(new StringField("content","北冥有鱼，其名为鲲", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();

    }
    @Test
    public void TestIndexSearcher() throws IOException {
         Directory dir = FSDirectory.open(new File("D:/index"));
        IndexReader indexReader = DirectoryReader.open(dir);
      IndexSearcher indexSearcher=new IndexSearcher(indexReader);


        Query query=new TermQuery(new Term("id","1"));
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
    @Test
    public void TestDelete() throws IOException {
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_44,analyzer);
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("D:/index")),conf);

        indexWriter.deleteDocuments(new Term("author","庄子"));
        indexWriter.commit();

    }


    @Test
    public void update() throws IOException {
        //    先删除  后创建的过程
        //    不存在  则创建
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_44);
        IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_44,analyzer);
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("D:/index")),conf);

        Document document = new Document();
        document.add(new StringField("id", "1", Field.Store.YES));
        document.add(new StringField("title", "滕王阁序", Field.Store.YES));
        document.add(new StringField("author", "王勃", Field.Store.YES));
        document.add(new TextField("content", "落霞与孤鹜齐飞，秋水共长天一色", Field.Store.NO));
        document.add(new StringField("date", "1011-09-26", Field.Store.YES));
        indexWriter.updateDocument(new Term("id","5"),document );
        indexWriter.commit();
    }
    @Test
    public void Test(){
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        System.out.println(indexWriter);
    }
}
