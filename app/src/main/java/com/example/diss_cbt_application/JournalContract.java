package com.example.diss_cbt_application;

public class JournalContract {

    /*Strings used in more than 1 table*/
    public static final String _ID = "_id";
    public static final String TABLE_ID = "tableID";
    public static final String JOURNAL_COLOUR = "journalColour";


    /*String for Journal Names Table*/

    public static final String JOURNAL_NAMES = "JournalNames";//Table Name

    //Table Fields
    public static final String JOURNAL_NAME = "journalName";
    public static final String ARCHIVED = "archived";

    /*Strings for Journal Structure Table*/

    public static final String JOURNAL_STRUCTURE = "JournalStructure";//Table Name

    //Table Fields
    public static final String COLUMN_NAME = "columnName";
    public static final String COLUMN_TYPE = "columnType";

    /*Strings for SEntry Table*/
    public static final String SENTRY = "SEntry"; //Table Name

    //Table Fields
    public static final String ENTRY_NAME = "entryName";
    public static final String ENTRY_DATE_TIME = "entryDateTime";
    public static final String ENTRY_JOURNAL_TYPE = "entryJournalType";

    /*Strings for SEntryData*/
    public static final String SENTRY_DATA = "SEntryData";

    //Table Fields
    //COLUMN_NAME and COLUMN_TYPE both defined for in Journal Structure
    public static final String ENTRY_DATA = "entryData";
    public static final String ENTRY_ID = "entryID";
    public static final String ENTRY_TIME = "entryTime";
    public static final String ENTRY_DATE = "entryDate";


}
