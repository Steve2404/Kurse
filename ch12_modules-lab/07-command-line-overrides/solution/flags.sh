# Lab 07 - corrige (lu par ../run.sh).

# --add-exports module/package=lecteur : comme un "exports ... to" ajoute de l'exterieur, pour javac.
COMPILE_FLAGS="--add-exports vault.core/com.example.vault.internal=vault.inspector"

# L'execution ne se souvient pas des options de compilation : il faut repeter --add-exports pour java.
RUN_EXPORTS="--add-exports vault.core/com.example.vault.internal=vault.inspector"

# --add-opens = un "opens ... to" ajoute de l'exterieur : reflexion profonde, a l'EXECUTION seulement.
RUN_OPENS="--add-opens vault.core/com.example.vault.api=vault.inspector"

# --add-modules ajoute une racine au graphe : vault.audit est resolu meme si aucun module ne le requiert.
RUN_ADD_MODULES="--add-modules vault.audit"
