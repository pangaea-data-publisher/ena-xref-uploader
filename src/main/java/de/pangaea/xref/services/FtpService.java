package de.pangaea.xref.services;

import de.pangaea.xref.config.AppConfig;
import de.pangaea.xref.model.EnaXref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FtpService {

    private static final Logger logger = LoggerFactory.getLogger(FtpService.class);
    private final AppConfig config;

    public FtpService(AppConfig config) {
        this.config = config;
    }

    public void upload(List<EnaXref> xrefs) {
        String ftpUrlString = String.format("ftp://%s:%s@%s%s;type=i",
                config.getFtpUser(), config.getFtpPassword(), config.getFtpHost(), config.getFtpPath());

    /*
    local test
     */
//        Writer writer2 = new StringWriter();
//        try {
//            for (EnaXref xref : xrefs) {
//                writer2.write(xref.insdc() + "\t" + xref.datasetId() + "\t" + xref.citation() + "\n");
//            }
//            writer2.flush();
//            System.out.println(writer2.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        logger.debug("Streaming {} records to ENA FTP at {}...", xrefs.size(), config.getFtpHost());

        try {
            URL url = new URL(ftpUrlString);
            URLConnection conn = url.openConnection();

            try (OutputStream outs = conn.getOutputStream();
                Writer writer = new OutputStreamWriter(outs, StandardCharsets.UTF_8)) {

                for (EnaXref xref : xrefs) {
                    writer.write(xref.insdc() + "\t" + xref.datasetId() + "\t" + xref.citation() + "\n");
                }
                writer.flush();

            }
            logger.debug("...done streaming to FTP.");
        } catch (IOException e) {
            logger.error("Failed to upload to FTP", e);
        }
    }
}