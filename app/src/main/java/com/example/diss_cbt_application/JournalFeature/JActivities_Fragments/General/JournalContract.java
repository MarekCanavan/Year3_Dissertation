package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General;

/**
 * Class holds the Strings used in database calls throughout the application
 * This is done to make the handling of the names easier
 * If it is necessary to change the name of a table or field in it, changing a string here is all that needs to be done*/
public class JournalContract {

    /*Strings used in more than 1 table*/
    public static final String _ID = "_id";
    public static final String ID = "id";
    public static final String FK_ID = "id";
    public static final String JOURNAL_COLOUR = "journalColour";

    //Table Fields
    public static final String JOURNAL_NAME = "journalName";

    //Table Fields
    public static final String ENTRY_NAME = "entryName";
    public static final String ENTRY_TIME = "entryTime";
    public static final String ENTRY_DATE = "entryDate";


    /*Other Data Fields*/

    /*Clearly Defining the Strings for the persistence of column types
    * This is so they are consistent throughout and changes only need to be made here
    * Aim of this is to reduce errors and ensure reliability of the code*/
    public static final String COLUMN = "column"; //When inserting a column to the database
    public static final String PERCENTAGE = "percentage"; //When inserting a percentage to the database


}
