#!/bin/bash

## Creates Unit test from expected output file
## usage:
##
## source gentest.sh ; genfiles ; mv *.java ../../java/
##

function print_test() {
	echo "public class Image${2}Test extends AbstractVerifyImage {"
	echo -e "\n\t@Override\n\tpublic String getFilename() {\n\t\treturn \"${1}\";\n\t}\n"
	echo -e "\n\t@Override\n\tpublic double getExpectedEnergy(boolean horizontal) {"
	echo -e "\t\treturn horizontal ? ${3} : ${4};\n"
	echo -e "\t}"

	echo -e "\n\t@Override\n\tpublic int[] getExpectedSeam(boolean horizontal) {"
	echo -e "\t\treturn horizontal ? new int[]${5} : new int[]${6};\n"
	echo -e "\t}"

	echo "}"; 
}

function readvars() { 
	HORIZONTAL_ENERGY="0.1"
	VERTICAL_ENERGY="0.1" 

	HORIZONTAL_SEAM=$(awk -F ":" '/Horizontal seam:/' $1)
	VERTICAL_SEAM=$(awk -F ":" '/Vertical seam:/' $1)


	VERTICAL_SEAM="${VERTICAL_SEAM##Vertical seam: }" 
	HORIZONTAL_SEAM="${HORIZONTAL_SEAM##Horizontal seam: }" 

	VERTICAL_SEAM="${VERTICAL_SEAM/ /!}"
	HORIZONTAL_SEAM="${HORIZONTAL_SEAM/ /!}"

	VERTICAL_SEAM="${VERTICAL_SEAM/ \}/!\}}"
	HORIZONTAL_SEAM="${HORIZONTAL_SEAM/ \}/!\}}"

	VERTICAL_SEAM="${VERTICAL_SEAM// /,}"
	HORIZONTAL_SEAM="${HORIZONTAL_SEAM// /,}"

	VERTICAL_SEAM="${VERTICAL_SEAM//!/ }"
	HORIZONTAL_SEAM="${HORIZONTAL_SEAM//!/ }"

#	echo "${VERTICAL_SEAM}"
#	echo "${HORIZONTAL_SEAM}"
	export HORIZONTAL_SEAM
	export VERTICAL_SEAM


	unset E_ARRAY
	readarray E_ARRAY < <( awk '/Total energy = /{print $4}' $1 )

	VERTICAL_ENERGY="${E_ARRAY[0]}"
	HORIZONTAL_ENERGY="${E_ARRAY[1]}"


	VERTICAL_ENERGY="${VERTICAL_ENERGY:0:-1}"
	HORIZONTAL_ENERGY="${HORIZONTAL_ENERGY:0:-1}"

#	echo "${VERTICAL_ENERGY}"
#	echo "${HORIZONTAL_ENERGY}"
	export VERTICAL_ENERGY
	export HORIZONTAL_ENERGY


}


function genfiles()
{
	for FILE in $(ls *.printseams.txt); 
	do 
		NAME="${FILE%%.printseams.txt}"; 
		CLASS="Image${NAME}Test"
		echo -e "Creating ${CLASS}.java: "
		readvars "${FILE}" 
		print_test "${NAME}.png" "${NAME}" "${HORIZONTAL_ENERGY}" "${VERTICAL_ENERGY}" "${HORIZONTAL_SEAM}" "${VERTICAL_SEAM}" > "${CLASS}.java"
	done
}


HORIZONTAL_ENERGY=0.0
VERTICAL_ENERGY=0.0
HORIZONTAL_SEAM=""
VERTICAL_SEAM=""


