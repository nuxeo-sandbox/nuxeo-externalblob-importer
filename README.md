# Nuxeo External File Importer


The file importer is an add-on to the Nuxeo platform.
It creates an externally referenced set of files and their directory structure within Nuxeo while preserving and not migrating into the Nuxeo binary store.
If provides the same content functionality as internally stored content.

## Building
### How to Build Nuxeo External File Importer
Build the Nuxeo External File Importer with Maven:
```$ mvn install -Dmaven.test.skip=true```

## Deploying
Nuxeo Platform Importer is available as a package add-on:
https://github.com/mobrebski/marketplace-externalblob-importer
OR
Build and deploy the jar, as described above into /nuxeo/nxserver/bundles

## Documentation


