package com.tsfn.loaders.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.loaders.bean.MarketingData;
import com.tsfn.loaders.helper.Helper;
import com.tsfn.loaders.helper.MapInfo;
import com.tsfn.loaders.repository.MarketingDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.tsfn.loaders.helper.FieldName.*;

@Service
@AllArgsConstructor
public class MarketingDataService {

    @Autowired
    private MarketingDataRepository marketingDataRepository;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Async
    public void saveToDB(String csvFileContent, String fileFullName) throws CsvValidationException, IOException {
        taskExecutor.execute(() -> {
            try (CSVReader csvReader = new CSVReader(new StringReader(csvFileContent))) {
                List<String> columns = new ArrayList<>();
                for (String str : csvReader.readNext()) {
                    columns.add(str.replaceAll("\uFEFF", "").trim());
                }
                String[] nextRecord;
                long accountId = Long.parseLong(fileFullName.split("_")[0]);
                String fileName = fileFullName.split("_")[1];
                MapInfo mapper = Helper.mappingExcelToListOfDats(columns, fileName);
                String currentTime = Helper.currentTimestamp();
                while ((nextRecord = csvReader.readNext()) != null) {
                    try {
                        MarketingData m = new MarketingData();
                        if (!nextRecord[mapper.getMap().get(POST_ID.getFieldName())].isEmpty()) {
                            m.setPostID(nextRecord[mapper.getMap().get(POST_ID.getFieldName())]);
                            m.setFileUploaded(fileFullName);
                            m.setAccountId(accountId);
                            if (!nextRecord[mapper.getMap().get(VIEWS.getFieldName())].isEmpty() && mapper.getMapName().equals(LINKEDIN.getFieldName())) {
                                int excludingOffsiteVideoViews = nextRecord[mapper.getMap().get(VIEWS.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(VIEWS.getFieldName())]);
                                int offsiteViews = nextRecord[mapper.getMap().get(OFFSITE_VIEWS.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(OFFSITE_VIEWS.getFieldName())]);
                                m.setViews(excludingOffsiteVideoViews + offsiteViews);
                            } else {
                                m.setViews(nextRecord[mapper.getMap().get(VIEWS.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(VIEWS.getFieldName())]));
                            }
                            m.setImpressions(nextRecord[mapper.getMap().get(IMPRESSIONS.getFieldName())].isEmpty() ? 0 : Double.parseDouble(nextRecord[mapper.getMap().get(IMPRESSIONS.getFieldName())]));
                            m.setClicks(nextRecord[mapper.getMap().get(CLICKS.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(CLICKS.getFieldName())]));
                            if (mapper.getMapName().equals(LINKEDIN.getFieldName())) {
                                m.setCtr(nextRecord[mapper.getMap().get(CTR.getFieldName())].isEmpty() ? 0 : Double.parseDouble(nextRecord[mapper.getMap().get(CTR.getFieldName())]));
                            } else {
                                if (m.getImpressions() != 0) {
                                    m.setCtr(m.getClicks() / m.getImpressions());
                                } else {
                                    m.setCtr(m.getClicks());
                                }
                            }
                            m.setLikes(nextRecord[mapper.getMap().get(LIKES.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(LIKES.getFieldName())]));
                            m.setComments(nextRecord[mapper.getMap().get(COMMENTS.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(COMMENTS.getFieldName())]));
                            m.setShares(nextRecord[mapper.getMap().get(SHARES.getFieldName())].isEmpty() ? 0 : Integer.parseInt(nextRecord[mapper.getMap().get(SHARES.getFieldName())]));

                            if (mapper.getMapName().equals(FACEBOOK.getFieldName())) {
                                double reach = m.getViews();
                                double reactionsCommentsShares = nextRecord[mapper.getMap().get(RCS.getFieldName())].isEmpty() ? 1 : Double.parseDouble(nextRecord[mapper.getMap().get(RCS.getFieldName())]);
                                m.setEngagementRate(reactionsCommentsShares / reach);
                            }
                            if (mapper.getMapName().equals(INSTAGRAM.getFieldName())) {
                                double likes = m.getLikes();
                                double comments = m.getComments();
                                double shares = m.getShares();
                                double reach = m.getViews();
                                if (reach != 0) {
                                    m.setEngagementRate((likes + comments + shares) / reach);
                                } else {
                                    m.setEngagementRate((likes + comments + shares));
                                }
                            }
                            if (mapper.getMapName().equals(LINKEDIN.getFieldName())) {
                                m.setEngagementRate(nextRecord[mapper.getMap().get(ENGAGEMENT_RATE.getFieldName())].isEmpty() ? 0 : Double.parseDouble(nextRecord[mapper.getMap().get(ENGAGEMENT_RATE.getFieldName())]));
                            }
                            if (!nextRecord[mapper.getMap().get(DESCRIPTION.getFieldName())].isEmpty()) {
                                String description = nextRecord[mapper.getMap().get(DESCRIPTION.getFieldName())];
                                int maxDescriptionLength = 1000;
                                String truncatedDescription = description.length() > maxDescriptionLength ? description.substring(0, maxDescriptionLength) : description;
                                m.setDescription(truncatedDescription);
                            } else {
                                m.setDescription("");
                            }
                            m.setContentType(nextRecord[mapper.getMap().get(CONTENT_TYPE.getFieldName())].isEmpty() ? "" : nextRecord[mapper.getMap().get(CONTENT_TYPE.getFieldName())]);
                            m.setTimestamp(currentTime);
                            marketingDataRepository.save(m);
                        }
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } catch (CsvValidationException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<String> getFileNamesFromGithub(String directoryUrl) throws IOException {
        List<String> fileNames = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(directoryUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        for (JsonNode item : jsonNode) {
            String fileName = item.get("name").asText();
            fileNames.add(fileName);
        }
        return fileNames;
    }

    public String pullCSVFile(String directory, String csvFileName) {
        String endPoint = "https://raw.githubusercontent.com/fadykittan/tsofen_project_data_files/main/" + directory;
        String rawFileUrl = endPoint + "/" + csvFileName;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(rawFileUrl, String.class);
    }

    public void checkCSVFileProvidersDirectory(String directory) {
        try {
            String directoryUrl = "https://api.github.com/repos/fadykittan/tsofen_project_data_files/contents/" + directory;
            List<String> csvFileNames = this.getFileNamesFromGithub(directoryUrl);
            for (String csvFileName : csvFileNames) {
                if (csvFileName != null) {
                    if (!this.checkFileName(csvFileName)) {
                        String csvFileContent = this.pullCSVFile(directory, csvFileName);
                        this.saveToDB(csvFileContent, csvFileName);
                        System.out.println("The file name " + csvFileName + " scan in the system");
                    }
                } else {
                    System.out.println("The file name in not Valid or Empty");
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while checking for CSV file: " + e.getMessage());

        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public Double checkMetric(long accountId, String startTime, String endTime, String metric) {
        return marketingDataRepository.hasEnoughClicksByAccountIdAndTimeRange(
                accountId, startTime, endTime, metric);
    }

    public boolean checkFileName(String fileName) {
        return 0 < marketingDataRepository.countByFileUploaded(fileName);
    }

}