package com.ozzydemo.cdc.source.wordpress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.ozzydemo.cdc.commons.data.domain.WordPressPost;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordPressProducer
{
    @Autowired
    KafkaTemplate<String, WordPressPost> kafkaTemplate;
    String TOPIC_NAME = "cdc-wp-posts";

    public void publishPost(WordPressPost post)
    {
        kafkaTemplate.send(TOPIC_NAME, post);
    }


    public void logWordpressPosts() //throws IOException
    {
        try
        {
            ObjectMapper jsonMapper = new ObjectMapper();
            //jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
            jsonMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, true);
            jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            final Map<String, Long> tableMap = new HashMap<String, Long>();
            String posts_tbl_name = "wp_posts";
            String posts_db_name = "wordpress5";

            EventDeserializer eventDeserializer = new EventDeserializer();
            eventDeserializer.setCompatibilityMode(
                    EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY

            );

            BinaryLogClient client =
                    new BinaryLogClient("localhost", 3306, posts_db_name, "appadmin", "#Admin234");

            client.setEventDeserializer(eventDeserializer);

            client.registerEventListener(event ->
            {
                EventData data = event.getData();

                if (data instanceof TableMapEventData)
                {
                    TableMapEventData tableData = (TableMapEventData) data;

                    System.out.println("table name->" + tableData.getTable());
                    System.out.println("table Id->" + tableData.getTableId());

                    tableMap.put(tableData.getTable(), tableData.getTableId());
                }
                else if (data instanceof WriteRowsEventData)
                {
                    WriteRowsEventData eventData = (WriteRowsEventData) data;

                    if (tableMap.containsKey(posts_tbl_name))
                    {

                        try
                        {

                            if (eventData.getTableId() == tableMap.get(posts_tbl_name))
                            {

                                for (Object[] newRow : eventData.getRows())
                                {

                                    WordPressPost pojo = mapPostData(newRow);
                                    publishPost(pojo);

                                    System.out.println(jsonMapper.writeValueAsString(pojo));

                                    System.out.println(jsonMapper.writeValueAsString(newRow));

                                }
                            }
                        }
                        catch (JsonProcessingException e)
                        {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }


                }
                else if (data instanceof UpdateRowsEventData)
                {
                    UpdateRowsEventData eventData = (UpdateRowsEventData) data;

                    if (eventData != null)
                    {

                        try
                        {
                            //System.out.println(jsonMapper.writeValueAsString(eventData));

                            if (tableMap.containsKey(posts_tbl_name))
                            {
                                if (eventData.getTableId() == tableMap.get(posts_tbl_name))
                                {
                                    for (Map.Entry<Serializable[], Serializable[]> row :
                                            eventData.getRows())
                                    {

                                        WordPressPost pojo = mapPostData(row.getValue());
                                        publishPost(pojo);

                                        System.out.println(jsonMapper.writeValueAsString(pojo));

                                        System.out.println(jsonMapper.writeValueAsString(row.getValue()));

                                    }
                                }


                            }


                        }
                        catch (JsonProcessingException e)
                        {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }


                    }

                }
                else if (data instanceof DeleteRowsEventData)
                {
                    DeleteRowsEventData eventData = (DeleteRowsEventData) data;
                    if (eventData.getTableId() == tableMap.get(posts_tbl_name))
                    {
                        for (Object[] oldRow : eventData.getRows())
                        {
                            try
                            {
                                //Object[] pojoRows = row.getValue();
                                WordPressPost pojo = mapPostData(oldRow);

                                publishPost(pojo);

                                System.out.println(jsonMapper.writeValueAsString(pojo));
                                System.out.println(jsonMapper.writeValueAsString(oldRow));
                            }
                            catch (JsonProcessingException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            client.connect();

        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    WordPressPost mapPostData(Object[] post) //throws UnsupportedEncodingException
    {

        WordPressPost pojo = null;

        try
        {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy");

            System.out.println("total props->" + post.length);
            System.out.println("post[0]->" + post[0]);
            System.out.println("post[4]-content>" + post[4]);
            System.out.println("post[4]-content>" + unicodeToUTF8(post[4].toString()));
            System.out.println("post[4]-content>" + StringEscapeUtils.unescapeJava(post[4].toString()));

            System.out.println("post[16]->" + post[16]);
            System.out.println("post[17]->" + post[17]);
            System.out.println("post[18]->" + post[18]);
            System.out.println("post[22]->" + post[22]);

            pojo = WordPressPost.builder()
                    .id(Long.valueOf(post[0].toString()))
                    .author(Long.valueOf(post[1].toString()))

//                .postDate(post[2] != null ? LocalDateTime.parse(post[2].toString(), formatter) : null)
//                .postDateGmt(post[3] != null ? LocalDateTime.parse(post[3].toString(), formatter) : null)

                    .postDate(post[2] != null ? post[2].toString() : null)
                    .postDateGmt(post[3] != null ? post[3].toString() : null)

                    .postContent(post[4] != null ? unicodeToUTF8(post[4].toString()) : null)
                    .postTitle(post[5] != null ? post[5].toString() : null)
                    .postExcerpt(post[6] != null ? post[6].toString() : null)
                    .postStatus(post[7] != null ? post[7].toString() : null)
                    .commentStatus(post[8] != null ? post[8].toString() : null)
                    .pingStatus(post[9] != null ? post[9].toString() : null)
                    .postPassword(post[10] != null ? post[10].toString() : null)
                    .postName(post[11] != null ? post[11].toString() : null)
                    .toPing(post[12] != null ? post[12].toString() : null)
                    .pinged(post[13] != null ? post[13].toString() : null)

//                .postModified(post[14] != null ? LocalDateTime.parse(post[14].toString(), formatter) : null)
//                .postModifiedGmt(post[15] != null ? LocalDateTime.parse(post[15].toString(), formatter) : null)
                    .postModified(post[14] != null ? post[14].toString() : null)
                    .postModifiedGmt(post[15] != null ? post[15].toString() : null)

                    //.postParent(post[16] != null ? Long.valueOf(post[16].toString()) : null)

                    .guid(post[17] != null ? post[17].toString() : null)
                    //.menuOrder(post[18] != null ? Integer.valueOf(post[18].toString()) : null)
                    .menuOrder(post[18] != null ? post[18].toString() : null)

                    .postType(post[19] != null ? post[19].toString() : null)
                    .postMimeType(post[20] != null ? post[20].toString() : null)
                    .commentStatus(post[21] != null ? post[21].toString() : null)
                    //.commentCount(post[22] != null ? Long.valueOf(post[22].toString()) : null)
                    .commentCount(post[22] != null ? post[22].toString() : null)

                    .build();

            //return pojo;

        }
        catch (Exception ex)
        {
            ex.printStackTrace();


        }

        return pojo;
    }


    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    String decodeUTF8(byte[] bytes)
    {
        return new String(bytes, UTF8_CHARSET);
    }

    byte[] encodeUTF8(String string)
    {
        return string.getBytes(UTF8_CHARSET);
    }

    String unicodeToUTF8(String original) throws UnsupportedEncodingException
    {
        byte[] utf8Bytes = original.getBytes("UTF-8");
        String roundTrip = new String(utf8Bytes, "UTF-8");
        return roundTrip;
    }

}
