# Maximo Automation Scripts — SAPID impact analysis

This folder contains the Automation Script SOURCE extracted from the Oracle/MAS AUTOSCRIPT export supplied for the SAPID impact analysis.

- 137 Automation Scripts recovered from the CSV.
- SOURCE is stored as individual `.py` files.
- Original script name, description and status are recorded in `metadata.csv`.
- This repository copy is intended for static analysis and traceability; it is not a Maximo deployment package.
- Hard-coded credential/secrets detected in source are redacted in this Git copy. The original CSV remains the authoritative source for exact values and should not be committed if it contains secrets.
- Launch Point metadata is not included yet; it must be added from `AUTOSCRIPTLAUNCHPOINT` before runtime activation can be concluded.
