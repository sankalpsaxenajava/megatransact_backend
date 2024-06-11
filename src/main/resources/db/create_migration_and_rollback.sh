#
# --------------------------------------------------
# Currently working on: Linux, Mac and Windows with Git Bash
# --------------------------------------------------
#
# This script creates files based in utc timestamp granting liabilty when creating database migrations.
# By default, the migration file will be empty but the rollback will be created with a warning text at the top.
# They will be placed inside a directory called migration and rollback.
# Those must be in the same directory level as the script.
#
# If you want to create a migration without rollback, use the create_migration.sh script instead.
# If you want to change the output directory, change MIGRATION_OUTPUT_FOLDER and/or ROLLBACK_OUTPUT_FOLDER.
#
# Just enter a name you want when asked, and it will be created with the following pattern:
# OUTPUT_FILE_PREFIX + CURRENT_UTC_TIMESTAMP + "__" + [MIGRATION_FILE_NAME or ROLLBACK_FILE_NAME] + OUTPUT_FILE_SUFFIX
#

# SETTINGS START
MIGRATION_OUTPUT_FOLDER="./migration"
ROLLBACK_OUTPUT_FOLDER="./rollback"
OUTPUT_FILE_PREFIX="V"
CURRENT_UTC_TIMESTAMP=$(date -u +%s)
OUTPUT_FILE_SUFFIX=".sql"
# SETTINGS END

echo "--------------------------------------------------"
echo "CREATE MIGRATION AND ROLLBACK FILE"
echo "--------------------------------------------------"
echo "Type a name for your file (blank spaces are not supported):"
echo "---- Use snake_case_in_your_name to separate words."
echo "---- SIGINT (CTRL + C) to cancel."
read BRANCH_NAME

MIGRATION_FILE_NAME=$OUTPUT_FILE_PREFIX$CURRENT_UTC_TIMESTAMP"__"$BRANCH_NAME$OUTPUT_FILE_SUFFIX
ROLLBACK_FILE_NAME=$OUTPUT_FILE_PREFIX$CURRENT_UTC_TIMESTAMP"__rollback_"$BRANCH_NAME$OUTPUT_FILE_SUFFIX

echo "Creating migration output folder $MIGRATION_OUTPUT_FOLDER (if not exists)"
mkdir -p $MIGRATION_OUTPUT_FOLDER

echo "Creating rollback output folder $ROLLBACK_OUTPUT_FOLDER (if not exists)"
mkdir -p $ROLLBACK_OUTPUT_FOLDER

echo "Creating migration file $MIGRATION_FILE_NAME in $MIGRATION_OUTPUT_FOLDER"
touch $MIGRATION_OUTPUT_FOLDER/$MIGRATION_FILE_NAME

echo "Creating rollback file $ROLLBACK_FILE_NAME in $ROLLBACK_OUTPUT_FOLDER"
echo "-- --------------------------------------------" > $ROLLBACK_OUTPUT_FOLDER/$ROLLBACK_FILE_NAME
echo "-- WARNING: THIS IS A DATABASE ROLLBACK SCRIPT!" >> $ROLLBACK_OUTPUT_FOLDER/$ROLLBACK_FILE_NAME
echo "-- --------------------------------------------" >> $ROLLBACK_OUTPUT_FOLDER/$ROLLBACK_FILE_NAME

echo "--------------------------------------------------"
echo "Finished creating migration and rollback files."
echo "--------------------------------------------------"