#!/DINB/local/bin/bash --login

# Les param�tres suivants sont ceux utilis�s par squalix
# pour faire l'analyse des violations
export VIEW_PATH=$1
export RULESET=$2
export PROJECT_FILE=$3
export REPORT_PATH=$4

cd ${VIEW_PATH}/vobs/squale/src/squalixTest/data/Project4McCabeCppTest

/OUTILS/Parasoft/c++test/6.7.2.2/bin/cpptest -Zf -Zocpf ${PROJECT_FILE} -Zmcl "make -e CC=\${cpptest_scan}"  -Ztc ${RULESET} -Zgx --Zxml_report_directory ${REPORT_PATH}
