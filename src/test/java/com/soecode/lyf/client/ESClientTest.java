package com.soecode.lyf.client;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.BaseTest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/4/18 0018
 **/
public class ESClientTest extends BaseTest {
    private static final List<String> NAMELIST = Arrays.asList("zs","ls","ww","zl","qq","wb");
    private final String INDEX = "user";
    private final String TYPE = "user_info";

    @Autowired
    private Client client;

    @Test
    public void test() throws IOException {
        IndicesExistsRequest request = new IndicesExistsRequest("index");
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();

        if(!response.isExists()){
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("index");
            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 5)
                    .put("index.number_of_replicas", 1)
                    .put("client.transport.sniff",true)
            );
            client.admin().indices().create(createIndexRequest);
        }
        for(int i=0;i < 100;i++){
            IndexResponse indexResponse = client.prepareIndex("index", "type", i + 1 + "")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("name", NAMELIST.get((int)(Math.random() * NAMELIST.size())))
                            .field("age", (int)(Math.random() * 60))
                            .field("gender",((int)(Math.random() * 2)) == 0 ? "male" : "female" )
                            .endObject()
                    )
                    .get();

            System.out.println(JSON.toJSONString(indexResponse));
        }
    }

    @Test
    public void test4() throws IOException {
        System.out.println(JSON.toJSONString(
                client.prepareIndex(INDEX,TYPE,"2").setSource(jsonBuilder()
                        .startObject()
                        .field("name","xzx")
                        .field("age",26)
                        .field("gender","male")
                        .endObject()
                ).get()
        ));
    }

    @Test
    public void test44() throws IOException {
        System.out.println(JSON.toJSONString(
                client.prepareIndex(INDEX,TYPE,"108").setSource(jsonBuilder()
                        .startObject()
                        .field("name","xzx")
                        .field("test1","male")
                        .endObject()
                ).get()
        ));
    }

    @Test
    public void test1(){
        for(int i=0;i < 100;i++){
            DeleteResponse response = client.prepareDelete(INDEX, TYPE, i + 1 + "")
                    .execute()
                    .actionGet();

            System.out.println(JSON.toJSONString(response));
        }
    }

    @Test
    public void test2(){
        System.out.println(JSON.toJSONString(client.admin().indices().delete(new DeleteIndexRequest(INDEX))));
    }

    @Test
    public void test6(){
        GetRequest getRequest = new GetRequest(INDEX,TYPE,"2");
        System.out.println(JSON.toJSONString(client.get(getRequest).actionGet()));
    }

    @Test
    public void test7(){
        BulkByScrollResponse response =
                new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE)
                        .filter(QueryBuilders.matchQuery("gender","male"))
                        .source(INDEX)
                        .get();
        System.out.println(response.getDeleted());
    }

    @Test
    public void test78(){
        SearchResponse searchResponse = client.prepareSearch("user")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                //.setQuery(QueryBuilders.matchQuery("gender","male"))
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(10).get();

        int count = 0;
        do{
            for(SearchHit hit:searchResponse.getHits().getHits()){
                String jsonStr = hit.getSourceAsString();
                System.out.println(jsonStr);
                count++;
            }
            System.out.println("--------" + count);

            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        }while(searchResponse.getHits().getHits().length != 0);

        System.out.println("-------" + count);
    }

    @Test
    public void test79(){
        //SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setQuery(QueryBuilders.wildcardQuery("name","*l*"));
        //SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setQuery(QueryBuilders.fuzzyQuery("name","l"));
        //SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setQuery(QueryBuilders.idsQuery("user_info").addIds("1","2"));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setQuery(QueryBuilders.wildcardQuery("name","*z*"));

        //SearchRequestBuilder searchRequestBuilder1 = client.prepareSearch("index").setQuery(QueryBuilders.matchQuery("name","zl"));

        MultiSearchResponse multiSearchResponse = client.prepareMultiSearch()
                .add(searchRequestBuilder)
                //.add(searchRequestBuilder1)
                .get();
        for(MultiSearchResponse.Item item:multiSearchResponse.getResponses()){
            for(SearchHit searchHit:item.getResponse().getHits().getHits()){
                System.out.println(searchHit.getSourceAsString());
            }
        }
    }

}