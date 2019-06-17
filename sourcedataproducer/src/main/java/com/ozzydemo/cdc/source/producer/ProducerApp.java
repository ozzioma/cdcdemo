package com.ozzydemo.cdc.source.producer;



import com.ozzydemo.cdc.source.producer.kafka.KafkaProducer;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApp implements CommandLineRunner
{
    public static void main(String[] args)
    {
        //Locale.setDefault(Locale.US);
        val context = SpringApplication.run(ProducerApp.class, args);
    }

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public void run(String... args) throws Exception
    {
        //SampleDataGen.demo1();

        kafkaProducer.generateCustomers(20);
    }

    void testLog1()
    {

    }


}