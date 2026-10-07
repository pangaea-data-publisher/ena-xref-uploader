package de.pangaea.xref.model;

public record EnaXref(String insdc, String datasetId, String citationUrl, String citation) {
    // Constructor to create a new record with an updated citation
    public EnaXref withCitation(String newCitation) {
        return new EnaXref(this.insdc, this.datasetId, this.citationUrl, newCitation);
    }
}