#X3CONPLTXTSTZ
#Custom rule handler del connettore MAXIMO-SAP per il calcolo dell'attributo di testata delle sedi tecniche delle STAZIONI Pltxt
#v 1.0

from java.util.regex import Pattern, Matcher
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning


descriptionMap = {'^3[0-6][0-9]{3}[A-Z]-5[2-6]-10[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-10[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-10[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-10[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-11[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-11[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-11[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-11[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-12[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-12[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-12[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-12[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-13[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-13[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-13[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-13[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-14[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-14[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-14[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-14[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-16[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-16[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-16[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-16[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-18[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-18[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-18[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-18[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-17[0-9|A-Z|-]{2}-A$':'Apparecchi/colleg. di potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-17[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-17[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-5[2-6]-17[0-9|A-Z|-]{2}-E$':'Opere civili/Strutture portanti U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-10[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-10[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-10[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-10[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-11[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-11[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-11[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-11[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-12[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-12[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-12[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-12[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-13[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-13[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-13[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-13[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-14[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-14[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-14[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-14[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-15[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-15[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-15[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-77-15[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-10[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-10[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-10[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-10[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-11[0-9|A-Z|-]{2}-A$':'Macchinario/apparecchi potenza di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-11[0-9|A-Z|-]{2}-B$':'Circuiti/apparati di SPCC di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-11[0-9|A-Z|-]{2}-C$':'Complessi di misura di U.F.',
'^3[0-6][0-9]{3}[A-Z]-78-11[0-9|A-Z|-]{2}-E$':'Opere civili e vasca tramoggia U.F.',
'^3[0-6][0-9]{3}[A-Z]-92-10[0-9|A-Z|-]{2}-A$':'Apparecchiat. e collegamenti di potenza',
'^3[0-6][0-9]{3}[A-Z]-92-10[0-9|A-Z|-]{2}-E$':'Opere civili e strutture portanti di U.F',
'^3[0-6][0-9]{3}[A-Z]-92-10[0-9|A-Z|-]{2}-Q$':'Trasformatori S.A.',
'^3[0-6][0-9]{3}[A-Z]-92-11[0-9|A-Z|-]{2}-A$':'Apparecchiat. e collegamenti di potenza',
'^3[0-6][0-9]{3}[A-Z]-92-11[0-9|A-Z|-]{2}-B$':'Circuiti e app. di Protezione e Comando',
'^3[0-6][0-9]{3}[A-Z]-92-11[0-9|A-Z|-]{2}-E$':'Opere civili e strutture portanti di U.F',
'^3[0-6][0-9]{3}[A-Z]-92-11[0-9|A-Z|-]{2}-H$':'Quadri e collegamenti BT in c.a.',
'^3[0-6][0-9]{3}[A-Z]-92-12[0-9|A-Z|-]{2}-H$':'Quadri e collegamenti BT in c.c.',
'^3[0-6][0-9]{3}[A-Z]-92-12[0-9|A-Z|-]{2}-S$':'Batterie accumulatori',
'^3[0-6][0-9]{3}[A-Z]-92-12[0-9|A-Z|-]{2}-T$':'Raddrizzatori carica batterie',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-P$':'Recinzioni e cancelli',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-Q$':'Terreni e aree verdi',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-R$':'Strade e piazzali',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-S$':'Rete idrica',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-T$':'Fognature',
'^3[0-6][0-9]{3}[A-Z]-93-11[0-9|A-Z|-]{2}-U$':'Scarico acque',
'^3[0-6][0-9]{3}[A-Z]-93-12[0-9|A-Z|-]{2}-P$':'Opere civili',
'^3[0-6][0-9]{3}[A-Z]-93-12[0-9|A-Z|-]{2}-Q$':'Impianti elettrici',
'^3[0-6][0-9]{3}[A-Z]-93-12[0-9|A-Z|-]{2}-R$':'Imp. idrosan. e di riscald. non elettr.',
'^3[0-6][0-9]{3}[A-Z]-93-12[0-9|A-Z|-]{2}-S$':'Impianti di sollevamento (ascensori,ecc)',
'^3[0-6][0-9]{3}[A-Z]-93-12[0-9|A-Z|-]{2}-T$':'Imp. di condiz. locali e/o centralizzati',
'^3[0-6][0-9]{3}[A-Z]-93-13[0-9|A-Z|-]{2}-P$':'Opere civili',
'^3[0-6][0-9]{3}[A-Z]-93-13[0-9|A-Z|-]{2}-Q$':'Impianti elettrici',
'^3[0-6][0-9]{3}[A-Z]-93-14[0-9|A-Z|-]{2}-P$':'Impianto antintrusione e/o controllo acc',
'^3[0-6][0-9]{3}[A-Z]-93-14[0-9|A-Z|-]{2}-Q$':'Impianto illuminazione e FM esterni',
'^3[0-6][0-9]{3}[A-Z]-93-14[0-9|A-Z|-]{2}-R$':'Impianto telefonico / trasmissione dati',
'^3[0-6][0-9]{3}[A-Z]-93-14[0-9|A-Z|-]{2}-S$':'Imp. rilevazione/spegnimento incendi',
'^3[0-6][0-9]{3}[A-Z]-93-14[0-9|A-Z|-]{2}-T$':'Impianto sollevamento(carroponte, gru)'}
										   


def regexStrno(strno):
    for regex_pattern, description in descriptionMap.items():
        pattern = Pattern.compile(regex_pattern)
        matcher = pattern.matcher(strno)
        if matcher.matches():
            return description
    return None

strno = sapAsset.getString("STRNO")

# Trova il valore corrispondente
description = regexStrno(strno)

if description:
    mappedRawValue = description
else:
    ruleHandler.addSimpleWarningForRule(mappingRule)