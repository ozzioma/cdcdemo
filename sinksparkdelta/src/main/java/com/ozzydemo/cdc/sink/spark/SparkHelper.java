package com.ozzydemo.cdc.sink.spark;

import org.apache.spark.sql.SparkSession;

import java.nio.file.Paths;

public class SparkHelper
{

    public static SparkSession getSession()
    {

        String warehousePath = Paths.get("datafiles/spark-warehouse").toAbsolutePath().toString();

        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win"))
        {
            System.setProperty("hadoop.home.dir", Paths.get("runtime").toAbsolutePath().toString());
        }
        else
        {
            System.setProperty("hadoop.home.dir", "");
        }

        SparkSession spark = SparkSession
                .builder()
                .master("local[1]")
                .appName("cdc spark demo")
                .config("spark.driver.memory", "1g")
                .config("spark.executor.memory", "1g")
                .config("spark.sql.warehouse.dir", warehousePath)
                .enableHiveSupport()
                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        return spark;
    }
}
