package com.leo.nlp.structure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by lionel on 17/10/25.
 */
@Slf4j
public class TestElasticSearch {

    private static TransportClient client = null;


    static {
        try {
            Settings settings = Settings.builder().put("cluster.name", "leo").build();
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.24.38.88"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static IndexResponse addIndex(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return client.prepareIndex("twitter", "tweet").setSource(map).get();
    }

    @Test
    public void test() {
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = TestElasticSearch.class.getResourceAsStream("/review_tag.csv");
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            int i = 1;
            while ((line = br.readLine()) != null) {
                String[] fileds = line.trim().split("\\t");
                if (fileds.length != 7) {
                    continue;
                }
                client.prepareIndex("reviews", "review", "0" + i)
                        .setSource(jsonBuilder().
                                startObject()
                                .field("shop_id", NumberUtils.toInt(fileds[0]))
                                .field("tag_id", NumberUtils.toInt(fileds[1]))
                                .field("tag", fileds[2])
                                .field("sentiment", fileds[3])
                                .field("type", NumberUtils.toInt(fileds[4]))
                                .field("review_id", NumberUtils.toInt(fileds[5]))
                                .field("subsen", fileds[6]).endObject())
                        .get();
                i += 1;
            }

        } catch (IOException e) {
            log.error("IO error", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }

    @Test
    public void test2() throws IOException {
        GetResponse getResponse = client.prepareGet("books", "book", "1").execute().actionGet();
        System.out.println(getResponse.getSourceAsString());
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("books");
        updateRequest.type("book");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder().startObject().field("author", "李四").endObject());
        try {
            client.update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        GetResponse getResponse1 = client.prepareGet("books", "book", "1").execute().actionGet();
        System.out.println(getResponse1.getSourceAsString());

        client.close();
    }

    @Test
    public void test3() {
        SearchResponse response = client.prepareSearch("reviews")
                .addAggregation(AggregationBuilders.min("min").field("shop_id"))
                .get();
        InternalMin min = response.getAggregations().get("min");
        System.out.println(min.getValue());
        client.close();
    }

    @Test
    public void test4() {
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("shop").field("shop_id")
                .subAggregation(AggregationBuilders.dateHistogram("by_year")
                        .field("dateOfBirth").dateHistogramInterval(DateHistogramInterval.YEAR)
                        .subAggregation(AggregationBuilders.count("count").field("tag_id")));
        SearchResponse sr = client.prepareSearch("reviews").addAggregation(aggregationBuilder).get();
        System.out.println(sr);
        client.close();

    }

    @Test
    public void test5() {
//        MatchPhraseQueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("tag", "回头客");
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("tag", "回头客");
        SearchResponse searchResponse = client.prepareSearch("reviews")
                .setTypes("review")
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(10000)
                .execute()
                .actionGet();
//        System.out.println(searchResponse.getHits().getTotalHits());
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        client.close();
    }

    @Test
    public void test6() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("shop_id", "3511525")).must(QueryBuilders.termQuery("tag_id", "690"));
        SearchResponse response = client.prepareSearch("reviews")
                .setTypes("review")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery("早餐"))
//                .setQuery(QueryBuilders.termQuery("tag_id", "690"))
                .setQuery(queryBuilder)
//                .addAggregation(AggregationBuilders.terms("tags").field("taglist"))
                .setFrom(0)
                .setSize(1000)
                .setExplain(true)
                .execute().actionGet();
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        client.close();
    }

    @Test
    public void test7() {
        TermsAggregationBuilder builder = AggregationBuilders.terms("shopAgg").field("shop_id")
                .subAggregation(AggregationBuilders.count("tagAgg").field("tag_id"));
        SearchResponse sr = client.prepareSearch("reviews").setTypes("review").addAggregation(builder).execute().actionGet();
        Terms terms = sr.getAggregations().get("shopAgg");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            System.out.println(bucket.getAggregations().get("tagAgg"));
        }
    }

    @Test
    public void test8() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("reviews").setTypes("review")
                .setQuery(null);
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("count").field("tag_id"));
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        System.out.println(searchResponse);
    }

    @Test
    public void test9() {
        TermsAggregationBuilder shopBuilder = AggregationBuilders.terms("shopAgg").field("shop_id");
        TermsAggregationBuilder tagBuilder = AggregationBuilders.terms("tagAgg").field("tag");
        shopBuilder.subAggregation(tagBuilder);
        SearchResponse sr = client.prepareSearch("reviews").addAggregation(shopBuilder).execute().actionGet();
        Terms terms = sr.getAggregations().get("shopAgg");

        for (Terms.Bucket bucket : terms.getBuckets()) {
            Terms terms2 = bucket.getAggregations().get("tagAgg");
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                System.out.println(bucket.getKey() + ":" + bucket2.getKey() + ":" + bucket2.getDocCount());
            }
        }
    }
}
