# Nuxeo External Blob Importer  
#####THIS IS A WORKING PROOF OF CONCEPT  
This plugin is an extension of the Nuxeo Bulk Importer.
It allows for importing of a set of documents from a server accessible file system, with copying the binary files into the Nuxeo Binary store.  
Binary files remain externally referenced while still providing the same Document Management capabilities of Nuxeo.  
The main file is external, while previews and rendition are created and stored in Nuxeo.
The referenced files are no longer modifiable from the Nuxeo system and there is no content synchronization, other than direct download using the normal methods.

#### Using
To Run:  
Use the same standard Bulk Importer methods as documented in:  
https://doc.nuxeo.com/display/ADMINDOC/Bulk+Document+Importer

#### Dependancies
Nuxeo Bulk Importer - https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-platform-importer  
Nuxeo DAM - https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-dam 
#### Build
Build the Nuxeo External Blob Importer with Maven:
```$ mvn install -Dmaven.test.skip=true```  
There is also a Marketplace package which will build this:  
https://github.com/mobrebski/marketplace-externalblob-importer
#### Deploy
Build jar as described above and manually deploy on your Nuxeo server to /nuxeo/nxserver/bundles  
OR  
Build the marketplace package and deploy from Nuxeo Admin Update center as a local package from Zip



