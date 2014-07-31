/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2010  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.hydrologis.geopaparazzi.database;

/**
 * Enumerations containing the database fields.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class TableDescriptions {

    /**
     * The metadata table name.
     */
    public static final String TABLE_METADATA = "metadata";
    /**
     * The notes table name.
     */
    public static final String TABLE_NOTES = "notes";
    /**
     * Image table name.
     */
    public static final String TABLE_IMAGES = "images";
    /**
     * Image data table name.
     */
    public static final String TABLE_IMAGE_DATA = "imagedata";
    /**
     * gpslog table name.
     */
    public static final String TABLE_GPSLOGS = "gpslogs";
    /**
     * gpslog data table name.
     */
    public static final String TABLE_GPSLOG_DATA = "gpslogsdata";
    /**
     * gpslog properties table name.
     */
    public static final String TABLE_GPSLOG_PROPERTIES = "gpslogsproperties";

    public static enum MetadataTableFields {
        /**
         * The field name for the keys.
         */
        COLUMN_KEY("key", String.class),
        /**
         * The field name for the values.
         */
        COLUMN_VALUE("value", String.class),
        /**
         * Project name/title key to use.
         */
        KEY_NAME("name", String.class),
        /**
         * Description key to use.
         */
        KEY_DESCRIPTION("description", String.class),
        /**
         * Notes key to use.
         */
        KEY_NOTES("notes", String.class),
        /**
        * Creation timestamp key to use.
        */
        KEY_CREATIONTS("creationts", Long.class),
        /**
        * Last available timestamp key to use.
        */
        KEY_LASTTS("lastts", Long.class),
        /**
         * The user that created the project key to use.
         */
        KEY_CREATIONUSER("creationuser", String.class),
        /**
         * The last user working on the project key to use.
         */
        KEY_LASTUSER("lastuser", String.class);

        private String fieldName;
        private Class fieldClass;

        MetadataTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum NotesTableFields {
        /**
         * id of the note, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * Longitude of the note in WGS84.
         */
        COLUMN_LON("lon", Double.class),
        /**
         * Latitude of the note in WGS84.
         */
        COLUMN_LAT("lat", Double.class),
        /**
         * Elevation of the note.
         */
        COLUMN_ALTIM("altim", Double.class),
        /**
         * Timestamp of the note.
         */
        COLUMN_TS("ts", Long.class),
        /**
         * Description of the note.
         */
        COLUMN_DESCRIPTION("description", String.class),
        /**
         * Simple text of the note.
         */
        COLUMN_TEXT("text", String.class),
        /**
         * Form data of the note.
         */
        COLUMN_FORM("form", String.class),
        /**
         * Is dirty field (0 = false, 1 = true)
         */
        COLUMN_ISDIRTY("isdirty", Integer.class),
        /**
         * Style of the note.
         */
        COLUMN_STYLE("style", String.class);

        private String fieldName;
        private Class fieldClass;

        NotesTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum ImageTableFields {
        /**
         * id of the note, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * Longitude of the note in WGS84.
         */
        COLUMN_LON("lon", Double.class),
        /**
         * Latitude of the note in WGS84.
         */
        COLUMN_LAT("lat", Double.class),
        /**
         * Elevation of the note.
         */
        COLUMN_ALTIM("altim", Double.class),
        /**
         * Timestamp of the note.
         */
        COLUMN_TS("ts", Long.class),
        /**
         * The azimuth of the picture.
         */
        COLUMN_AZIM("azim", Double.class),
        /**
         * A name or text for the image.
         */
        COLUMN_TEXT("text", String.class),
        /**
         * Is dirty field (0=false, 1=true)
         */
        COLUMN_ISDIRTY("isdirty", Integer.class),
        /**
         * An optional note id, to which it is bound to.
         */
        COLUMN_NOTE_ID("note_id", Long.class),
        /**
         * The id of the connected image data.
         */
        COLUMN_IMAGEDATA_ID("imagedata_id", Long.class);

        private String fieldName;
        private Class fieldClass;


        ImageTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum ImageDataTableFields {
        /**
         * id of the note, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * The image data.
         */
        COLUMN_IMAGE("imagedata", byte[].class);

        private String fieldName;
        private Class fieldClass;

        ImageDataTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum GpsLogsTableFields {
        /**
         * id of the log, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * the start UTC timestamp.
         */
        COLUMN_LOG_STARTTS("startts", Long.class),
        /**
         * the end UTC timestamp.
         */
        COLUMN_LOG_ENDTS("endts", Long.class),
        /**
         * The length of the track in meters, as last updated.
         */
        COLUMN_LOG_LENGTHM("lengthm", Double.class),
        /**
         * Is dirty field (0=false, 1=true)
         */
        COLUMN_LOG_ISDIRTY("isdirty", Integer.class),
        /**
         * the name of the log.
         */
        COLUMN_LOG_TEXT("text", String.class);

        private String fieldName;
        private Class fieldClass;

        GpsLogsTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum GpsLogsDataTableFields {
        /**
         * id of the log, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * the longitude of the point.
         */
        COLUMN_DATA_LON("lon", Double.class),
        /**
         * the latitude of the point.
         */
        COLUMN_DATA_LAT("lat", Double.class),
        /**
         * the elevation of the point.
         */
        COLUMN_DATA_ALTIM("altim", Double.class),
        /**
         * the UTC timestamp
         */
        COLUMN_DATA_TS("ts", Long.class),
        /**
         * the id of the parent gps log.
         */
        COLUMN_LOGID("logid", Long.class);

        private String fieldName;
        private Class fieldClass;

        GpsLogsDataTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }

    public static enum GpsLogsPropertiesTableFields {
        /**
         * id of the log, Generated by the db.
         */
        COLUMN_ID("_id", Long.class),
        /**
         * field for log visibility.
         */
        COLUMN_PROPERTIES_VISIBLE("visible", Integer.class),
        /**
         * the lgo stroke width.
         */
        COLUMN_PROPERTIES_WIDTH("width", Float.class),
        /**
         * the log stroke color.
         */
        COLUMN_PROPERTIES_COLOR("color", String.class),
        /**
         * the id of the parent gps log.
         */
        COLUMN_LOGID("logid", Long.class);

        private String fieldName;
        private Class fieldClass;

        GpsLogsPropertiesTableFields(String fieldName, Class fieldClass) {
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldClass() {
            return fieldClass;
        }
    }
}
