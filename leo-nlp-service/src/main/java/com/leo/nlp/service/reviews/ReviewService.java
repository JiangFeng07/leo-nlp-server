package com.leo.nlp.service.reviews;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by lionel on 17/10/30.
 */
@Slf4j
public class ReviewService {
    private static TransportClient client = null;
    private static final String CLUSTER_NAME = "leo";
    private static final String IP = "172.24.38.88";
    private static final int PORT = 9300;


    static {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", CLUSTER_NAME)
//                    .put("client.transport.sniff", true)
//                    .put("index.refresh_interval", "2s")
                    .build();
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    private static void loadFile(String path) {
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = ReviewService.class.getResourceAsStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            int i = 1;
            while ((line = br.readLine()) != null) {
                String[] fields = line.trim().split("\\t");
                if (fields.length != 7) {
                    continue;
                }
                client.prepareIndex("reviews", "review", String.format("%d", i))
                        .setSource(jsonBuilder().
                                startObject()
                                .field("shop_id", NumberUtils.toInt(fields[0]))
                                .field("tag_id", NumberUtils.toInt(fields[1]))
                                .field("tag", fields[2])
                                .field("sentiment", fields[3])
                                .field("type", NumberUtils.toInt(fields[4]))
                                .field("review_id", NumberUtils.toInt(fields[5]))
                                .field("subsen", fields[6]).endObject())
                        .get();
                i += 1;
            }
            client.close();

        } catch (IOException e) {
            log.error("IO error", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }

    public void IndexDocument(String index, String type, String id) {
        try {
            IndexResponse response = client.prepareIndex(index, type, id).
                    setSource(jsonBuilder().
                                    startObject().field("").endObject()
                    ).get();
        } catch (IOException e) {
            log.info("IO error", e);
        }
    }

    public void DeleteDocument(String index, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).get();
    }

    public long DeleteDocumentByCondition(String index, String key, String value) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(QueryBuilders.matchQuery(key, value))
                .source(index).get();
        return response.getDeleted();
    }

    public void updateIndex(String index, String type, String id) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.type(type);
        updateRequest.id(id);
        try {
            updateRequest.doc(jsonBuilder().startObject().field("", "").endObject());
            client.update(updateRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public List<String> countByShopIdAndTag(String index) {
        if (StringUtils.isBlank(index)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        AggregationBuilder shopBuilder = AggregationBuilders.terms("shopAgg").field(String.valueOf("shop_id"));
        AggregationBuilder tagBuilder = AggregationBuilders.terms("tagAgg").field("tag");
        shopBuilder.subAggregation(tagBuilder);
        SearchResponse sr = client.prepareSearch(index).addAggregation(shopBuilder).execute().actionGet();

        Terms terms = sr.getAggregations().get("shopAgg");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            Terms terms2 = bucket.getAggregations().get("tagAgg");
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                String ele = String.format("%s:%s:%d", bucket.getKey(), bucket2.getKey(), bucket2.getDocCount());
                list.add(ele);
            }
        }

        client.close();
        return list;
    }


    public List<String> getContentByTag(String tag, String index) {
        if (StringUtils.isBlank(tag) || StringUtils.isBlank(index)) {
            return null;
        }
        SearchResponse searchResponse = client.prepareSearch(index).setQuery(QueryBuilders.matchQuery("tag", tag)).setFrom(0).execute().actionGet();
        List<String> list = new ArrayList<String>();
        for (SearchHit hit : searchResponse.getHits()) {
            list.add(hit.getSourceAsString());
        }
        client.close();
        return list;
    }

    public static void main(String[] args) {
        ReviewService reviewService = new ReviewService();
        List<String> res = reviewService.countByShopIdAndTag("reviews");
//        List<String> res = reviewService.getContentByTag("回头客", "reviews");
        if (res == null) {
            return;
        }
        for (String ele : res) {
            System.out.println(ele);
        }
    }

}
