package com.baizhi.lucene;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */
public interface LuceneDao {
    public void saveIndex(Product product);

    public void deleteIndex(String id);

    public void updateIndex(Product product);

    public List<Product> queryIndex(String value);
}
