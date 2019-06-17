package com.ozzydemo.cdc.source.producer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.github.javafaker.Faker;
import com.ozzydemo.cdc.commons.data.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class KafkaProducer
{
    @Autowired
    KafkaTemplate<String, Customer> kafkaTemplate;
    String TOPIC_NAME = "cdc-customers";


    public void generateCustomers(final int count)
    {
        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        ObjectMapper jsonMapper = new ObjectMapper();
        //jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        jsonMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, true);
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Faker faker = new Faker(new Random(100));

        for (int counter = 1; counter <= count; counter++)
        {
            Customer customer = Customer.builder()
                    .accountNo(faker.code().ean13())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .phoneNo(faker.phoneNumber().cellPhone())
                    .address(faker.address().fullAddress())
                    .build();


            System.out.println("sending->"+customer);
            kafkaTemplate.send(TOPIC_NAME, customer);

        }
    }

}
