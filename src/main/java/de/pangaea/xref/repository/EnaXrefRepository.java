package de.pangaea.xref.repository;

import de.pangaea.xref.model.EnaXref;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface EnaXrefRepository {

    @SqlQuery("SELECT distinct substring(reference.uri,7) as \"insdc\", " +
            "dataset.id_dataset as \"datasetId\", " +
            "'https://doi.pangaea.de/10.1594/PANGAEA.' || dataset.id_dataset || '?format=citation_text' as \"citationUrl\", " +
            "null as \"citation\" " +
            "FROM dataSet JOIN dataset_reference dr ON (dataset.id_dataset=dr.id_dataset) " +
            "JOIN reference ON (dr.id_reference=reference.id_reference) " +
            "WHERE dr.id_relation_type in (12) AND dataset.id_dataset_status>=4 AND reference.uri like 'insdc:%' " +
            "ORDER BY insdc, dataset.id_dataset")
    List<EnaXref> findAllEnaXrefs();
}