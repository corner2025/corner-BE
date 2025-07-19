package com.corner.travel.dutyfree.config;//package com.corner.travel.dutyfree.config;
//
//import com.corner.travel.dutyfree.domain.DutyFreeProduct;
//import com.corner.travel.dutyfree.repository.DutyFreeProductRepository;
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class CsvDataInitializer {
//
//    @Bean
//    public CommandLineRunner loadCsvAtStartup(DutyFreeProductRepository repo) {
//        return args -> {
//            if (repo.count() > 0) {
//                return;
//            }
//
//            repo.deleteAll();
//
//            String[] categoryNames = {
//                    "bag",
//                    "jewellery",
//                    "other",
//                    "tobacco",
//                    "folkcraft",
//                    "watch",
//                    "food",
//                    "shoes",
//                    "glasses",
//                    "clothing",
//                    "redginseng",
//                    "electronics",
//                    "alcohol",
//                    "perfume",
//                    "cosmetics"
//            };
//
//            var resource = new ClassPathResource("dutyfree_sales_250331.csv");
//            try (
//                    Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.ISO_8859_1);
//                    CSVReader csv = new CSVReaderBuilder(reader).withSkipLines(1).build()
//            ) {
//                String[] line;
//                int cnt = 0;
//                while ((line = csv.readNext()) != null) {
//                    String yearMonth = line[0].trim();
//                    for (int i = 0; i < categoryNames.length; i++) {
//                        String cat = categoryNames[i];
//                        String strCount = line[i+1].trim();
//                        if (!strCount.isEmpty()) {
//                            int sales = Integer.parseInt(strCount);
//                            repo.save(new DutyFreeProduct(yearMonth, cat, sales));
//                            cnt++;
//                        }
//                    }
//                }
//                System.out.println("CSV 초기 로딩 완료, 총 " + cnt + " 건");
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException("CSV 초기 로딩 실패", e);
//            }
//        };
//    }
//
//}
