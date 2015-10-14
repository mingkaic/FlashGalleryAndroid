package com.chen.mingkai.flashgallery;

public class PhotoDbSchema {
    public static final class PhotoTable {
        public static final String NAME = "photos";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String Title = "title";
            public static final String Date = "date";
            public static final String Description = "description";
        }
    }
}
