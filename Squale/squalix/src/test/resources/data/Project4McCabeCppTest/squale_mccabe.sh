#!/DINB/local/bin/bash --login

# Les param�tres suivants sont ceux utilis�s par squalix
# pour faire l'analyse des violations

VIEW_PATH=$1

cd ${VIEW_PATH}/vobs/squale/src/squalixTest/data/Project4McCabeCppTest

# Lignes � int�grer au makefile noyau DINB

# On remplace les directives #nnn par #line nnn
# la regexp n'utilise pas la forme [0-9]+ mais [0-9][0-9]* a cause de l'interpretation du + par make ou le shell
CC_RULE_DINB='$(CC_DINB) $< | sed -e "s/^#\([0-9][0-9]*\)/#line \\1/g" > `dirname $@`/`basename $@ .o`.i'
export CC_RULE_DINB
# L'option -E permet de pr�server les directives de num�ro de ligne pour McCabe qui se sert
# de ces indications pour �carter les objets d�finis dans des includes systems
CC_DINB_BASE='$(ATRIA) $(CC) -E $(CFLAGS)'
export CC_DINB_BASE

make -e USE_MCCABE=1

