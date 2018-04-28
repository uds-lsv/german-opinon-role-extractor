#!/bin/bash

# function to show help
function show_help() {
  echo "usage: /preprocess.sh [-r] [-k] <input file>"
  echo "use no flag to process tigerXML"
  echo "use -r to process raw text"
  echo "use -k to keep temporary files of the tools"  
}

# function to create ouput folder
function create_folder(){
  # create timestamped folder
  current_time=$(date "+%Y.%m.%d-%H.%M")
  output_folder=preprocess.$current_time
  
  mkdir $output_folder
}

# function to move raw file
function move_raw(){
  cp $input_file $output_folder/raw_text.txt
}

# function to do ParZu processing
function parzu_parse(){
  # prepare for parzu
  sed -e 'G;' $output_folder/raw_text.txt | tr ' ' '\n' > $output_folder/tokenized
  
  $PATHTOPARZU -q -i tokenized < $output_folder/tokenized > $output_folder/dependency_parse.txt
}

# function to do BerkleyParser processing
function berkeley_parse(){
  java -jar $PATHTOBERKELEYPARSER -gr $PATHTOBERKELEYGRAMMAR -inputFile $output_folder/raw_text.txt -outputFile $output_folder/berkeley_out
  
  # Convert Berkeley Out with LinguaAlign
  $PATHTOLINGUAALIGN $output_folder/berkeley_out berkeley tiger > $output_folder/constituency_parse.xml

  # add Tiger information
  sed -i 's/pos="/lemma="" pos="/g' $output_folder/constituency_parse.xml
}

# function to do GermaNer processing
function germaner_parse(){
  if [ $(getconf LONG_BIT) = "64" ]
    then
      java -Xmx4G -jar $PATHTOGERMANER64 -t $output_folder/tokenized -o $output_folder/germaner_out &> /dev/null
    else
      GERMADIR=$(dirname $PATHTOGERMANER32)
      PATH=$PATH:$GERMADIR/crf/bin
      java -Xmx1300M -jar $PATHTOGERMANER32 -t $output_folder/tokenized -o $output_folder/germaner_out &> /dev/null
      
  fi
  # convert GermaNER output into readable format
  sed 's#  #/#g' $output_folder/germaner_out | perl -00 -lpe 'tr/\n/ /d' | awk 'NF' > $output_folder/named_entity.txt
}

# function to convert tigerXML to raw
function tiger_to_raw(){
  LDIR=$(dirname $PATHTOLINGUAALIGN)
  $LDIR/tiger2text $input_file > $output_folder/text
  iconv -f ISO-8859-1 -t UTF-8 $output_folder/text > $output_folder/raw_text.txt
}

# function to preprocess tigerXMl data
# iterate over tigerXML file to add lemma and pos if necessary
function preprocess_tiger_xml(){
  IFS=''
  while read s; do
   if [[ $s == *'pos="'* ]] && [[ $s != *'lemma="'* ]];
    then
     (echo "$s" | sed 's/pos="/lemma="" pos="/g') >> $output_folder/constituency_parse.xml
    else
     echo "$s" >> $output_folder/constituency_parse.xml
   fi
  done < $input_file
}

# function to adapt file formats
function adapt_fileformat(){

  # dependency parse
  while read s; do
   if [[ $s == *'&quot;'* ]];
    then
     (echo "$s" | sed 's/quot;/amp;quot;/g') >> $output_folder/dependency_parse
    else
     echo "$s" >> $output_folder/dependency_parse
   fi
  done < $output_folder/dependency_parse.txt
  
  mv $output_folder/dependency_parse $output_folder/dependency_parse.txt
  
  # named entity
  while read s; do
   if [[ $s == *'&quot;'* ]];
    then
     (echo "$s" | sed 's/quot;/amp;quot;/g') >> $output_folder/named_entity
    else
     echo "$s" >> $output_folder/named_entity
   fi
  done < $output_folder/named_entity.txt
  
  mv $output_folder/named_entity $output_folder/named_entity.txt
  
  # raw text
  while read s; do
   if [[ $s == *'&quot;'* ]];
    then
     (echo "$s" | sed 's/quot;/amp;quot;/g') >> $output_folder/raw_text
    else
     echo "$s" >> $output_folder/raw_text
   fi
  done < $output_folder/raw_text.txt
  
  mv $output_folder/raw_text $output_folder/raw_text.txt
}

# function to correct berkeley output
function adapt_berkeley_format(){
  # constituency parse
  while read s; do
   if [[ $s == *'&amp;amp;'* ]];
    then
     (echo "$s" | sed 's/amp;amp;/amp;/g') >> $output_folder/constituency_parse
    else
     echo "$s" >> $output_folder/constituency_parse
   fi
  done < $output_folder/constituency_parse.xml
  
  mv $output_folder/constituency_parse $output_folder/constituency_parse.xml
}

# function to clean up directory
function cleanup(){
  rm $output_folder/tokenized
  rm $output_folder/germaner_out
  rm $output_folder/tokenized.normalized
  rm -rf output

  if $raw
    then
      rm $output_folder/berkeley_out
    else
      rm $output_folder/text
  fi

}

# script starts here
# read command line options to determine if tiger or raw text should be processed
input_file=""
raw=false
keep_files=false

while getopts ":hrk" opt; do
  case "$opt" in
  h|\?)
    show_help
    exit 0
    ;;
  r)
    raw=true
    ;;
  k)
    keep_files=true
    ;;
  esac
done

shift $((OPTIND-1))
# read filename 
if [ -z $1 ]
  then
    show_help
    exit 1
  else
  input_file=$1
fi

# all command line options read, begin processing

START_TIME=$SECONDS

# read paths from config file
source config

# create ouput folder
echo "creating output folder"
create_folder

# create raw file or tigerXMl, depending on flag
if $raw
  then # raw text file processing
    echo "processing raw text input"
    move_raw
    echo "parsing with BerkeleyParser"
    berkeley_parse
  else # tigerXML processing
    echo "processing tigerXML input"
    tiger_to_raw
    preprocess_tiger_xml
fi

# parse with ParZu
echo "parsing with ParZu"
parzu_parse

# parse with GermaNER
echo "parsing with GermaNER"
germaner_parse

# adapt fileformats
echo "adapt file formats"
adapt_fileformat
if $raw
  then
    adapt_berkeley_format
fi

# cleanup directory
if ! $keep_files
 then
    echo "cleanup directory"
    cleanup
  else
    mv output $output_folder/germaner_output
fi

ELAPSED_TIME=$(($SECONDS - $START_TIME))

echo "all done" 
echo "Duration of Preprocessing:"
echo $ELAPSED_TIME
