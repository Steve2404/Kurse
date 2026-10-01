# Lab 07 - A COMPLETER (lu par ../run.sh). Les modules sont dans ../src ; on NE TOUCHE PAS aux module-info.
# Chaque variable contient des options pour javac ou java, par exemple : "--option module/package=autre.module".

# TODO 1 : option de COMPILATION pour que vault.inspector puisse utiliser com.example.vault.internal.
COMPILE_FLAGS=""

# TODO 2 : la meme autorisation, mais a l'EXECUTION (javac et java ne partagent rien).
RUN_EXPORTS=""

# TODO 3 : option d'EXECUTION pour autoriser la reflexion profonde (setAccessible) sur com.example.vault.api.
RUN_OPENS=""

# TODO 4 : option d'EXECUTION pour que le module vault.audit soit charge alors que personne ne le requiert.
RUN_ADD_MODULES=""
