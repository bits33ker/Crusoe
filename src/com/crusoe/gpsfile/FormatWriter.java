package com.crusoe.gpsfile;

public interface FormatWriter {

	  /**
	   * @return The file extension (i.e. gpx, kml, ...)
	   */
	  String getExtension();

	  /**
	   * Writes the header.
	   * This is chance for classes to write out opening information.
	   */
	  void writeHeader();

	  /**
	   * Writes the footer.
	   * This is chance for classes to write out closing information.
	   */
	  void writeFooter();

	  /**
	   * Write a way point.
	   *
	   * @param waypoint
	   */
	  void writeWaypoint(WayPoint waypoint);

	  /**
	   * Close the underlying file handle.
	   */
	  void close();
	}
