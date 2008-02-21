#!/bin/sh

appli=squalixdev

fatal_error()
{
  echo "Erreur fatale " $*
  exit 3
}

# Test du nombre d'arguments
[ $# -ne 1 ] && fatal_error "Nombre d'arguments incorrect"

# Obtention du pid par la recherche d'une commande qui contienne le nom de 
# l'application dans sa ligne de commande avec l'option -Dbatch=
pid=`pgrep -d @ -f Dbatch=${appli}` || fatal_error "Processus ${appli} introuvable"
# V�rification de l'unicit� de pid
[ `expr "${pid}" : '.*@.*'` -eq 0 ] || fatal_error "Processus multiples ${pid}"
# Envoi du signal SIGTERM
kill ${pid}
