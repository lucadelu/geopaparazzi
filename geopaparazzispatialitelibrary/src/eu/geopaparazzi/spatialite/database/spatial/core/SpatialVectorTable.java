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
package eu.geopaparazzi.spatialite.database.spatial.core;
import java.io.File;
import eu.geopaparazzi.library.database.GPLog;

// https://www.gaia-gis.it/fossil/libspatialite/wiki?name=metadata-4.0

/**
 * A vector table from the spatial db.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class SpatialVectorTable {

    private final String name;
    private final String geomName;
    private final int geomType;
    private final String srid;
    private Style style;
    private File file_map; // all DatabaseHandler/Table classes should use these names
    private String s_map_file; // [with path] all DatabaseHandler/Table classes should use these names
    private String s_name_file; // [without path] all DatabaseHandler/Table classes should use these names
    private String s_name; // all DatabaseHandler/Table classes should use these names
    private String s_description=""; // all DatabaseHandler/Table classes should use these names
    private String s_map_type="geometry"; // all DatabaseHandler/Table classes should use these names
    private double centerX; // wsg84
    private double centerY; // wsg84
    private double bounds_west; // wsg84
    private double bounds_east; // wsg84
    private double bounds_north; // wsg84
    private double bounds_south; // wsg84
    private int minZoom=0;
    private int maxZoom=22;
    private int defaultZoom=17;
    private int i_row_count=0;
    private int i_coord_dimension=0;
    private int i_spatial_index_enabled=0;
    private String s_last_verified="";
    private boolean checkDone = false;
    private boolean isPolygon = false;
    private boolean isLine = false;
    private boolean isPoint = false;
    private boolean isGeometryCollection = false;

    public SpatialVectorTable( String  s_map_file,String name, String geomName, int geomType, String srid, double[] center , double[] bounds,
     String s_layer_type,int i_row_count,int i_coord_dimension,int i_spatial_index_enabled,String s_last_verified ) {
        this.s_map_file = s_map_file;
        this.file_map=new File(s_map_file);
        s_name_file=file_map.getName();
        this.name = name;
        this.geomName = geomName;
        this.geomType = geomType;
        this.srid = srid;
        this.centerX = center[0];
        this.centerY = center[1];
        this.bounds_west = bounds[0];
        this.bounds_south = bounds[1];
        this.bounds_east = bounds[2];
        this.bounds_north = bounds[3];
        this.s_map_type=s_layer_type;
        this.i_row_count=i_row_count;
        this.i_coord_dimension=i_coord_dimension;
        this.i_spatial_index_enabled=i_coord_dimension;
        this.s_last_verified=s_last_verified;
        checkType();
        String s_dump="isPoint["+isPoint+"] isLine["+isLine+"] isPolygon["+isPolygon+"] isGeometryCollection["+isGeometryCollection+"]";
        // GPLog.androidLog(-1,"SpatialVectorTable geomName[" + geomName + "] name["+name+"] types["+s_dump+"]");
    }
    // -----------------------------------------------
    /**
      * Return long name of map/file
      *
      * <p>default: file name with path and extention
      * <p>mbtiles : will be a '.mbtiles' sqlite-file-name
      * <p>map : will be a mapforge '.map' file-name
      *
      * @return file_map as File
      */
    public String getFileNamePath() {
        return this.s_map_file; // file_map.getAbsolutePath();
    }
    // -----------------------------------------------
    /**
      * Return short name of map/file
      *
      * <p>default: file name without path but with extention
      *
      * @return file_map.getAbsolutePath();
      */
    public String getFileName() {
        return this.s_name_file; // file_map.getName();
    }
    // -----------------------------------------------
    /**
      * Return type of map/file
      *
      * <p>raster: can be different: mbtiles,db,sqlite,gpkg
      * <p>mbtiles : mbtiles
      * <p>map : map
      *
      * @return s_name as short name of map/file
      */
    public String getMapType() {
        return this.s_map_type;
    }
    // -----------------------------------------------
    /**
      * Return String of bounds [wms-format]
      *
      * <p>x_min,y_min,x_max,y_max
      *
      * @return bounds formatted using wms format
      */
    public String getBounds_toString() {
        return bounds_west+","+bounds_south+","+bounds_east+","+bounds_north;
    }
    // -----------------------------------------------
    /**
      * Return String of Map-Center with default Zoom
      *
      * <p>x_position,y_position,default_zoom
      *
      * @return center formatted using mbtiles format
      */
    public String getCenter_toString() {
        return centerX+","+centerY+","+defaultZoom;
    }
    // -----------------------------------------------
    /**
      * Return long description of map/file
      *
      * <p>default: s_name with bounds and center
      * <p>mbtiles : metadata description'
      * <p>map : will be value of 'comment', if not null
      *
      * @return s_description long description of map/file
      */
    public String getDescription() {
        if ((this.s_description == null) || (this.s_description.length() == 0) || (this.s_description.equals(this.s_name)))
         setDescription(getName()); // will set default values with bounds and center if it is the same as 's_name' or empty
        return this.s_description; // long comment
    }
     // -----------------------------------------------
    /**
      * Set long description of map/file
      *
      * <p>default: s_name with bounds and center
      * <p>mbtiles : metadata description'
      * <p>map : will be value of 'comment', if not null
      *
      * @return s_description long description of map/file
      */
    public void setDescription(String s_description) {
        if ((s_description == null) || (s_description.length() == 0) || (s_description.equals(this.s_name)))
        {
         this.s_description = getName()+" bounds["+getBounds_toString()+"] center["+getCenter_toString()+"]";
        }
        else
         this.s_description = s_description;
    }
    // -----------------------------------------------
    /**
      * Return map-file as 'File'
      *
      * <p>if the class does not fail, this file exists
      * <p>mbtiles : will be a '.mbtiles' sqlite-file
      * <p>map : will be a mapforge '.map' file
      *
      * @return file_map as File
      */
    public File getFile() {
        return this.file_map;
    }
    // -----------------------------------------------
    /**
      * Return Min Zoom
      *
      * <p>default :  0
      * <p>mbtiles : taken from value of metadata 'minzoom'
      * <p>map : value is given in 'StartZoomLevel'
      *
      * @return integer minzoom
      */
    public int getMinZoom() {
        return minZoom;
    }
    // -----------------------------------------------
    /**
      * Return Max Zoom
      *
      * <p>default :  22
      * <p>mbtiles : taken from value of metadata 'maxzoom'
      * <p>map : value not defined, seems to calculate bitmap from vector data [18]
      *
      * @return integer maxzoom
      */
    public int getMaxZoom() {
        return maxZoom;
    }
    // -----------------------------------------------
    /**
      * Return Min/Max Zoom as string
      *
      * <p>default :  1-22
      * <p>mbtiles : taken from value of metadata 'min/maxzoom'
      *
      * @return String min/maxzoom
      */
    public String getZoom_Levels() {
        return getMinZoom()+"-"+getMaxZoom();
    }
    public String getName() {
        return name;
    }

    public String getGeomName() {
        return geomName;
    }

    public int getGeomType() {
        return geomType;
    }

    public String getSrid() {
        return srid;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle( Style style ) {
        this.style = style;
    }

    public boolean isPolygon() {
        return isPolygon;
    }
    public boolean isLine() {
        return isLine;
    }
    public boolean isPoint() {
        return isPoint;
    }
    public boolean isGeometryCollection() {
        return isPoint;
    }

    private void checkType() {
        if (checkDone) {
            return;
        }
        GeometryType TYPE = GeometryType.forValue(geomType);
        switch( TYPE ) {
        case POLYGON_XY:
        case POLYGON_XYM:
        case POLYGON_XYZ:
        case POLYGON_XYZM:
        case MULTIPOLYGON_XY:
        case MULTIPOLYGON_XYM:
        case MULTIPOLYGON_XYZ:
        case MULTIPOLYGON_XYZM:
            isPolygon = true;
            break;
        case POINT_XY:
        case POINT_XYM:
        case POINT_XYZ:
        case POINT_XYZM:
        case MULTIPOINT_XY:
        case MULTIPOINT_XYM:
        case MULTIPOINT_XYZ:
        case MULTIPOINT_XYZM:
            isPoint = true;
            break;
        case LINESTRING_XY:
        case LINESTRING_XYM:
        case LINESTRING_XYZ:
        case LINESTRING_XYZM:
        case MULTILINESTRING_XY:
        case MULTILINESTRING_XYM:
        case MULTILINESTRING_XYZ:
        case MULTILINESTRING_XYZM:
            isLine = true;
            break;
        case GEOMETRYCOLLECTION_XY:
        case GEOMETRYCOLLECTION_XYM:
        case GEOMETRYCOLLECTION_XYZ:
        case GEOMETRYCOLLECTION_XYZM:
         isGeometryCollection = true;
         break;
        default:
            throw new IllegalArgumentException("No geom type for: " + TYPE+" isGeometryCollection["+isGeometryCollection+"]");
        }
        checkDone = true;
    }

    public void makeDefaultStyle() {
        style = new Style();
        style.name = name;
    }
}
