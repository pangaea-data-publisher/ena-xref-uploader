# ena-xref-uploader
This code cross-references environmental data in PANGAEA to sequence data in ENA.
The program is triggered once weekly by cron. It will produce a file containing the required
information (insdc, id_dataset and citation) and uploads it to an ENA FTP server.

The generated, runnable jar must be copied to pangaea-mw1.awi.de/ena_xref/xref.jar (user middlew).
