# Maximo SAPID impact analysis

## Included source

The following Automation Scripts from the Oracle AUTOSCRIPT export were added as individual source files because they are directly relevant to the current SAPID impact investigation:

- X4RENAMESAPIDWO
- X4CHANGEODMNTK
- X4OWNGROUPNESE
- X4CHANGE2LVL_SNOW
- X4WORKORDER_ADD
- X3SAPIDVALIDATION
- X3SAPIDLIVELLO2
- X4CREATEODMNTK
- X4REQSAP

The complete 137-script CSV supplied for the investigation remains the source dataset. Launch Point metadata is still required to establish which code paths are actually active in the MAS environment.

## Main dependency paths under investigation

- ASSET.X3SAPID -> WORKORDER.WOEQ9
- ASSET.X3SAPID -> WORKORDER.X4SAPIDASSET (creation/population paths)
- ASSETLOCRELATION.X3SAPID -> WORKORDER.X4SAPIDASSET
- WORKORDER.X4SAPIDASSET -> SAP ODM/NTK payloads
- ASSETLOCRELATION.X3SAPID -> X4RENAMESAPIDSNOW queue
- SAPID validation/recoding -> SAP connector scenarios and object changes

## Security

Hard-coded credential/secret literals detected in source were redacted in the Git copy. Do not commit the original CSV if it contains live credentials.

## Important limitation

A script being present in AUTOSCRIPT, or having STATUS=DRAFT in the export, does not by itself prove that its Launch Point is active.