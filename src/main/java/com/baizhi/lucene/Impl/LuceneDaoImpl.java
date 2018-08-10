package com.baizhi.lucene.Impl;

import com.baizhi.lucene.LuceneDao;
import com.baizhi.lucene.LuceneUtil;
import com.baizhi.lucene.Product;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */
public class LuceneDaoImpl implements LuceneDao {
    @Override
    public void saveIndex(Product product) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document document = LuceneUtil.getDocument(product);
        try {
            indexWriter.addDocument(document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }

    }

    @Override
    public void deleteIndex(String id) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.deleteDocuments(new Term("id",id));
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }
    }

    @Override
    public void updateIndex(Product product) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document document = LuceneUtil.getDocument(product);
        try {
            indexWriter.updateDocument(new Term("id",product.getId()),document );
            indexWriter.commit();
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> queryIndex(String value) {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        Query query = new TermQuery(new Term("author", value));
        List<Product> list =new ArrayList<Product>();
        try {
            TopDocs search = indexSearcher.search(query, 10);
            ScoreDoc[] scoreDocs = search.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                float score = scoreDoc.score;//分数
                int doc = scoreDoc.doc;//页码
                Document doc1 = indexSearcher.doc(doc);
                Product product = LuceneUtil.getProduct(doc1);
                list.add(product);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }
}
