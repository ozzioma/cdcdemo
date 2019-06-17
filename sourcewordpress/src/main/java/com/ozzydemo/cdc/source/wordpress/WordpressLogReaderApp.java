package com.ozzydemo.cdc.source.wordpress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.ozzydemo.cdc.commons.data.domain.WordPressPost;
import lombok.val;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(
        scanBasePackages = {"com.ozzydemo.cdc.commons.data", "com.ozzydemo.cdc.source"}
)

public class WordpressLogReaderApp implements CommandLineRunner
{

    @Autowired
    WordPressProducer wordPressProducer;

    public static void main(String[] args)
    {
        //Locale.setDefault(Locale.US);
        val context = SpringApplication.run(WordpressLogReaderApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        //logWordpressPosts();
        wordPressProducer.logWordpressPosts();
    }



}


