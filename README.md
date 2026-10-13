# ena-xref-uploader
This code cross-references environmental data in PANGAEA to sequence data in ENA.
The program is triggered once monthly by cron.It will produce a file containing the required information (insdc, id_dataset
and citation) and uploads it to an ENA FTP server.
