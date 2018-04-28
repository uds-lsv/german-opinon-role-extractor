## About

This install script was written to install the necessary preprocessing tool for the   
_Opinion Holder an Target Extraction for German_   
Tool, created at Saarland University.

The script was tested under Ubuntu 16.04 LTS, and works as of 2016-08-09, presuming that the websites and git repositories of the tools are online.

## Running the script

The script needs sudo rights to install necessary dependencies on the Ubuntu machine and to run the make command to install some of the NLP-tools. Therefore you need to enter your machines password after running the script.

Before running the script, put it in the destination folder, where the installed tools should be. The script will create all necessary folders for the tools itself.

To run the script, use the following command:

	sh install_script.sh

After installation you can set the path in the preprocessing config, to run the preprocessing tool with the installed NLP-tools.
