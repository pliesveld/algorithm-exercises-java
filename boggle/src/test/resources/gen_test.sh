#!/bin/bash






for FILE in $(ls board-points*);
do 
echo $FILE; 

SRC_FILE="${FILE}"

POINTS="${SRC_FILE##board-points}"
POINTS="${POINTS%%.txt}"

CLASS="BoardSolverVerifyPoints_${POINTS}"
FILENAME="${CLASS}.java"

cat > ${FILENAME} <<EOF
public class ${CLASS} extends AbstractBoardVerifyPoints {

    @Override
    String getBoardFilename() {
        return "board-points${POINTS}.txt";
    }

    @Override
    int getExpectedPoints() {
        return ${POINTS};
    }

}
EOF


done
