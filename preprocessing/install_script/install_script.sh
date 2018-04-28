#!/bin/bash


# dependencies
sudo apt-get --assume-yes install openjdk-8-jre-headless swi-prolog sfst git perl python2.7


# berkeley parser
#git clone https://github.com/slavpetrov/berkeleyparser BerkeleyParser
mkdir BerkeleyParser
cd BerkeleyParser
wget https://github.com/slavpetrov/berkeleyparser/raw/master/BerkeleyParser-1.7.jar
wget https://github.com/slavpetrov/berkeleyparser/raw/master/ger_sm5.gr
cd ..

# parzu

git clone https://github.com/rsennrich/ParZu ParZu

cd ParZu
sh install.sh
cd ..

# germaner

mkdir GermaNER
cd GermaNER
if [ $(getconf LONG_BIT) = "64" ]
  then
    wget https://github.com/tudarmstadt-lt/GermaNER/releases/download/germaNER0.9.1/GermaNER-09-09-2015.jar
  else
    wget https://github.com/tudarmstadt-lt/GermaNER/releases/download/germaNER0.9.1/GermaNER-nofb-09-09-2015.jar
    # liblbfgs
    wget https://github.com/downloads/chokkan/liblbfgs/liblbfgs-1.10.tar.gz
    tar -zxvf liblbfgs-1.10.tar.gz
    rm liblbfgs-1.10.tar.gz
    cd liblbfgs-1.10
    ./configure --prefix=$PWD/../crf
    make
    make install
    cd ..
    # crfsuite
    wget https://github.com/downloads/chokkan/crfsuite/crfsuite-0.12.tar.gz
    tar -zxvf crfsuite-0.12.tar.gz
    rm crfsuite-0.12.tar.gz
    cd crfsuite-0.12
    ./configure --prefix=$PWD/../crf -with-liblbfgs=$PWD/../crf
    make
    make install
    cd ..
fi
cd ..

# lingua align

wget http://search.cpan.org/CPAN/authors/id/T/TI/TIEDEMANN/Lingua-Align-0.04.tar.gz

tar -zxvf Lingua-Align-0.04.tar.gz

rm Lingua-Align-0.04.tar.gz

wget http://search.cpan.org/CPAN/authors/id/T/TP/TPEDERSE/Algorithm-Munkres-0.08.tar.gz

tar -zxvf Algorithm-Munkres-0.08.tar.gz

rm Algorithm-Munkres-0.08.tar.gz

cp -r Algorithm-Munkres-0.08/lib/Algorithm Lingua-Align-0.04/lib

rm -rf Algorithm-Munkres-0.08

cd Lingua-Align-0.04

perl Makefile.PL

make

sudo make install
