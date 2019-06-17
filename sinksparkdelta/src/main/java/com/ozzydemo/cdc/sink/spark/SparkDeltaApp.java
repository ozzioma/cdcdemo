package com.ozzydemo.cdc.sink.spark;

import com.ozzydemo.cdc.commons.data.domain.Customer;
import com.ozzydemo.cdc.commons.data.domain.WordPressPost;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.StringType;
import org.apache.spark.sql.types.StructType;

import java.util.UUID;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;

public class SparkDeltaApp
{

    public static void main(String[] args)
    {

        //testDelta();
        //readDelta1();
        //readPostsFromKafka();
        readPostsFromDelta();
    }


    static void readPostsFromKafka()
    {
        try
        {

            SparkSession spark = SparkHelper.getSession();

            Dataset<Row> wpDataset = spark.readStream().format("kafka")
                    .option("kafka.bootstrap.servers", "localhost:9092")
                    .option("dateFormat", "dd-MM-yyyy hh:mm:ss")
                    .option("timeStampFormat", "dd-MM-yyyy hh:mm:ss")
                    .option("subscribe", "cdc-wp-posts")
                    .option("startingOffsets", "earliest")
                    .load();

            wpDataset.printSchema();

            StructType schemaPosts = Encoders.bean(WordPressPost.class).schema();
            Dataset<Row> dfPosts = wpDataset.select(from_json(col("value").cast("string"), schemaPosts).as("posts"));
            Dataset<Row> dfFlat = dfPosts.selectExpr("posts.*");

            dfFlat.printSchema();

            StreamingQuery streamingQuery = dfFlat.writeStream()
                    .format("delta")
                    .outputMode("append")
                    .option("checkpointLocation", "datafiles/spark-checkpoints/" + UUID.randomUUID().toString())
                    .start("datafiles/spark-delta/wp-posts");

            streamingQuery.awaitTermination();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


    static void readPostsFromDelta()
    {
        SparkSession spark = SparkHelper.getSession();

        String deltaPostsPath = "datafiles/spark-delta/wp-posts";
        Encoder<WordPressPost> wpEncoder = Encoders.bean(WordPressPost.class);

        Dataset<WordPressPost> wpDataset = spark.read().format("delta")
                .load(deltaPostsPath).as(wpEncoder);

        wpDataset.printSchema();
        wpDataset.show();


    }


    static void testDelta()
    {
        SparkSession spark = SparkHelper.getSession();

        String jsonCustomerPath = "datafiles/sample/customers.json";
        String csvCustomerPath = "datafiles/sample/customers.csv";
        Encoder<Customer> customerEncoder = Encoders.bean(Customer.class);

//        Dataset<Row> df = spark.read().json(jsonCustomerPath);
//        df.printSchema();
//        df.show();

        //Dataset<Customer> customerDataset = spark.read().json(jsonCustomerPath).as(customerEncoder);

        Dataset<Customer> customerDataset = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .option("mode", "PERMISSIVE")
                .option("dateFormat", "dd-MM-yyyy hh:mm:ss")
                .option("timeStampFormat", "dd-MM-yyyy hh:mm:ss")
                .option("header", "true")
                .csv(csvCustomerPath).as(customerEncoder);


        customerDataset.printSchema();
        customerDataset.show();

        customerDataset.write().format("delta").save("datafiles/spark-delta/customers");

    }


    static void readDelta1()
    {
        SparkSession spark = SparkHelper.getSession();

        String jsonCustomerPath = "datafiles/sample/customers.json";
        String csvCustomerPath = "datafiles/sample/customers.csv";
        String deltaCustomerPath = "datafiles/spark-delta/customers";
        Encoder<Customer> customerEncoder = Encoders.bean(Customer.class);

//        Dataset<Row> df = spark.read().json(jsonCustomerPath);
//        df.printSchema();
//        df.show();

        Dataset<Customer> customerDataset = spark.read().format("delta").load(deltaCustomerPath).as(customerEncoder);

        customerDataset.printSchema();
        customerDataset.show();


    }


}


