The evaluation tool for the IGGSA-STEPS shared task is provided as a runnable jar (needs java 7). The tool comes with a gui, where you can specify the location of the files containing the gold standard and the system annotations. Besides a result file in the csv or xlsx format a log file with detailed information on matches, false positives and false negatives is generated.

The result file provides two types of evaluations: One is based on exact matches only and the other incorporates partial matches as well. For the latter the dice coefficent is given to provide information on the overlap of system and gold standard annotations. For sources and targets the result file shows measures based on the general performance and based on cases with a correct match for the respective subjective expression.

The adjudicated gold standard file from the 2014 shared task (shata14_adjudicated.xml) consists of 17 different speeches on topics like book price controls or foreign policy. For the evaluation on this gold standard file, the measures (recall, precision, f-measure, dice) are given on the individual speeches. To obtain overall measures, the possibility of calculating a macro average (average over all documents) and a micro average (based on the total number of annotated entites) is given. The same is true for the 2016 test gold standard. By hitting the "start evaluation"-button the agreement measures for the different speeches are calculated. Micro and macro averages can be obtained be the corresponding buttons. Of course, the evaluation tool can be used with any arbitrary gold standard file (provided the file is in SalsaTIGER-XML format). In case a file other than the previously mentioned files is used, you can simply specify the locations of the corresponding files and hit the "start evaluation" button. If you want to use multiple files, you need to specifiy each file in a separate step. The results for each system-goldstandard-file-pair will be added at the bottom of the results.csv/xlsx file. If you don't close the evaluation tool between the calculations for the different file pairs, you can also obtain a  micro and macro average by hitting the corresponding buttons.


Abbreviations:
SE: Subjective Expression
FE: Frame Element (Source/Target)

Additional remarks:
If there are multiple gold standard annotations matching one system annotation the best match according to the annotated sources and targets as well as the dice coefficient is taken. If this still leads to mulitple matches, the first one is choosen as the true positive.
