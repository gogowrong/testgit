package com.baizhi.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/8/9.
 */
public class LuceneUtil {
    public static Directory dir;
    private static Analyzer analyzer;
    private static IndexWriterConfig conf;
    private static  IndexWriter indexWriter;

    static {
        try {
             dir = FSDirectory.open(new File("D:/index"));
            analyzer=new StandardAnalyzer(Version.LUCENE_44);
            conf=new IndexWriterConfig(Version.LUCENE_44, analyzer);
            indexWriter = new IndexWriter(dir,conf);
            //indexReader = DirectoryReader.open(dir);IndexNotFoundException 没有找到segments文件
            //indexSearcher=new IndexSearcher(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static IndexWriter getIndexWriter()  {
        return indexWriter;
    }

    public static IndexSearcher getIndexSearcher() {
        IndexReader indexReader= null;
        try {
            indexReader = DirectoryReader.open(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        return indexSearcher;
    }

    public static void commit(IndexWriter indexWriter)  {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void rollback(IndexWriter indexWriter){
        try {
            indexWriter.rollback();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document getDocument(Product product){
        Document document=new Document();
        document.add(new StringField("id",product.getId(), Field.Store.YES));
        document.add(new StringField("title",product.getAuthor(), Field.Store.YES));
        document.add(new StringField("author",product.getAuthor(), Field.Store.YES));
        document.add(new StringField("content",product.getContent(), Field.Store.YES));
        return document;
    }

    public static Product getProduct(Document document){
        String id = document.get("id");
        String author = document.get("author");
        String title = document.get("title");
        String content = document.get("content");
        Product product=new Product(id,title,author,content);
        return product;
    }


}
