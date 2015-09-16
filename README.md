{\rtf1\ansi\ansicpg1252\cocoartf1347\cocoasubrtf570
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
\margl1440\margr1440\vieww17240\viewh11120\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural

\f0\fs24 \cf0 # Nuxeo External File Importer\
\
\
The file importer is an add-on to the Nuxeo platform.\
It creates an externally referenced set of files and their directory structure within Nuxeo while preserving and not migrating into the Nuxeo binary store.\
If provides the same content functionality as internally stored content.\
\
## Building\
### How to Build Nuxeo External File Importer\
Build the Nuxeo External File Importer with Maven:\
```$ mvn install -Dmaven.test.skip=true```\
\
## Deploying\
Nuxeo Platform Importer is available as a package add-on:\
https://github.com/mobrebski/marketplace-externalblob-importer\
OR\
Build and deploy the jar, as described above into /nuxeo/nxserver/bundles\
\
## Documentation\
\
\
}