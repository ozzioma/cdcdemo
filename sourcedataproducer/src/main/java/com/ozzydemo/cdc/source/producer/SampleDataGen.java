package com.ozzydemo.cdc.source.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.github.javafaker.Faker;
import com.ozzydemo.cdc.commons.data.domain.Customer;
import com.ozzydemo.cdc.commons.data.domain.PostingType;
import com.ozzydemo.cdc.commons.data.domain.TransactionType;
import com.ozzydemo.cdc.commons.data.domain.TxnDto;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SampleDataGen
{

    static String filePath = "datafiles\\sample\\customers.json";
    static String filePath2 = "datafiles\\sample\\customers.csv";

    static String filePathTxn = "datafiles\\sample\\txn.json";
    static String filePathTxn2 = "datafiles\\sample\\txn.csv";


    public static void demo1() throws Exception
    {
//        MessageDigest salt = MessageDigest.getInstance("SHA-256");
//        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
//        String digest = bytesToHex(salt.digest());

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        UUID uuid2 = Generators.randomBasedGenerator().generate();
        UUID uuid3 = Generators.timeBasedGenerator().generate();

        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

        System.out.println(uuid2);
        System.out.println(uuid3);
        System.out.println(gen.generate());
        System.out.println(gen.generate());

        Faker faker = new Faker(new Random(100));

        System.out.println(faker.idNumber().ssnValid());
        System.out.println(faker.idNumber().valid());
        System.out.println(faker.idNumber().validSvSeSsn());
        System.out.println("asin->" + faker.code().asin());
        System.out.println("ean8->" + faker.code().ean8());
        System.out.println("ean13->" + faker.code().ean13());
        System.out.println("gtin8->" + faker.code().gtin8());
        System.out.println("gtin13->" + faker.code().gtin13());
        System.out.println("imei->" + faker.code().imei());
        System.out.println("isbn10->" + faker.code().isbn10());
        System.out.println("isbn13->" + faker.code().isbn13());
        System.out.println("isbnGs1->" + faker.code().isbnGs1());

        System.out.println("fullname->" + faker.name().fullName());
        System.out.println("email->" + faker.internet().emailAddress());
        System.out.println("fone->" + faker.phoneNumber().cellPhone());
        System.out.println("addr->" + faker.address().fullAddress());

        System.out.println(TransactionType.values()[new Random().nextInt(TransactionType.values().length)]);
        System.out.println(PostingType.values()[new Random().nextInt(PostingType.values().length)]);
        System.out.println(TransactionType.values()[new Random().nextInt(TransactionType.values().length)]);
        System.out.println(PostingType.values()[new Random().nextInt(PostingType.values().length)]);
        System.out.println(TransactionType.values()[new Random().nextInt(TransactionType.values().length)]);
        System.out.println(PostingType.values()[new Random().nextInt(PostingType.values().length)]);

        ObjectMapper jsonMapper = new ObjectMapper();
        //jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        jsonMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, true);
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<Customer> customerList = new ArrayList<>();
        List<TxnDto> txnList = new ArrayList<>();

        for (int counter = 1; counter <= 100; counter++)
        {
            Customer customer = Customer.builder()
                    .accountNo(faker.code().ean13())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .phoneNo(faker.phoneNumber().cellPhone())
                    .address(faker.address().fullAddress())
                    .build();

            customerList.add(customer);
            //System.out.println(jsonMapper.writeValueAsString(customer));
            //jsonMapper.writeValue();

            String tranRef = faker.code().ean13();
            BigDecimal amount = BigDecimal.valueOf(faker.number().numberBetween(50, 200));
            LocalDateTime txnDate = LocalDateTime.ofInstant(faker.date().past(30, TimeUnit.DAYS).toInstant(),
                    ZoneId.systemDefault());

            for (Integer count2 = 1; count2 <= 2; count2++)
            {
                TxnDto debit = TxnDto.builder()
                        .tranRef(tranRef)
                        .tranId(faker.code().ean13())
                        .tranSeqNo(count2.toString())
                        .accountNo(customer.getAccountNo())
                        .transactionType(TransactionType.values()[new Random().nextInt(TransactionType.values().length)])
                        .type(PostingType.DEBIT)
                        .amount(amount)
                        .txnDate(txnDate)
                        .description(faker.book().title())
                        .build();


                TxnDto credit = TxnDto.builder()
                        .tranRef(tranRef)
                        .tranId(faker.code().ean13())
                        .tranSeqNo(count2.toString())
                        .accountNo(customer.getAccountNo())
                        .transactionType(TransactionType.values()[new Random().nextInt(TransactionType.values().length)])
                        .type(PostingType.CREDIT)
                        .amount(amount)
                        .txnDate(txnDate)
                        .description(faker.book().title())
                        .build();

                txnList.add(debit);
                txnList.add(credit);

            }
        }

        //jsonMapper.writer(new MinimalPrettyPrinter("\n")).writeValue(new FileWriter(filePath),customerList);
        jsonMapper.writeValue(new FileWriter(filePath), customerList);

        CsvMapper mapperCustomer = new CsvMapper();
        CsvSchema schemaCustomer = mapperCustomer.schemaFor(Customer.class).withHeader();
        mapperCustomer.writer(schemaCustomer).writeValue(new FileWriter(filePath2), customerList);


        CsvMapper mapperTxn = new CsvMapper();
        CsvSchema schemaTxn = mapperTxn.schemaFor(TxnDto.class).withHeader();
        mapperTxn.writer(schemaTxn).writeValue(new FileWriter(filePathTxn2), txnList);

        jsonMapper.writeValue(new FileWriter(filePathTxn), txnList);

    }

    public static void demo2() throws Exception
    {

        ObjectMapper jsonMapper = new ObjectMapper();

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Customer.class).withHeader();
        ;
        MappingIterator<Customer> rows = mapper.readerFor(Customer.class)
                .with(schema).readValues(new FileReader(filePath2));
        List<Customer> all = rows.readAll();
        all.stream().forEach(r ->
        {
            try
            {
                System.out.println(jsonMapper.writeValueAsString(r));
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace();
            }
        });
    }

}
